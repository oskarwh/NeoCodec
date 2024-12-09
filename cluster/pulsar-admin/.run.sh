bash .build.sh
zip -d target/pulsar-admin-1.0.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
java -jar ./target/pulsar-admin-1.0.jar $1 $2