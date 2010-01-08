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



import re
import sys
import string
import os

PATH = 'es/uji/robot/player/generated/abstractproxy/'

USAGE = 'USAGE: genAbstractProxy.py [player_interfaces.h] functiontable.c [functiontable_gen.c] playerc.h player.h <other player_interfaces.h files>\n <-D> <destination directory>'

command = ""
for i in xrange(len(sys.argv)):
    command += sys.argv[i] + " "

LICENSE = "/**\n* Copyright (C) 2009  Leo Nomdedeu, David Olmos\n*\n* ### Autogenerated file with command \n* " + command + "\n* Do not make changes to this file.\n*\n* This program is free software: you can redistribute it and/or modify\n* it under the terms of the GNU General Public License as published by\n* the Free Software Foundation, either version 3 of the License, or\n* any later version.\n*\n* This program is distributed in the hope that it will be useful,\n* but WITHOUT ANY WARRANTY; without even the implied warranty of\n* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n* GNU General Public License for more details.\n*\n* You should have received a copy of the GNU General Public License\n* along with this program.  If not, see <http://www.gnu.org/licenses/>.\n*\n*********************************************************************\n*\n* Authors: Leo Nomdedeu, David Olmos\n* Release: 0.1pre_alfa\n* Changelog:\n*\t\t0.1pre_alfa: Initial release\n*********************************************************************\n*/\n\n"


# Test if argument -D has been passed
dir = False
DIR = " "

for i in xrange(len(sys.argv)):
    if sys.argv[i] == "-D":
        dir = True
        if len(sys.argv) < i+2:
            print "Not a suitable directory gave"
            sys.exit(-1)
        else:
            if os.path.exists(sys.argv[i+1]):
                if sys.argv[i+1][-1] != "/":
                    DIR = sys.argv[i+1] + "/"
                else:
                    DIR = sys.argv[i+1]
            else:
                print "Directory does not exist"
                sys.exit(-1)


# Method used to change the name to a well formed java version
def goodName(name):
    i = 0
    res = ''
    cambiar = False
    for c in name:
        if i == 0:
            res = res + c.upper()
            i = i + 1
        elif cambiar:
            res = res + c.upper()
            cambiar = False
        elif c != '_':
            res = res + c
        elif c == '_':
            cambiar = True
            
    return res


# Method used to remove blancks from an array
def removeBlanks(vector):
    res = []
    for e in vector:
        if e != '':
            res.append(e)
            
    return res


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print USAGE
        sys.exit(-1)
        
#    infilenames = []
#    for i in xrange(1,len(sys.argv)):
#        infilenames.append(sys.argv[i])    

    infilenames = []
    directory = 0
  
    for i in xrange(1,len(sys.argv)):
        if directory != 1:
            if sys.argv[i] != '-D':
                infilenames.append(sys.argv[i])
            else:
                directory = 1
        else:
            directory = 0
    
    if len(sys.argv) > 6:
        for opt in sys.argv[6:]:
            infilenames.append(opt)
            print "processing extra file ", opt
            
    # Boolean variables indicates if we have player_interfaces.h and functiontable_gen.c or not
    isPlayerInterfaces = False
    isFunctionTableGen = False        
            
            
    # Put the four input files in different variables
    interfaces = ""
    table = ""
    functiontable = ""
    playerc = ""
    player = ""
    for f in infilenames:
        if "player_interfaces" in f:
            infile = open(f, 'r')
            interfaces += infile.read()
            infile.close()
            isPlayerInterfaces = True
        elif "gen" in f:
            infile = open(f, 'r')
            table += infile.read()
            infile.close()
            isFunctionTableGen = True
        elif "functiontable.c" in f:
            infile = open(f, 'r')
            functiontable += infile.read()
            infile.close()
        elif "playerc.h" in f:
            infile = open(f, 'r')
            playerc += infile.read()
            infile.close()
        elif "player.h" in f:
            infile = open(f, 'r')
            player += infile.read()
            infile.close()

    # strip blank lines
    pattern = re.compile('^\s*?\n', re.MULTILINE)
    if isPlayerInterfaces:
        interfaces = pattern.sub('', interfaces)
    if isFunctionTableGen:
        table = pattern.sub('', table)
    functiontable = pattern.sub('', functiontable)
    
    # ingroup declarations to find proxy
    pattern = re.compile('\s+\@defgroup\s+interface\_[\w\s]+', re.MULTILINE)
    if isPlayerInterfaces:
        found = pattern.findall(interfaces)
    else:
        found = pattern.findall(player)
    
    # find defines
    # '^#define\s+\w*\s+\d+[x]?\d*'
    defines = []
    otherDefines = []
    playerHDefines = []
    
    # pattern string detection
    pattern = re.compile('^#define\s+\w*\s+["].*["]', re.MULTILINE)
    if isPlayerInterfaces:
        defines = pattern.findall(interfaces)
        # find defines in playerc.h
        otherDefines = pattern.findall(playerc)
        # find defines in player.h
        playerHDefines = pattern.findall(player)
    else:
        defines = pattern.findall(player)
        playerHDefines = pattern.findall(playerc)
    
    

    
    # pattern number detection
    pattern = re.compile('^#define\s+\w*\s+\d+[x]?\d*', re.MULTILINE)
    if isPlayerInterfaces:
        defines += pattern.findall(interfaces)
        otherDefines += pattern.findall(playerc)
        playerHDefines += pattern.findall(player)
    else:
        defines += pattern.findall(player)
        playerHDefines += pattern.findall(playerc)
        
    
    #pattern special definitions detection
    pattern = re.compile('^#define\s+\w*\s+\(.*\)', re.MULTILINE)
    if isPlayerInterfaces:
        defines += pattern.findall(interfaces)
        otherDefines += pattern.findall(playerc)
        playerHDefines += pattern.findall(player)
    else:
        defines += pattern.findall(player)
        playerHDefines += pattern.findall(playerc)
        
    
    #find defines with comments
    # '/\*\*.*\*/\s*#define\s+\w*\s+\d+'
    definesWithComment = []
    otherDefinesWithComment = []
    playerHDefinesWithComment = []
    
    # pattern string detection
    pattern = re.compile('/\*\*.*\*/\s*#define\s+\w*\s+["].*["]', re.MULTILINE)
    if isPlayerInterfaces:
        definesWithComment = pattern.findall(interfaces)
        # find defines with comments in playerc.h
        otherDefinesWithComment = pattern.findall(playerc)
        # find defines with comments in player.h
        playerHDefinesWithComment = pattern.findall(player)
    else:
        definesWithComment = pattern.findall(player)
        playerHDefinesWithComment = pattern.findall(playerc)
        
    
    # pattern number detection
    pattern = re.compile('/\*\*.*\*/\s*#define\s+\w*\s+\d+', re.MULTILINE)
    if isPlayerInterfaces:
        definesWithComment += pattern.findall(interfaces)
        otherDefinesWithComment += pattern.findall(playerc)
        playerHDefinesWithComment += pattern.findall(player)
    else:
        definesWithComment += pattern.findall(player)
        playerHDefinesWithComment += pattern.findall(playerc)
        
    
    #pattern special definitions detection
    pattern = re.compile('/\*\*.*\*/\s*#define\s+\w*\s+\(.*\)', re.MULTILINE)
    if isPlayerInterfaces:
        definesWithComment += pattern.findall(interfaces)
        otherDefinesWithComment += pattern.findall(playerc)
        playerHDefinesWithComment += pattern.findall(player)
    else:
        definesWithComment += pattern.findall(player)
        playerHDefinesWithComment += pattern.findall(playerc)
        
    
    #find elements to be added to the table
    # '{[^}]*}'
    elements = []
    pattern = re.compile('{[^}]*}', re.MULTILINE)
    if isFunctionTableGen:
        elements = pattern.findall(table)        

    # find elements from funciontable.c to be added to the table
    universalElements = []
    pattern = re.compile('{\w*,\s*\w*,[^}]*}', re.MULTILINE)
    if isFunctionTableGen:
        universalElements = pattern.findall(functiontable)
    else:
        elements = pattern.findall(functiontable)
    
    #print 'Found ' + `len(found)` + ' proxy(s)'
    
    # Create an array with all the proxies, removing irrelevant characters from the name
    proxy = []
    for f in found:
        #proxy.append(f.split(' ')[3].split('_')[1])
        proxy.append(goodName(removeBlanks(f.split(' '))[-1].rstrip()))
                     
    
    abstract = []
    
    # Array with all the constant declarations of player.h 
    constants = []
    
    # Common defines
    filename = "CommonDefines.java"
    if dir:
        # If directories are not created, create them.
        # If the directory already exists, delete its content
        if not os.path.isdir(DIR + PATH):
            os.makedirs(DIR + PATH)
        else:
            print "Removing directory content..."
            for fileInDir in os.listdir(DIR + PATH):
                filePath = os.path.join(DIR + PATH, fileInDir)
                try:
                    if os.path.isfile(filePath):
                        os.unlink(filePath)
                except Exception, e:
                    print e
                    
            print "Finished removing content!"
        commonDefines = open (DIR + PATH + filename, 'w+')
    else:
        if not os.path.isdir("./" + PATH):
            os.makedirs("./" + PATH)
        else:
            print "Removing directory content..."
            for fileInDir in os.listdir("./" + PATH):
                filePath = os.path.join("./" + PATH, fileInDir)
                try:
                    if os.path.isfile(filePath):
                        os.unlink(filePath)
                except Exception, e:
                    print e
            print "Finished removing content!"
        commonDefines = open("./" + PATH + filename, 'w+')
        
    #commonDefines = open("../player/generated/abstractproxy/"+filename,'w+')
    
    commonDefines.write(LICENSE)
    commonDefines.write("package es.uji.robot.player.generated.abstractproxy;\n\n")
    commonDefines.write("public interface CommonDefines {\n")
    
    #define with comments extracted from player.h
    for d in playerHDefinesWithComment:
        df = d.split('#')
        # pattern to find the final string in the defines
        pattern = re.compile('\".*\"',re.MULTILINE)
        string = pattern.findall(df[1])
        
        # pattern to find special definitions, like sizeof()
        pattern = re.compile('\(.*\)',re.MULTILINE)
        special = pattern.findall(df[1])
        
        elementsDefine = df[1].split(' ')
        elementsDefine = removeBlanks(elementsDefine)
        
        # treat the special case where PLAYER_MAX_PAYLOAD_SIZE appears
        if "PLAYER_MAX_PAYLOAD_SIZE" in df[1]:
            constants.append(elementsDefine[1])
            commonDefines.write("    %s\n" % (df[0].rstrip('\n')))
            commonDefines.write("    public static final int %s = PLAYER_MAX_MESSAGE_SIZE; //%s;\n" % (elementsDefine[1],special[0]))
        
        # For each constant definition ensure that is not already defined
        if not elementsDefine[1] in constants:
            constants.append(elementsDefine[1])
            commonDefines.write("    %s\n" % (df[0].rstrip('\n')))
            if '"' in elementsDefine[-1]:
                commonDefines.write("    public static final String %s = %s;\n" % (elementsDefine[1], string[0]))
            else:
                commonDefines.write("    public static final int %s = %s;\n" % (elementsDefine[1], elementsDefine[-1]))
                
    #define extracted from player.h
    for d in playerHDefines:
            # pattern to find the final string in the defines
            pattern = re.compile('\".*\"',re.MULTILINE)
            string = pattern.findall(d)
            
            df = d.split(' ')
            df = removeBlanks(df)
            
            # For each constant definition ensure that is not already defined
            if not df[1] in constants:
                constants.append(df[1])
                if '"' in df[-1]:
                    commonDefines.write("    public static final String %s = %s;\n" % (df[1], string[0]))
                else:
                    commonDefines.write("    public static final int %s = %s;\n" % (df[1], df[-1]))
                    
    # Add defines from player.h, which not correspond to any proxy,  to commonDefines                
    if not isPlayerInterfaces:
        
        for d in definesWithComment:
            df = d.split('#')
            # pattern to find the final string in the defines
            pattern = re.compile('\".*\"',re.MULTILINE)
            string = pattern.findall(df[1])
            
            # pattern to find special definitions, like sizeof()
            pattern = re.compile('\(.*\)',re.MULTILINE)
            special = pattern.findall(df[1])
            
            elementsDefine = df[1].split(' ')
            elementsDefine = removeBlanks(elementsDefine)
            
            esta = False
            for p in proxy:
                if p.lower() != 'player':
                    if p.lower() in df[1].lower():
                        esta = True 
                        
            # It corresponds to a client define
            if "PLAYER_PLAYER" in df[1]:
                esta = True
                
            # treat the special case where PLAYER_MAX_PAYLOAD_SIZE appears
            if "PLAYER_MAX_PAYLOAD_SIZE" in df[1]:
                constants.append(elementsDefine[1])
                commonDefines.write("    %s\n" % (df[0].rstrip('\n')))
                commonDefines.write("    public static final int %s = PLAYER_MAX_MESSAGE_SIZE; //%s;\n" % (elementsDefine[1],special[0]))
            
            # if the define not correspond with a proxy we add it to commonDefines
            if not esta and not elementsDefine[1] in constants:
                constants.append(elementsDefine[1])
                commonDefines.write("    %s\n" % (df[0].rstrip('\n')))
                if '"' in elementsDefine[-1]:
                        commonDefines.write("    public static final String %s = %s;\n" % (elementsDefine[1], string[0]))
                else:
                        commonDefines.write("    public static final int %s = %s;\n" % (elementsDefine[1], elementsDefine[-1]))
                
        for d in defines:
            # pattern to find the final string in the defines
            pattern = re.compile('\".*\"',re.MULTILINE)
            string = pattern.findall(d)
            df = d.split(' ')
            df = removeBlanks(df)
            
            esta = False
            for p in proxy:
                if p.lower() != 'player':
                    if p.lower() in df[1].lower():
                        esta = True 
                        
            # It corresponds to a client define
            if "PLAYER_PLAYER" in df[1]:
                esta = True             
            
                    
            # if the define not correspond with a proxy we add it to commonDefines
            if not esta and not df[1] in constants:
                constants.append(df[1])
                if '"' in df[-1]:
                            commonDefines.write("    public static final String %s = %s;\n" % (df[1], string[0]))
                else:
                            commonDefines.write("    public static final int %s = %s;\n" % (df[1], df[-1]))
                            
    commonDefines.write("}\n")
    commonDefines.close()
        
    
    for p in proxy:
        if p.lower() == 'player': #The proxies named "player" are changed to "client"
            p = 'client'
            
        filename = "Abstract"+p[0].upper()+p[1:]+".java"
        if p[0].upper()+p[1:] != 'Client':
            abstract.append("Abstract"+p[0].upper()+p[1:])
        if dir:
            if not os.path.isdir(DIR + PATH):
                os.makedirs(DIR + PATH)
            sourcefile = open(DIR + PATH + filename, 'w+')
        else:
            if not os.path.isdir("./" + PATH):
                os.makedirs("./" + PATH)
            sourcefile = open("./" + PATH + filename, 'w+')
        #sourcefile = open("../player/generated/abstractproxy/"+filename,'w+')
        
        sourcefile.write(LICENSE)
        sourcefile.write("package es.uji.robot.player.generated.abstractproxy;\n\n")
        sourcefile.write("import es.uji.robot.player.generated.xdr.*;\n")
        sourcefile.write("import es.uji.robot.player.proxy.*;\n")
        sourcefile.write("import es.uji.robot.xdr.XDRObject;\n\n\n")
        if p == 'client':
            sourcefile.write("import java.util.Hashtable;\n") 
        sourcefile.write("public abstract class Abstract" + p[0].upper()+p[1:] +" extends Device{\n")
        
        # Array with all the constant declarations of each proxy 
        constantsAdded = []
        
        # constant declaration zone with comments
        
        for d in definesWithComment:
            df = d.split('#')
            # pattern to find the final string in the defines
            pattern = re.compile('\".*\"',re.MULTILINE)
            string = pattern.findall(df[1])
            
            elementsDefine = df[1].split(' ')
            elementsDefine = removeBlanks(elementsDefine)
            # Find out the definitions for each proxy and write them on the appropriate file.
            # For each constant definition ensure that is not already defined
            if p == 'client':
                if "PLAYER_PLAYER" in df[1] and (not elementsDefine[1] in constantsAdded):
                    constantsAdded.append(elementsDefine[1])
                    sourcefile.write("    %s\n" % (df[0].rstrip('\n')))
                    if '"' in elementsDefine[-1]:
                        sourcefile.write("    protected static final String %s = %s;\n" % (elementsDefine[1], string[0]))
                    else:    
                        sourcefile.write("    protected static final int %s = %s;\n" % (elementsDefine[1], elementsDefine[-1]))
            else:   
                if "_" + p.upper() + "_" in df[1] and (not elementsDefine[1] in constantsAdded):
                    constantsAdded.append(elementsDefine[1])
                    sourcefile.write("    %s\n" % (df[0].rstrip('\n')))
                    if "PLAYER_AUDIO_COMMAND_BUFFER_SIZE" in df[1]:
                        sourcefile.write("    protected static final int PLAYER_AUDIO_COMMAND_BUFFER_SIZE = 3;\n")
                    
                    elif "PLAYER_CAMERA_IMAGE_SIZE" in df[1]:
                        sourcefile.write("    protected static final int PLAYER_CAMERA_IMAGE_SIZE = PLAYER_CAMERA_IMAGE_WIDTH * PLAYER_CAMERA_IMAGE_HEIGHT * 4;\n")
                        
                    elif "PLAYER_MAP_MAX_TILE_SIZE" in df[1]:
                        sourcefile.write("    protected static final int PLAYER_MAP_MAX_TILE_SIZE = ((PLAYER_MAX_PAYLOAD_SIZE - 12) / 1.001) - 20 - 1;\n")
                        
                    elif "MCOM_COMMAND_BUFFER_SIZE" in df[1]:
                        sourcefile.write("    /*protected static final int MCOM_COMMAND_BUFFER_SIZE = sizeof(player_mcom_config_t);*/\n")
                    
                    else:    
                        if '"' in elementsDefine[-1]:
                            sourcefile.write("    protected static final String %s = %s;\n" % (elementsDefine[1], string[0]))
                        else:
                            sourcefile.write("    protected static final int %s = %s;\n" % (elementsDefine[1], elementsDefine[-1]))
                        
        #define extracted from playerc.h
        for d in otherDefinesWithComment:
            df = d.split('#')
            # pattern to find the final string in the defines
            pattern = re.compile('\".*\"',re.MULTILINE)
            string = pattern.findall(df[1])
            
            elementsDefine = df[1].split(' ')
            elementsDefine = removeBlanks(elementsDefine)
            # Find out the definitions for each proxy and write them on the appropriate file.
            # For each constant definition ensure that is not already defined
            if p == 'client':
                if "PLAYER_PLAYER" in df[1] and (not elementsDefine[1] in constantsAdded):
                    constantsAdded.append(elementsDefine[1])
                    sourcefile.write("    %s\n" % (df[0].rstrip('\n')))
                    if '"' in elementsDefine[-1]:
                        sourcefile.write("    protected static final String %s = %s;\n" % (elementsDefine[1], string[0]))
                    else:
                        sourcefile.write("    protected static final int %s = %s;\n" % (elementsDefine[1], elementsDefine[-1]))
            else:
                if "_" + p.upper() + "_" in df[1] and (not elementsDefine[1] in constantsAdded):
                    constantsAdded.append(elementsDefine[1])
                    sourcefile.write("    %s\n" % (df[0].rstrip('\n')))
                    if '"' in elementsDefine[-1]:
                        sourcefile.write("    protected static final String %s = %s;\n" % (elementsDefine[1], string[0]))
                    else:
                        sourcefile.write("    protected static final int %s = %s;\n" % (elementsDefine[1], elementsDefine[-1]))
                    
                
        # constant declaration zone without comments        
        
        for d in defines:
                # pattern to find the final string in the defines
                pattern = re.compile('\".*\"',re.MULTILINE)
                string = pattern.findall(d)
                df = d.split(' ')
                df = removeBlanks(df)
                if p == 'client':
                    if "PLAYER_PLAYER" in df[1] and (not df[1] in constantsAdded):
                        constantsAdded.append(df[1])
                        if '"' in df[-1]:
                            sourcefile.write("    protected static final String %s = %s;\n" % (df[1], string[0]))
                        else:
                            sourcefile.write("    protected static final int %s = %s;\n" % (df[1], df[-1]))
                else:                        
                    if "_" + p.upper() + "_" in df[1] and (not df[1] in constantsAdded):
                        constantsAdded.append(df[1])
                        if "PLAYER_AUDIO_COMMAND_BUFFER_SIZE" in df[1]:
                            sourcefile.write("    protected static final int PLAYER_AUDIO_COMMAND_BUFFER_SIZE = 3;\n")
                    
                        elif "PLAYER_CAMERA_IMAGE_SIZE" in df[1]:
                            sourcefile.write("    protected static final int PLAYER_CAMERA_IMAGE_SIZE = PLAYER_CAMERA_IMAGE_WIDTH * PLAYER_CAMERA_IMAGE_HEIGHT * 4;\n")
                            
                        elif "PLAYER_MAP_MAX_TILE_SIZE" in df[1]:
                            sourcefile.write("    protected static final int PLAYER_MAP_MAX_TILE_SIZE = (((int)((PLAYER_MAX_PAYLOAD_SIZE-12)/1.001)) - 20 - 1);\n")
                            
                        elif "MCOM_COMMAND_BUFFER_SIZE" in df[1]:
                            sourcefile.write("    /*protected static final int MCOM_COMMAND_BUFFER_SIZE = sizeof(player_mcom_config_t);*/\n")
                        
                        else:    
                            if '"' in df[-1]:
                                sourcefile.write("    protected static final String %s = %s;\n" % (df[1], string[0]))
                            else:
                                sourcefile.write("    protected static final int %s = %s;\n" % (df[1], df[-1]))
                            
        #define extracted from playerc.h
        for d in otherDefines:
                # pattern to find the final string in the defines
                pattern = re.compile('\".*\"',re.MULTILINE)
                string = pattern.findall(d)
                df = d.split(' ')
                df = removeBlanks(df)
                if p == 'client':
                    if "PLAYER_PLAYER" in df[1] and (not df[1] in constantsAdded):
                        constantsAdded.append(df[1])
                        if '"' in df[-1]:
                            sourcefile.write("    protected static final String %s = %s;\n" % (df[1], string[0]))
                        else:
                            sourcefile.write("    protected static final int %s = %s;\n" % (df[1], df[-1]))
                else:   
                    if "_" + p.upper() + "_" in df[1] and (not df[1] in constantsAdded):
                        constantsAdded.append(df[1])
                        if '"' in df[-1]:
                            sourcefile.write("    protected static final String %s = %s;\n" % (df[1], string[0]))
                        else:
                            sourcefile.write("    protected static final int %s = %s;\n" % (df[1], df[-1]))
                        
        
        # Client file is treated different because definitions are extracted in a different way from player_interfaces        
        
        if p == 'client':
            dentro = False
            lines = interfaces.split('\n')
            for l in lines:
                if dentro:
                    if "@ingroup" in l:
                        dentro = False
                        #print l
                    if re.match('^#define\s+\w*\s+\d+', l):
                        list = removeBlanks(l.split(' '))
                        if not list[1] in constantsAdded:
                            constantsAdded.append(list[1])
                            sourcefile.write("    protected static final int %s = %s;\n" % (list[1], list[-1]))
                if "@ingroup interface_player" in l:
                    #print l
                    dentro = True
                
                    
                    
        # Begin of the static part
        sourcefile.write("    static{\n")
        
        if p == 'client':
            sourcefile.write("        xdrObjectTable = new Hashtable<MyKey, XDRObject>();\n")
            sourcefile.write("        ProxiesInitialiser.initialise();\n")
            #universalElements are the elements defined in functiontable.c and they are added to client file
            for elem in universalElements:
                e = elem.split(',')
                #print e
                if not '0' in e[1]:
                    code = e[0].lstrip().rstrip()[1:]
                    type = e[1].lstrip().rstrip()
                    subtype = e[2].lstrip().rstrip()
                    xdrobject = goodName(e[3].lstrip().rstrip()[18:-5])
                
                    sourcefile.write("        Client.registerXDRObject((short)%s, (char)%s, (char)%s, (XDRObject)new %s());\n" % (code, type, subtype, xdrobject))
             
            # In funciontable_gen.c file there are also some elements to add to AbstractClient       
            for elem in elements:
                e = elem.split(',')
                code = e[0].lstrip().rstrip()[1:]
                type = e[1].lstrip().rstrip()
                subtype = e[2].lstrip().rstrip()
                xdrobject = goodName(e[3].lstrip().rstrip()[18:-5])
                
                if "PLAYER_PLAYER_" in code:
                    sourcefile.write("        Client.registerXDRObject((short)%s, (char)%s, (char)%s, (XDRObject)new %s());\n" % (code, type, subtype, xdrobject))
                
        # Once the universal elements have been added, we have to add the normal elements    
        for elem in elements:
            e = elem.split(',')
            code = e[0].lstrip().rstrip()[1:]
            type = e[1].lstrip().rstrip()
            subtype = e[2].lstrip().rstrip()
            #xdrobject = goodName(e[4].lstrip().rstrip()[18:-7])
            xdrobject = goodName(e[3].lstrip().rstrip()[18:-5])
            
            if "_" + p.upper() + "_" in code:
                sourcefile.write("        Client.registerXDRObject((short)%s, (char)%s, (char)%s, (XDRObject)new %s());\n" % (code, type, subtype, xdrobject))
    
        sourcefile.write("    }\n")
        
        if p == "client":
            sourcefile.write("    protected static class MyKey {\n")
            sourcefile.write("        short iface;\n")
            sourcefile.write("        char type;\n")
            sourcefile.write("        char subtype;\n")
            sourcefile.write("        public MyKey(short iface, char type, char subtype) {\n")
            sourcefile.write("            super();\n")
            sourcefile.write("            this.iface = iface;\n")
            sourcefile.write("            this.type = type;\n")
            sourcefile.write("            this.subtype = subtype;\n")
            sourcefile.write("        }\n")
            sourcefile.write("        public short getIface() {\n")
            sourcefile.write("            return iface;\n")
            sourcefile.write("        }\n")
            sourcefile.write("        public void setIface(short iface) {\n")
            sourcefile.write("            this.iface = iface;\n")
            sourcefile.write("        }\n")
            sourcefile.write("        public char getType() {\n")
            sourcefile.write("            return type;\n")
            sourcefile.write("        }\n")
            sourcefile.write("        public void setType(char type) {\n")
            sourcefile.write("            this.type = type;\n")
            sourcefile.write("        }\n")
            sourcefile.write("        public char getSubtype() {\n")
            sourcefile.write("            return subtype;\n")
            sourcefile.write("        }\n")
            sourcefile.write("        public void setSubtype(char subtype) {\n")
            sourcefile.write("            this.subtype = subtype;\n")
            sourcefile.write("        }\n")
        
            sourcefile.write("        @Override\n")
            sourcefile.write("        public int hashCode() {\n")
            sourcefile.write("            return Short.valueOf(iface).hashCode() +\n")
            sourcefile.write("                Character.valueOf(type).hashCode() +\n") 
            sourcefile.write("                Character.valueOf(subtype).hashCode();\n")
            sourcefile.write("        }\n")
            sourcefile.write("        @Override\n")
            sourcefile.write("        public boolean equals(Object obj) {\n")
            sourcefile.write("            if ( obj instanceof MyKey ) {\n")
            sourcefile.write("                MyKey k = ((MyKey)obj);\n")
            sourcefile.write("                return k.iface == iface && k.subtype == subtype && k.type == type;\n")
            sourcefile.write("            }\n")
            sourcefile.write("            return super.equals(obj);\n")
            sourcefile.write("        }\n")
            sourcefile.write("    }\n")
    
            sourcefile.write("    protected static Hashtable<MyKey, XDRObject> xdrObjectTable;\n")
    
            sourcefile.write("    public static void registerXDRObject(short iface, char type, char subtype, XDRObject xdrObject) {\n")
            sourcefile.write("        xdrObjectTable.put(new MyKey(iface,type,subtype), xdrObject);\n")
            sourcefile.write("    }\n")
            
        else:
            sourcefile.write("    /** Public static method to force static initialiser to be launched and so the XDRObjects registered in the Client structure */\n")
            sourcefile.write("    public static void initialise(){};\n")
        sourcefile.write("}\n")
                    
    sourcefile.close()    
    
    # Generate ProxiesInitialiser
    sourcefilename = "ProxiesInitialiser.java"
    if dir:
        if not os.path.isdir(DIR + PATH):
            os.makedirs(DIR + PATH)
        sourcefile = open(DIR + PATH + sourcefilename, 'w+')
    else:
        if not os.path.isdir("./" + PATH):
            os.makedirs("./" + PATH)
        sourcefile = open("./" + PATH + sourcefilename, 'w+')
    #sourcefile = open("../player/generated/abstractproxy/ProxiesInitialiser.java",'w+')
        
    sourcefile.write(LICENSE)
    sourcefile.write("package es.uji.robot.player.generated.abstractproxy;\n\n")
    sourcefile.write("public abstract class ProxiesInitialiser {\n\n")
    sourcefile.write("    public static void initialise(){\n")
    
    for a in abstract:
        sourcefile.write("        %s.initialise();\n" % (a))
        
    sourcefile.write("    };\n")
    sourcefile.write("}\n")
    

    
        
        