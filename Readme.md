keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks  -validity 360 -keysize 2048 -ext san=dns:localhost
keytool -export -alias selfsigned -file selfsigned.crt -keystore keystore.jks
sudo keytool -delete -alias selfsigned -keystore /Library/Java/Home/lib/security/cacerts
sudo keytool -import -trustcacerts -alias selfsigned -file selfsigned.crt -keystore /Library/Java/Home/lib/security/cacerts