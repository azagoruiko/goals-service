#!/usr/bin/env bash

VER=$1
REPO="192.168.0.21:9999/docker"

export BOT_TOKEN=111 && mvn clean install

docker build -t $REPO/goals-service:$VER .
docker push $REPO/goals-service:$VER

export NOMAD_ADDR="http://192.168.0.21:4646"
nomad job run ./goals-service.nomad
