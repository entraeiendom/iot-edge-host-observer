#!/usr/bin/env bash
DOCKER_USER=${1:-.}
TAG=${2:-alpine}
echo build -t $DOCKER_USER/iot-edge-host-observer:$TAG .
docker build -t $DOCKER_USER/iot-edge-host-observer:$TAG .
