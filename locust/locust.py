from locust import HttpUser, task, between
import locust.runners
from os import walk
import os
import random, requests
import sys
import pulsar
import time

sys.path.append('proto')

from proto import payload_pb2


INPUT_TOPIC = "persistent://public/inout/input-topic"
OUTPUT_TOPIC = "persistent://public/neocodec/"
VIDEO_PATH = "./videos"
VIDEO_TYPE_LIST = ["mp4", "avi", "mov"]
REST_URL = "http://localhost:8095/api/converter"
locust.runners.HEARTBEAT_INTERVAL = 30

class QuickstartUser(HttpUser):
    wait_time = between(30, 60)
    host = "http://localhost:23456"

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
        self.consumer  = self.client.subscribe(OUTPUT_TOPIC + str(self.id), "locust", message_listener=self.receive_message) # Consume from client specific output topic
        print("Pulsar Client Started")

    @task
    def produce_message(self):
        print("Producing Message...")
        self.start_time = time.time()
        self.producer.send(self.fetch_message().SerializeToString())

    def on_stop(self):
        print("Stopping Locust Client")
        self.consumer.close()
        self.client.close()

    def receive_message(self, consumer, message):
        runtime = str(time.time() - self.start_time)
        print("Message consumed after " + runtime + " seconds")

        with open("results.csv", "a") as file:
                file.write(runtime + ";" + str(time.time()) + ";" + str(self.video_size) + "\n")

        try:
            print("Received message id='{}'".format(message.message_id()))
            # Acknowledge successful processing of the message
            consumer.acknowledge(message)
        except Exception:
            # Message failed to be processed
            print("Failed to process message")
            consumer.negative_acknowledge(message)

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
        target_type = VIDEO_TYPE_LIST[index]

        # Build proto payload
        payload = payload_pb2.NeoPayload()
        
        self.video_size = len(video_bytes)

        payload.file.file = video_bytes
        payload.file.fileName = video_name 
        payload.file.targetType = target_type

        payload.metadata.clientId = self.id
        payload.metadata.error = 0

        return payload