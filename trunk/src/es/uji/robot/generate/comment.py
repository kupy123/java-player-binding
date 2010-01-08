#!/usr/bin/env python

#Copyright (C) 2009  Leo Nomdedeu, David Olmos
#
#This program is free software: you can redistribute it and/or modify
#it under the terms of the GNU General Public License as published by
#the Free Software Foundation, either version 3 of the License, or
#any later version.
#
#This program is distributed in the hope that it will be useful,
#but WITHOUT ANY WARRANTY; without even the implied warranty of
#MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#GNU General Public License for more details.
#
#You should have received a copy of the GNU General Public License
#along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
######################################################################
#
# Authors: Leo Nomdedeu, David Olmos
# Release: 0.1pre_alfa
# Changelog:
#        0.1pre_alfa: Initial release
######################################################################

import os
import sys
from stat import S_ISDIR, S_ISREG

USAGE = 'USAGE: comment.py \n\t <-c> <Comment the code between the tags> \n\t <-u> <Uncomment the code between the tags> \n\t <-D> <Directory to start commenting files> \n\t -T Tag provided by the user\n'
INI_TAG = '//@@IF'
END_TAG = '//@@ENDIF\n'
PATH = '/Users/david/Documents/workspace/jc/es/uji/robot/test'

def commentFile(file, userTag):
    f = open(file, 'r')
    res = ""
    tag = False
    for line in f.readlines():
        if INI_TAG in line:
            res += line
            if userTag in line and not "!"+userTag in line: # Excludes things like "!PLAYER1"
                tag = True
        elif line == END_TAG:
            tag = False
            res += END_TAG
        else:
            if tag:
                res += '//@' + line
            else:
                res += line
    
    f.close()
    f = open(file, 'w')
    f.write(res)
            
    
def uncommentFile(file, userTag):
    f = open(file, 'r')
    res = ""
    tag = False
    for line in f.readlines():
        if INI_TAG in line:
            res += line
            if userTag in line and not "!"+userTag in line: # Excludes things like "!PLAYER1"
                tag = True
        elif line == END_TAG:
            tag = False
            res += END_TAG
        else:
            if tag:
                if line[:3] == "//@":
                    # Delete three first characters to erase the comment at the beginning of the line
                    res += line[3:] 
                else:
                    res += line
            else:
                res += line
                
    f.close()
    f = open(file, 'w')
    f.write(res)

comment = False
uncomment = False

USER_TAG = ""
dir = False

if len(sys.argv) < 4:
    print USAGE
    sys.exit(-1)

for i in xrange(len(sys.argv)):
    # Comment
    if sys.argv[i] == "-c":
        comment = True
        
    # Uncomment
    elif sys.argv[i] == "-u":
        uncomment = True
        
    # Tag 
    elif sys.argv[i] == "-T":
        if len(sys.argv) < i+2:
            print "Not a suitable tag given"
            sys.exit(-1)
        else:
            USER_TAG = sys.argv[i+1]
            
    # Directory
    elif sys.argv[i] == "-D":
        dir = True
        if len(sys.argv) < i+2:
            print "Not a suitable directory given"
            sys.exit(-1)
        else:
            if os.path.exists(sys.argv[i+1]):
                PATH = sys.argv[i+1]
            else:
                print "Directory does not exist"
                sys.exit(-1)
            
            

# List objects in the initial directory
files = os.listdir(PATH)

for file in files:
    file = os.path.join(PATH, file)
    tipo = os.stat(file).st_mode
    if S_ISDIR(tipo):
        #print "Directory -> " + file
        # Add files in new directory to initial list of files
        for newFile in os.listdir(file):
            #print "Append...\t " + newFile
            files.append(os.path.join(file,newFile))
    elif S_ISREG(tipo):
        #print "Regular file -> " + file
        # Only treat .java files
        if os.path.splitext(file)[1] == ".java":
            if comment:
                commentFile(file, USER_TAG)
            elif uncomment:
                uncommentFile(file, USER_TAG)
        
    