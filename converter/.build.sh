mvn clean package
zip -d target/converter-1.0-SNAPSHOT.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
docker build -f ./Dockerfile.converter -t converter:latest .