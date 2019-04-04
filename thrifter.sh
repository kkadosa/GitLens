#!/usr/bin/env sh

thrift -gen java -out /home/koltai/w/GitLens/src/main/java/ GitLens.thrift
thrift -gen py -out /home/koltai/w/GitLens/src/main/python/ GitLens.thrift
