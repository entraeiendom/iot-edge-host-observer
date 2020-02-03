#!/usr/bin/env bash
DOCKER_USER=${1:-.}
TAG=${2:-raspberrypi4}
CONNECTION_STRING=${3:-connection-to-eg-azure-iot}
echo docker run -e DEVICE_CONNECTION_STRING=$CONNECTION_STRING --name=iot-edge-host-observer $DOCKER_USER/iot-edge-host-observer:$TAG
docker run --network host -e DEVICE_CONNECTION_STRING=$CONNECTION_STRING --name=iot-edge-host-observer $DOCKER_USER/iot-edge-host-observer:$TAG
