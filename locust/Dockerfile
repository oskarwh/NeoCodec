FROM locustio/locust

COPY /locust/locust.py /locust.py
COPY /locust/videos/ /videos/
#COPY /shared/src/main/proto /proto/
COPY /locust/proto/ /

#RUN python3 -m pip install protobuf
#RUN protoc --python_out=/proto --proto_path /proto/ payload.proto file.proto metadata.proto filetypes.proto

RUN python3 -m pip install locust pulsar-client
RUN python3 -m pip install protobuf



CMD ["-f", "/locust.py"]
