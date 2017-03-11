#!/bin/bash
# create_db_docker.sh --- Hasan Abdel Halim ( habdelhalim AT souq DOT com )
# @Creatd : 2017-01-14.
# @Revision:    0.0

CURR_DIR=`pwd`
docker rm postgres-bk && docker run -p 5432:5432 -d -v $CURR_DIR:/royal  --name=postgres-bk postgres

# vi: 
