#!/usr/bin/env bash
DOCKER_USER=${1:-.}
TAG=${2:-alpine}
CONNECTION_STRING=${3:-connection-to-eg-azure-iot}
echo docker run --name=iot-edge-host-observer $DOCKER_USER/iot-edge-host-observer:$TAG
docker run --name=iot-edge-host-observer $DOCKER_USER/iot-edge-host-observer:$TAG
