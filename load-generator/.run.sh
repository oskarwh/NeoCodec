mvn clean package
zip -d target/load-generator-1.0-SNAPSHOT.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
java -jar ./target/load-generator-1.0-SNAPSHOT.jar $1