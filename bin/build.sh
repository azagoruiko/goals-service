#!/usr/bin/env bash

VER=$1
REPO="10.8.0.5:9999/docker"

export BOT_TOKEN=111
export POSTGRES_URL=111
export POSTGRES_DRIVER=org.postgresql.Driver
export POSTGRES_USER=111
export POSTGRES_PASSWORD=111
mvn clean install

docker build -t $REPO/goals-service:$VER .
docker push $REPO/goals-service:$VER

export NOMAD_ADDR="http://10.8.0.5:4646"
nomad job run ./goals-service.nomad
