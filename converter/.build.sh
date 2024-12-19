cd ../shared
mvn clean install -U
cd ../converter
mvn clean package -U

zip -d target/converter-1.0-SNAPSHOT.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF