#!/bin/bash

CREDENTIALS="$HOME/.ivy2/.credentials"
echo "realm=Sonatype Nexus Repository Manager" > $CREDENTIALS
echo "host=$NEXUS_HOST" >> $CREDENTIALS
echo "user=$NEXUS_USER" >> $CREDENTIALS
echo "password=$NEXUS_PASSWORD" >> $CREDENTIALS

