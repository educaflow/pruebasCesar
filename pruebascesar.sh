#!/bin/bash
#if [ -n "$HOME" ]; then
#  rm -rf ${HOME}/.axelor/attachments/
#fi
set -e
clear
#sudo docker stop educaflow
docker compose down
docker compose up -d
./gradlew clean build
#./gradlew --no-daemon run --debug-jvm --port 8080 --contextPath /
./gradlew --no-daemon run --port 8080 --contextPath /
