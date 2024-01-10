# Wifi-Control
Use the keyboard and mouse of one computer to control other

##For receiver:

export DISPLAY=:0

java -jar -Djava.awt.headless=false receiver/target/receiver-1.1-jar-with-dependencies.jar

##For sender

java -jar sender/target/sender-1.1-jar-with-dependencies.jar {REMOTE_IP} 9876