#!/usr/bin/env bash
echo stopping iot-edge-host-observer
docker stop iot-edge-host-observer
echo removing iot-edge-host-observer
docker rm iot-edge-host-observer
echo list active docker containers
docker ps
