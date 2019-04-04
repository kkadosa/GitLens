#!/usr/bin/env python2.7

import sys

sys.path.insert(0, "/home/koltai/w/WGit/src/main/python/WGit")
sys.path.insert(0, "/home/koltai/w/WGit/src/main/python/triggers")
sys.path.insert(0, "/home/koltai/thrift-0.12.0/lib/py/build/lib.linux-x86_64-2.7")

import WGitService
from ttypes import *
from thrift.protocol import TBinaryProtocol
from thrift.transport import TTransport
from thrift.transport import TSocket

repo = sys.argv[2]
user = sys.argv[3]

if sys.argv[4] == 'W':
    mode = Mode.W
else:
    mode = Mode.R

if sys.argv[6] == 'DENIED':
    result = 0
else:
    result = 1
    
socket = TSocket.TSocket('localhost', 12147)
transport = TTransport.TFramedTransport(socket)
protocol = TBinaryProtocol.TBinaryProtocol(transport)
client = WGitService.Client(protocol)

transport.open()

response = ServerResponse(client.GetResponse(Trigger.ACCESS_1, user, repo, mode, result))

transport.close()

#TODO send message
sys.exit(response.returnValue)

