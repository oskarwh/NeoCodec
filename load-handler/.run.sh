bash .build.sh
zip -d target/load-handler-1.0.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
java -jar ./target/load-handler-1.0.jar $1 $2