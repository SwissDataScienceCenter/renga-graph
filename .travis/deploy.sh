#!/bin/bash

sbt ++$TRAVIS_SCALA_VERSION "project $COMPONENT" publish

docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD" $DOCKER_REGISTRY
sbt ++$TRAVIS_SCALA_VERSION "project $COMPONENT" docker:publishLocal
docker tag renga-graph-$COMPONENT:0.1.0-SNAPSHOT $DOCKER_REGISTRY/swissdatasciencecenter/images/renga-graph-$COMPONENT:0.1.0-SNAPSHOT
docker push $DOCKER_REGISTRY/swissdatasciencecenter/images/renga-graph-$COMPONENT:0.1.0-SNAPSHOT
