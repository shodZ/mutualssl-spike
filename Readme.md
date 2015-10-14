keytool -genkey -keyalg RSA -alias server-private -keystore server-private-key.jks  -validity 360 -keysize 2048 -keypass serverprivatekey -storepass serverprivatestore -ext san=dns:localhost
keytool -export -alias server-private -file servercert.crt -keystore server-private-key.jks -storepass serverprivatestore
keytool -import -trustcacerts -alias server-private -file servercert.crt -keystore trusted-stuff.jks -storepass useless ;password is only used for keystore integrity

keytool -genkey -keyalg RSA -alias client-private -keystore client-private-key.jks  -validity 360 -keysize 2048 -keypass clientprivate -storepass clientprivatestore
keytool -export -alias client-private -file clientcert.crt -keystore client-private-key.jks -storepass clientprivatestore
keytool -import -trustcacerts -alias client-private -file clientcert.crt -keystore trusted-stuff.jks -storepass useless