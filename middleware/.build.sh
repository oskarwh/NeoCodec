cd ../shared
mvn clean install -U
cd ../middleware/load-handler
mvn clean package -U
cd ../pulsar-functions/conversion-output-handler
mvn clean package -U
cd ../../

zip -d load-handler/target/load-handler-1.0.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
zip -d pulsar-functions/conversion-output-handler/target/conversion-output-handler-1.0.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
