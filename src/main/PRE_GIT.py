#!/usr/bin/env python2.7

import sys
f = open ("/home/koltai/temp.txt","a")
f.write(str(sys.argv))
f.write("\r")
f.close()