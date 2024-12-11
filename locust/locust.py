from locust import HttpUser, task, between

class PulsarUser(HttpUser)
    wait_time = between(1, 5)

    @task
    def produce_message(self)
        self.client.post("/pulsar/produce"...)

    @task
    def consume_message(self)
        self.client.get("/pulsar/consume"...)