#!/bin/sh
sudo docker kill neo
sudo docker rm neo
sudo docker run -td --name=neo -p 7474:7474 neo4j

