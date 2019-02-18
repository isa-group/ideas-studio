#! /bin/bash
# Create a default keystore
#if no existe variables 
#/usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -genkey -alias tomcat -keyalg RSA -storepass changeit -dname "cn=Mark Jones, ou=JavaSoft, o=Sun, c=US" -keypass changeit'CA@SYdF@xkvCrgGcS9fRDvk2HHM6#s7bH8qnff2fbC3GvTeM' -keystore ./.keystore
#else
openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out pkcs.p12 -name tomcat -password  pass:'b4z0rJPH7HNV57fqKR2dI9Upv$v5uNg4xN293k!Qrp4jXf0'

/usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -importkeystore -deststorepass 'CA@SYdF@xkvCrgGcS9fRDvk2HHM6#s7bH8qnff2fbC3GvTeM' -destkeypass 'b4z0rJPH7HNV57fqKR2dI9Upv$v5uNg4xN293k!Qrp4jXf0' -destkeystore ./.keystore -srckeystore pkcs.p12 -srcstoretype PKCS12 -srcstorepass 'b4z0rJPH7HNV57fqKR2dI9Upv$v5uNg4xN293k!Qrp4jXf0' -alias tomcat
