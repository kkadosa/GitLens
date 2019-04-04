#!/usr/bin/env sh

thrift -gen java -out /home/koltai/w/WGit/src/main/java/ WGit.thrift
thrift -gen py -out /home/koltai/w/WGit/src/main/python/ WGit.thrift
