protoc --python_out=/proto --proto_path ../shared/src/main/proto  payload.proto file.proto metadata.proto filetypes.proto

cd ../
docker build -t larssonludvig/neocodec:locust -f ./locust/Dockerfile .
docker run -p 8095:8095 larssonludvig/neocodec:locust
# docker push larssonludvig/neocodec:locust
