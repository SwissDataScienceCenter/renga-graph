#!/bin/bash

sbt ++$TRAVIS_SCALA_VERSION "project $COMPONENT" publish
