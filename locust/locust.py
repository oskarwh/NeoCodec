from locust import HttpUser, task, between
from os import walk
import random, requests
import sys
sys.path.append('proto')

from proto import payload_pb2, metadata_pb2, file_pb2, filetypes_pb2

import pulsar


INPUT_TOPIC = "persistent://public/inout/input-topic"
OUTPUT_TOPIC = "persistent://public/neocodec/"
VIDEO_PATH = "./videos"
VIDEO_TYPE_LIST = filetypes_pb2.NeoFileTypes.items()
print(VIDEO_TYPE_LIST)


REST_URL = "http://localhost:8095/api/converter"

class QuickstartUser(HttpUser):
    wait_time = between(5, 6)

    def on_start(self):
        print("Starting Locust Client")

        self.filenames = next(walk(VIDEO_PATH), (None, None, []))[2]  # [] if no file
        self.filenames_len = len(self.filenames)
        print("Fetched Video Directory")
        
        self.id = -1
        try:
            r = requests.get(REST_URL, verify=False)
            if r.status_code != 200:
                print("Get request Error (None 200)")
                exit(-1)
            self.id = int(r.content)
        except :
            print("Failed Get call to cluster")
            exit(-1)
        
        print("ID Fetched: " + str(self.id))

        self.client = pulsar.Client('pulsar://localhost:6650')
        self.producer = self.client.create_producer(INPUT_TOPIC, chunking_enabled=True)
        self.consumer = self.client.subscribe(OUTPUT_TOPIC + str(self.id), "locust") # Consume from client specific output topic
        print("Pulsar Client Started")


    @task
    def produce_message(self):
        print("Producing Message...")
        self.producer.send(self.fetch_message().SerializeToString())

        print("Consuming message")
        msg = self.consumer.receive()

        try:
            print("Received message id='{}'".format(msg.message_id()))
            # Acknowledge successful processing of the message
            self.consumer.acknowledge(msg)
        except Exception:
            # Message failed to be processed
            print("Failed to process message")
            self.consumer.negative_acknowledge(msg)

        
        


    def fetch_message(self):
        # Fetch file to convert
        print("Fetching Video...")
        max = self.filenames_len - 1
        index = random.randint(0, max)
        video_name = self.filenames[index]
        print(video_name)
        with open(VIDEO_PATH + "/" + video_name, 'rb') as file:
            video_bytes = file.read()

        # Randomize conversion
        index = random.randint(0, len(VIDEO_TYPE_LIST)-1)
        target_type = VIDEO_TYPE_LIST[index][0]

        # Build proto payload
        payload = payload_pb2.NeoPayload()
        
        payload.file.file = video_bytes
        payload.file.fileName = video_name 
        payload.file.targetType = target_type

        payload.metadata.clientId = self.id
        payload.metadata.error = 0

        return payload