cd ../shared
mvn clean install -U
cd ../middleware/load-handler
mvn clean package
cd ../pulsar-functions/conversion-output-handler
mvn clean package
cd ../../

zip -d load-handler/target/load-handler-1.0.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
zip -d pulsar-functions/conversion-output-handler/target/conversion-output-handler-1.0.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF

docker build -t larssonludvig/neocodec:middleware .
docker push larssonludvig/neocodec:middleware
