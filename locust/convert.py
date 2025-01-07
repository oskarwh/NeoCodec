from os import walk
import random, requests
import sys
import pulsar
import time
import os

sys.path.append('proto')

from proto import payload_pb2

INPUT_TOPIC = "persistent://public/inout/input-topic"
OUTPUT_TOPIC = "persistent://public/neocodec/"
REST_URL = "http://localhost:8095/api/converter"

class Convert:
    def main(self):
        self.csv = False
        argv = sys.argv
        if len(argv) < 3:
            print("Usage: python3 convert.py <video_path> <output_path>")
            exit(-1)
        elif len(argv) == 4 and argv[3] == "-csv":
            self.csv = True
        elif len(argv) > 4:
            print("To many arguments")
            exit(-1)

        self.video_path = argv[1]
        self.output_path = argv[2]
        
        self.id = "-1"
        try:
            r = requests.get(REST_URL, verify=False)
            if r.status_code != 200:
                print("Get request Error (None 200)")
                exit(-1)
            self.id = r.content.decode("utf-8")
        except:
            print("Failed Get call to cluster")
            exit(-1)
        
        print("ID Fetched: " + self.id)


        self.client = pulsar.Client('pulsar://localhost:6650')
        self.producer = self.client.create_producer(INPUT_TOPIC, chunking_enabled=True)
        self.consumer = self.client.subscribe(OUTPUT_TOPIC + self.id, "locust", message_listener=self.receive_message) # Consume from client specific output topic
        print("Pulsar Client Started")

        print("Producing Message...")
        self.start_time = time.time()
        self.producer.send(self.fetch_message().SerializeToString())

        msg = self.consumer.receive()
        runtime = str(time.time() - self.start_time)
        print("Response consumed in " + runtime + " seconds")

        if self.csv:
            with open("results.csv", "a") as file:
                file.write(runtime + ";" + str(os.path.getsize(self.video_path)) + "\n")

        try:
            print("Received message id='{}'".format(msg.message_id()))
            self.consumer.acknowledge(msg)
        except Exception:
            print("Error receiving message")
            self.consumer.negative_acknowledge(msg)
            self.client.close()
            exit(-1)

        payload = payload_pb2.NeoPayload()
        payload.ParseFromString(msg.data())

        if payload.metadata.error != 0:
            print("Error occured: " + str(payload.metadata.error) + ": " + payload.metadata.errorMessage)
            self.consumer.close()
            self.client.close()
            exit(-1)

        print("Storing file...")

        with open(self.output_path, 'wb') as file:
            file.write(payload.file.file)
        print("File stored")

        print("Stopping pulsar client")
        self.consumer.close()
        self.client.close()

    def fetch_message(self):
        video_name = self.video_path.split('/')[-1]
        print(video_name)
        with open(self.video_path, 'rb') as file:
            video_bytes = file.read()

        # Build proto payload
        payload = payload_pb2.NeoPayload()
        
        payload.file.file = video_bytes
        payload.file.fileName = video_name 
        
        type = self.output_path.split('.')[-1]
        payload.file.targetType = type

        payload.metadata.clientId = int(self.id)
        payload.metadata.error = 0

        return payload

    def receive_message(self, consumer, message):
        self.message = message

if __name__ == "__main__":
    converter = Convert()
    converter.main()