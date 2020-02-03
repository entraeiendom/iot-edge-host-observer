FROM openjdk:14-jdk-alpine3.10
MAINTAINER Bard Lind <bard.lind@gmail.com>

ADD target/iot-edge-host-observer-*.jar /home/edgeobserver/iot-edge-host-observer.jar

WORKDIR "/home/edgeobserver"
CMD [ \
    "java", \
    "-jar", \
    "iot-edge-host-observer.jar" \
]


