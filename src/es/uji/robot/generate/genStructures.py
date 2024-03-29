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

PATH = 'es/uji/robot/player/generated/structure/'

USAGE = 'USAGE: genStructures.py player_interfaces.h playerc.h <other player_interfaces.h files>\n <-D> <destination directory>'

command = ""
for i in xrange(len(sys.argv)):
    command += sys.argv[i] + " "

LICENSE = "/**\n* Copyright (C) 2009  Leo Nomdedeu, David Olmos\n*\n* ### Autogenerated file with command \n* " + command + "\n* Do not make changes to this file.\n*\n* This program is free software: you can redistribute it and/or modify\n* it under the terms of the GNU General Public License as published by\n* the Free Software Foundation, either version 3 of the License, or\n* any later version.\n*\n* This program is distributed in the hope that it will be useful,\n* but WITHOUT ANY WARRANTY; without even the implied warranty of\n* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n* GNU General Public License for more details.\n*\n* You should have received a copy of the GNU General Public License\n* along with this program.  If not, see <http://www.gnu.org/licenses/>.\n*\n*********************************************************************\n*\n* Authors: Leo Nomdedeu, David Olmos\n* Release: 0.1pre_alfa\n* Changelog:\n*\t\t0.1pre_alfa: Initial release\n*********************************************************************\n*/\n\n"

hasdynamic = []

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
                
# Test if the directories are created before generating the new files
# If the directory exists and is not empty, delete the content
if dir:
    if not os.path.isdir(DIR + PATH):
        os.makedirs(DIR + PATH)
    else:
        for fileInDir in os.listdir(DIR + PATH):
            filePath = os.path.join(DIR + PATH, fileInDir)
            try:
                if os.path.isfile(filePath):
                    os.unlink(filePath)
            except Exception, e:
                print e
else:
    if not os.path.isdir("./" + PATH):
        os.makedirs("./" + PATH)
    else:
        for fileInDir in os.listdir("./" + PATH):
            filePath = os.path.join("./" + PATH, fileInDir)
            try:
                if os.path.isfile(filePath):
                    os.unlink(filePath)
            except Exception, e:
                print e

# Stores information of arrays

class DataTypeMember:
    arraypattern = re.compile('\[(.*?)\]')
    pointerpattern = re.compile('\*')
    doublepointerpattern = re.compile('\*\*')
    
    def __init__(self, body, void):
        # declarations like void *
        if void:
            self.array = False
            self.pointer = False
            self.doublepointer = False
            self.arraysize = 0
            body = self.arraypattern.sub('', body)
            body = self.pointerpattern.sub('', body)
            body = self.doublepointerpattern.sub('',body)
            self.Name = body.strip()
            self.pointervar = ''
            self.countvar = ''
        else:
            # is it an array or a scalar?
            self.array = False
            self.pointer = False
            self.doublepointer = False
            self.arraysize = self.arraypattern.findall(body)
            doublepointers = self.doublepointerpattern.findall(body)
            body = self.doublepointerpattern.sub('',body)
            pointers = self.pointerpattern.findall(body)
            if len(self.arraysize) > 0:
                self.array = True
                self.arraysize = self.arraysize[0]
                body = self.arraypattern.sub('', body)
                
            elif len(pointers) > 1:  # This checks for things like "uint8_t* *data"
                raise 'Illegal pointer declaration in struct\n' + body
            elif len(pointers) > 0:
                self.array = True
                self.arraysize = ''
                self.pointer = True
                body = self.pointerpattern.sub('', body)
            elif len(doublepointers) > 0:
                self.array = True
                self.doublepointer = True
                self.arraysize = ''
    
            self.Name = body.strip()
            self.pointervar = self.Name + '_p'
            self.countvar = self.Name + '_count'
  
# multiple variables can occur with single type, i.e. int a,b,c so we first get the type and then add each variable to the entry
class DataTypeMemberSet:
      typepattern = re.compile('\s*\w+')
      variablepattern = re.compile('\s*([^,;]+?)\s*[,;]')
      
      def __init__(self, body):
        self.haspointer = False
        self.hasarray = False
        self.void = False
        self.variables = []
        self.typename = self.typepattern.findall(body)[0].strip()
        if self.typename == 'void':
            self.void = True
        if self.typename in hasdynamic:
            self.dynamic = True
        else:
            self.dynamic = False
        
        # find the variable names
        body = self.typepattern.sub('', body, 1)
        vars = self.variablepattern.findall(body)
        
        
        # iterate through each variable
        for varstring in vars:
            member = DataTypeMember(varstring, self.void)
            self.variables.append(member)
            if member.pointer:
                self.haspointer = True
            if member.array:
                self.hasarray = True
          

class DataType:
    contentspattern = re.compile('.*\{\s*(.*?)\s*\}', re.MULTILINE | re.DOTALL)
    declpattern = re.compile('\s*([^;]*?;)', re.MULTILINE)
  
    def __init__(self, body):
        split = string.split(body)
        self.prefix = split[-1][:-2]
        self.typename = split[-1]
        self.dynamic = False
        self.hasarray = False
        self.haspointer = False

        self.members = []
        # pick out the contents of the struct
        varpart = self.contentspattern.findall(body)
        if len(varpart) != 1:
            print 'skipping nested / empty struct ' + typename
            raise "Empty Struct"
        # separate the variable declarations
        decls = self.declpattern.findall(varpart[0])
        for dstring in decls:
              ms = DataTypeMemberSet(dstring)
              self.members.append(ms)
              if ms.haspointer or ms.dynamic:
                  self.dynamic = True
              if ms.hasarray:
                  self.hasarray = True
              if ms.haspointer:
                  self.haspointer = True
          
        if self.dynamic:
            hasdynamic.append (self.typename)
            
    def GetVarNames(self):
        varnames = []
        for m in self.members:
          for v in m.variables:
            varnames.append(v.Name)
        return varnames
    
    def HasDynamicArray(self):
        for m in self.members:
            if m.dynamic:
                for v in m.variables:
                    if v.array:
                        return True
        return False


class MethodGenerator:
      def __init__(self,sourcefile):
          self.sourcefile = sourcefile
          
          
      def genJava(self,datatype):
          self.code = ""
          self.hasArray = False
          self.hasBaos = False
          
          self.code += "package es.uji.robot.player.generated.structure;\n"
          self.code += "\n"
          self.code += "ARRAYLIST"
          self.code += "BAOS"
          self.code += "\n"
          self.code += "import es.uji.robot.player.proxy.*;\n"
          self.code += "import es.uji.robot.player.generated.xdr.*;\n\n"
          self.code += "public class %s{\n" % (goodName(datatype.prefix))
          
          for member in datatype.members:    
              for var in member.variables:
                  # Declarations like void * are transformed to ByteArrayOutputStream
                  if member.void:
                      self.hasBaos = True
                      self.code += "    private ByteArrayOutputStream %s = new ByteArrayOutputStream();\n" % (goodNameVar(var.Name))
                  else:        
                      primitive = True # Primitive java type
                      # Do some name mangling for common types
                      
                      # Special management for some variable names
                      if var.Name == "name" and member.typename.find('int8') >= 0:
                          member.typename = 'char'
                      if var.Name == "data" and member.typename.find('char') >= 0:
                          member.typename = 'uint8_t'
                      
                      if member.typename == 'long long':
                          typename = 'long'
                      elif member.typename == 'int64_t':
                          typename = 'long'
                      elif member.typename == 'uint64_t':
                          typename = 'long'
                      elif member.typename == 'int32_t':
                          typename = 'int'
                      elif member.typename == 'uint32_t':
                          typename = 'int'
                      elif member.typename == 'int16_t':
                          typename = 'short'
                      elif member.typename == 'uint16_t':
                          typename = 'short'
                      elif member.typename == 'char':
                          typename = 'char'
                      elif member.typename == 'int8_t':
                          typename = 'byte'
                      elif member.typename == 'uint8_t':
                          #typename = 'char'
                          typename = 'byte'
                      elif member.typename == 'bool_t':
                          typename = 'boolean'
                      elif member.typename == 'double':
                          typename = 'double'
                      elif member.typename == 'int':
                          typename = 'int'
                      elif member.typename == 'float':
                          typename = 'float'
                      elif member.typename == 'long':
                          typename = 'long'
                      elif member.typename == 'boolean':
                          typename = 'boolean'
                      elif member.typename == 'short':
                          typename = 'short'
                      else:
                           # rely on a previous declaration of an xdr_ proc for this type
                          typename = member.typename
                          primitive = False
                          
                      first = typename[0]
                      if var.array:
                          if primitive:
                              if var.doublepointer: # Things like char ** words
                                  if typename == 'char':
                                      self.hasBaos = True
                                      self.code += "    private ByteArrayOutputStream %s = new ByteArrayOutputStream();\n" % (goodNameVar(var.Name))
                                  elif typename == 'int':
                                      self.hasArray = True
                                      self.code += "    private List<ArrayList<Integer>> %s = new ArrayList<ArrayList<Integer>>();\n" % (goodNameVar(var.Name))
                                  else:
                                      self.hasArray = True
                                      self.code += "    private List<ArrayList<%s>> %s = new ArrayList<ArrayList<%s>>();\n" % (goodName(typename), goodNameVar(var.Name), goodName(typename))
                              else:
                                  if typename == 'byte':
                                      self.hasBaos = True
                                      self.code += "    private ByteArrayOutputStream %s = new ByteArrayOutputStream();\n" % (goodNameVar(var.Name))
                                  elif typename == 'char':
                                      self.hasBaos = True
                                      self.code += "    private String %s = new String();\n" % (goodNameVar(var.Name))
                                  elif typename == 'int':
                                      self.hasArray = True
                                      self.code += "    private List<%s> %s = new ArrayList<%s>();\n" % ("Integer", goodNameVar(var.Name), "Integer")
                                  else:
                                      self.hasArray = True
                                      self.code += "    private List<%s> %s = new ArrayList<%s>();\n" % (first.upper()+typename[1:], goodNameVar(var.Name), first.upper()+typename[1:])
                          else:
                              if var.doublepointer:
                                  self.hasArray = True
                                  self.code += "    private List<ArrayList<%s>> %s = new ArrayList<ArrayList<%s>>();\n" % (goodName(typename[:-2]), goodNameVar(var.Name), goodName(typename[:-2]))
                              else:
                                  self.hasArray = True
                                  self.code += "    private List<%s> %s = new ArrayList<%s>();\n" % (goodName(typename[:-2]), goodNameVar(var.Name), goodName(typename[:-2]))
                      else:
                          # "count" variables are not added to java objects because they number of elements of an array, and this can be controlled with the arraylist class in java
                          if "count" not in var.Name: 
                              if primitive:
                                  self.code += "    private %s %s;\n" % (typename,goodNameVar(var.Name))
                              else:
                                  self.code += "    private %s %s;\n" % (goodName(typename[:-2]),goodNameVar(var.Name))
          self.code += "\n"
          return (self.code,self.hasArray, self.hasBaos)
          
      def genConstructorCopy(self, datatype):
          self.hasArray = False
          self.hasBaos = False
          self.code = ""
          
          self.code +="    public %s(%s src) {\n" % (goodName(datatype.prefix), goodName(datatype.prefix))
          
          for member in datatype.members:
              for var in member.variables:
                  # "count" variables are not added to java objects because they number of elements of an array, and this can be controlled with the arraylist class in java
                  # also we test if the file is not in the array of diffFiles (files that are treated different)
                  if "count" not in var.Name:
                      self.code +="        this.%s = src.%s;\n" % (goodNameVar(var.Name), goodNameVar(var.Name))
                      
          self.code +="    }\n"
          return (self.code,self.hasArray, self.hasBaos)
      
      
      def genConstructor(self,datatype):
          self.hasArray = False
          self.hasBaos = False
          self.code = ""
          
          self.code +="    public %s(){\n" % (goodName(datatype.prefix))
          
          for member in datatype.members:            
              for var in member.variables:
                  primitive = True #Primitive java types
                  # Do some name mangling for common types
                  if member.typename == 'long long':
                      typename = 'long'
                  elif member.typename == 'int64_t':
                      typename = 'long'
                  elif member.typename == 'uint64_t':
                      typename = 'long'
                  elif member.typename == 'int32_t':
                      typename = 'int'
                  elif member.typename == 'uint32_t':
                      typename = 'int'
                  elif member.typename == 'int16_t':
                      typename = 'short'
                  elif member.typename == 'uint16_t':
                      typename = 'short'
                  elif member.typename == 'char':
                      typename = 'char'
                  elif member.typename == 'int8_t':
                          typename = 'byte'    
                  elif member.typename == 'uint8_t':
                      typename = 'char'
                  elif member.typename == 'bool_t':
                      typename = 'boolean'
                  elif member.typename == 'double':
                      typename = 'double'
                  elif member.typename == 'int':
                      typename = 'int'
                  elif member.typename == 'float':
                      typename = 'float'
                  elif member.typename == 'long':
                      typename = 'long'
                  elif member.typename == 'boolean':
                      typename = 'boolean'
                  elif member.typename == 'short':
                      typename = 'short'
                  else:
                      # rely on a previous declaration of an xdr_ proc for this type
                      typename = member.typename
                      primitive = False
                      
                  if not var.array:
                      # "count" variables are not added to java objects because they number of elements of an array, and this can be controlled with the arraylist class in java
                      # also we test if the file is not in the array of diffFiles (files that are treated different)
                      if "count" not in var.Name:
                          if typename == 'int':
                              self.code +="        this.%s = 0;\n" % (goodNameVar(var.Name))
                          elif typename == 'float':
                              self.code +="        this.%s = (float)0.0;\n" % (goodNameVar(var.Name))
                          elif typename == 'long':
                              self.code +="        this.%s = 0l;\n" % (goodNameVar(var.Name))
                          elif typename == 'double':
                              self.code +="        this.%s = 0.0;\n" % (goodNameVar(var.Name))
                          elif typename == 'short':
                              self.code +="        this.%s = (short)0;\n" % (goodNameVar(var.Name))
                          elif typename == 'boolean':
                              self.code +="        this.%s = true;\n" % (goodNameVar(var.Name))
                          elif typename == 'byte':
                              self.code +="        this.%s = (byte)0;\n" % (goodNameVar(var.Name))
                          elif typename == 'char':
                              self.code +="        this.%s = 'c';\n" % (goodNameVar(var.Name))
                                        
          self.code +="    }\n"
          return (self.code,self.hasArray, self.hasBaos)
      
      
      def genSetterGetter(self,datatype):
          self.code = ""
          self.hasArray = False
          self.hasBaos = False
          
          for member in datatype.members:
              for var in member.variables:
                  # Declarations like void * are treated first.
                  if member.void:
                      self.hasBaos = True
                      self.code += "    public ByteArrayOutputStream get%s(){\n" % (goodName(var.Name))
                      self.code += "        return this.%s;\n" % (goodNameVar(var.Name))
                      self.code += "    }\n\n"
                      
                      self.code += "    public void set%s(ByteArrayOutputStream %s){\n" % (goodName(var.Name), goodNameVar(var.Name))
                      self.code += "        this.%s = %s;\n" % (goodNameVar(var.Name), goodNameVar(var.Name))
                      self.code += "    }\n\n"
                      
                  else:
                      primitive = True # Primitive java type
                      # Do some name mangling for common types
                      
                      # Special management for some variable names
                      if var.Name == "name" and member.typename.find('int8') >= 0:
                          member.typename = 'char'
                      if var.Name == "data" and member.typename.find('char') >= 0:
                          member.typename = 'uint8_t'
                      
                      if member.typename == 'long long':
                          typename = 'long'
                      elif member.typename == 'int64_t':
                          typename = 'long'
                      elif member.typename == 'uint64_t':
                          typename = 'long'
                      elif member.typename == 'int32_t':
                          typename = 'int'
                      elif member.typename == 'uint32_t':
                          typename = 'int'
                      elif member.typename == 'int16_t':
                          typename = 'short'
                      elif member.typename == 'uint16_t':
                          typename = 'short'
                      elif member.typename == 'char':
                          typename = 'char'
                      elif member.typename == 'int8_t':
                          typename = 'byte'
                      elif member.typename == 'uint8_t':
                          #typename = 'char'
                          typename = 'byte'
                      elif member.typename == 'bool_t':
                          typename = 'boolean'
                      elif member.typename == 'double':
                          typename = 'double'
                      elif member.typename == 'int':
                          typename = 'int'
                      elif member.typename == 'float':
                          typename = 'float'
                      elif member.typename == 'long':
                          typename = 'long'
                      elif member.typename == 'boolean':
                          typename = 'boolean'
                      elif member.typename == 'short':
                          typename = 'short'
                      else:
                          # rely on a previous declaration of an xdr_ proc for this type
                          typename = member.typename
                          primitive = False
                    
                      first = var.Name[0]
                      firstTypename = typename[0]
                      # Booleans are treated different because the have different getter function names
                      if member.typename == 'Boolean':
                          if var.array:    
                              if primitive:
                                  if var.doublepointer:
                                      self.hasArray = True
                                      self.code += "    public List<ArrayList<Boolean>> %s(){\n" % ("is"+first.upper()+goodNameVar(var.Name[1:]))
                                  else:
                                      self.hasArray = True
                                      self.code += "    public List<%s> %s(){\n" % (firstTypename.upper()+typename[1:],"is"+first.upper()+goodNameVar(var.Name[1:]))
                              else:
                                  if var.doublepointer:
                                      self.hasArray = True
                                      self.code += "    public List<ArrayList<%s>> %s(){\n" % (goodName(typename[:-2]),"is"+first.upper()+goodNameVar(var.Name[1:]))
                                  else:
                                      self.hasArray = True
                                      self.code += "    public List<%s> %s(){\n" % (goodName(typename[:-2]),"is"+first.upper()+goodNameVar(var.Name[1:]))                    
                          else:
                              if primitive:
                                  self.code += "    public %s %s(){\n" % (typename,"is"+first.upper()+goodNameVar(var.Name[1:]))
                              else:
                                  self.code += "    public %s %s(){\n" % (goodName(typename),"is"+first.upper()+goodNameVar(var.Name[1:-2]))
                                  
                      #First we generate self.code for getters
                      else:
                          if var.array:
                              if var.doublepointer:
                                  if typename == 'char':
                                      self.hasBaos = True
                                      self.code += "    public ByteArrayOutputStream %s(){\n" % ("get"+first.upper()+goodNameVar(var.Name[1:]))
                                  elif typename == 'int':
                                      self.hasArray = True
                                      self.code += "    public List<ArrayList<Integer>> %s(){\n" % ("get"+first.upper()+goodNameVar(var.Name[1:]))
                                  else:
                                      if primitive:
                                          self.hasArray = True
                                          self.code += "    public List<ArrayList<%s>> %s(){\n" % firstTypename.upper()+typename[1:],"get"+first.upper()+goodNameVar(var.Name[1:])
                                      else:
                                          self.hasArray = True
                                          self.code += "    public List<ArrayList<%s>> %s(){\n" % (goodName(typename[:-2]),"get"+first.upper()+goodNameVar(var.Name[1:]))
                              else:
                                  
                                  if typename == 'char':
                                      self.hasBaos = True
                                      self.code += "    public String %s(){\n" % ("get"+first.upper()+goodNameVar(var.Name[1:])) 
                                  elif typename == 'byte':
                                      self.hasBaos = True
                                      self.code += "    public ByteArrayOutputStream %s(){\n" % ("get"+first.upper()+goodNameVar(var.Name[1:]))    
                                  elif typename == 'int':
                                      self.hasArray = True
                                      self.code += "    public List<%s> %s(){\n" % ("Integer","get"+first.upper()+goodNameVar(var.Name[1:]))    
                                  else:
                                      if primitive:
                                          self.hasArray = True
                                          self.code += "    public List<%s> %s(){\n" % (firstTypename.upper()+typename[1:],"get"+first.upper()+goodNameVar(var.Name[1:]))
                                      else:
                                          self.hasArray = True
                                          self.code += "    public List<%s> %s(){\n" % (goodName(typename[:-2]),"get"+first.upper()+goodNameVar(var.Name[1:]))
                          else:
                              # "count" variables are not added to java objects because they number of elements of an array, and this can be controlled with the arraylist class in java
                              if "count" not in var.Name:
                                  if primitive:
                                      self.code += "    public %s %s(){\n" % (typename,"get"+first.upper()+goodNameVar(var.Name[1:]))
                                  else:
                                      self.code += "    public %s %s(){\n" % (goodName(typename[:-2]),"get"+first.upper()+goodNameVar(var.Name[1:]))
                      # "count" variables are not added to java objects because they number of elements of an array, and this can be controlled with the arraylist class in java
                      if "count" not in var.Name:
                          self.code += "        return this.%s;\n" % (goodNameVar(var.Name))
                          self.code += "    }\n\n"
                          
                      # Then we have to generate code for the setters
                      if var.array:
                          if var.doublepointer:
                              if typename == 'char':
                                  self.hasBaos = True
                                  self.code += "    public void %s(ByteArrayOutputStream %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),goodNameVar(var.Name))
                              elif typename == 'int':
                                  self.hasArray = True
                                  self.code += "    public void %s(ArrayList<ArrayList<Integer>> %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),"Integer",goodNameVar(var.Name))
                              else:
                                  if primitive:
                                      self.hasArray = True
                                      self.code += "    public void %s(ArrayList<ArrayList<%s>> %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),firstTypename.upper()+typename[1:],goodNameVar(var.Name))
                                  else:
                                      self.hasArray = True
                                      self.code += "    public void %s(ArrayList<ArrayList<%s>> %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),goodName(typename[:-2]),goodNameVar(var.Name))
                          else:
                              if typename == 'char':
                                  self.hasBaos = True
                                  self.code += "    public void %s(String %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),goodNameVar(var.Name))
                              elif typename == 'byte':
                                  self.hasBaos = True
                                  self.code += "    public void %s(ByteArrayOutputStream %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),goodNameVar(var.Name))     
                              elif typename == 'int':
                                  self.hasArray = True
                                  self.code += "    public void %s(ArrayList<%s> %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),"Integer",goodNameVar(var.Name))    
                              else:
                                  if primitive:
                                      self.hasArray = True
                                      self.code += "    public void %s(ArrayList<%s> %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),firstTypename.upper()+typename[1:],goodNameVar(var.Name))
                                  else:
                                      self.hasArray = True
                                      self.code += "    public void %s(ArrayList<%s> %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),goodName(typename[:-2]),goodNameVar(var.Name))
                      else:
                          # "count" variables are not added to java objects because they number of elements of an array, and this can be controlled with the arraylist class in java
                          if "count" not in var.Name:
                              if primitive:
                                  self.code += "    public void %s(%s %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),typename,goodNameVar(var.Name))
                              else:
                                  self.code += "    public void %s(%s %s){\n" % ("set"+first.upper()+goodNameVar(var.Name[1:]),goodName(typename[:-2]),goodNameVar(var.Name))
                      # "count" variables are not added to java objects because they number of elements of an array, and this can be controlled with the arraylist class in java
                      if "count" not in var.Name:
                          self.code += "        this.%s = %s;\n" % (goodNameVar(var.Name),goodNameVar(var.Name))
                          self.code += "    }\n\n"
                          
          return (self.code,self.hasArray, self.hasBaos)
          
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
            
    # Change Playerc to Player
    if "Playerc" in res:
        res = res.replace("Playerc","Player", 1)
        
    if res == "PlayerDevice":
        return "Device"
    elif res == "PlayerClient":
        return "Client"
        
    return res


# Method used to change the variables name to a well formed java version
def goodNameVar(name):
    res = ''
    cambiar = False
    for c in name:
        if cambiar:
            res = res + c.upper()
            cambiar = False
        elif c != '_':
            res = res + c
        elif c == '_':
            cambiar = True
            
    # Change Playerc to Player
    if "Playerc" in res:
        res = res.replace("Playerc","Player", 1)        
            
    return res

# Method used to remove blancks from an array
def removeBlanks(vector):
    res = []
    for e in vector:
        if e != '':
            res.append(e)
            
    return res


if __name__ == '__main__':
    if len(sys.argv) < 3:
        print USAGE
        sys.exit(-1)
        
    infilenames = [sys.argv[1],sys.argv[2]]    
    
    if len(sys.argv) > 3:
        for opt in sys.argv[3:]:
            infilenames.append(opt)
            
    # Separate input files, for processing them separately
    interfaces = ""
    structures = ""
    for f in infilenames:
        if "player_interfaces" in f:
            infile = open(f, 'r')
            interfaces += infile.read()
            infile.close()
        elif "playerc.h" in f:
            infile = open(f, 'r')
            structures += infile.read()
            infile.close()
        elif "player.h" in f:
            infile = open(f, 'r')
            interfaces += infile.read()
            infile.close()

    # strip blank lines
    pattern = re.compile('^\s*?\n', re.MULTILINE)
    interfaces = pattern.sub('', interfaces)
    structures = pattern.sub('', structures)
    
    # ingroup declarations to find proxy
    pattern = re.compile('\s+\@defgroup\s+interface\_[\w\s]+', re.MULTILINE)
    found = pattern.findall(interfaces)
    
    # strip C++-style comments
    pattern = re.compile('//.*')
    structures = pattern.sub('', structures)

    # strip C-style comments
    pattern = re.compile('/\*.*?\*/', re.MULTILINE | re.DOTALL)
    structures = pattern.sub('', structures)
    
    # find structs
    pattern = re.compile('typedef\s+struct\s+{[^}]+\}[^;]+', re.MULTILINE)
    structs = pattern.findall(structures)  

    # Create an array with all the proxies, removing irrelevant characters from the name
    proxy = []
    for f in found:
        proxy.append(f.rstrip().lstrip().split(' ')[-1])
    
    # Create an array with the struct names from playerc.h
    structNames = []
    for s in structs:
        structNames.append(s.split('}')[-1].lstrip().strip()[8:-2])
    
    
    # Generate two sets with proxy data and structNames data
    # We use this two sets to find out the structNames that do not correspon with proxies, and we store them in validElements array
    proxySet = set(proxy)
    structNamesSet = set(structNames)
    
    validElements = structNamesSet - proxySet
    
    #delete from the list mclient, because it is treated different
    validElements.remove('mclient') 
    
    # Once we now the valid elements from playerc.h, we store the valid structs in validStructs array
    validStructs = []
    for s in structs:
        if s.split('}')[-1].lstrip().rstrip()[8:-2] in validElements:
            validStructs.append(s)
    
    # For each valid struct we create the suitable file
    for s in validStructs:
        current = DataType(s)
        #print current.prefix
        sourcefilename = goodName(current.prefix) + ".java"
        if dir:
            if not os.path.isdir(DIR + PATH):
                os.makedirs(DIR + PATH)
            sourcefile = open(DIR + PATH + sourcefilename, 'w+')
        else:
            if not os.path.isdir("./" + PATH):
                os.makedirs("./" + PATH)
            sourcefile = open("./" + PATH + sourcefilename, 'w+')
        #sourcefile = open('../player/generated/structure/' + sourcefilename,'w+')
        
        sourcefile.write(LICENSE)
        
        hasArray = False
        hasBaos = False
        code = ""
        
        gen = MethodGenerator(sourcefile)
        
        # Call each of the methods that are going to generate the different methods of the structure files.
        # After calling each method test if it uses ByteArrayOutputStream or ArrayList, so we can include the suitable import at the beginning.
        res = gen.genJava(current)
        code += res[0]
        hasArray = hasArray or res[1]
        hasBaos = hasBaos or res[2]
    
        res = gen.genConstructor(current)
        code += res[0]
        hasArray = hasArray or res[1]
        hasBaos = hasBaos or res[2]
    
        res = gen.genConstructorCopy(current)
        code += res[0]
        hasArray = hasArray or res[1]
        hasBaos = hasBaos or res[2]
        
        res = gen.genSetterGetter(current)
        code += res[0]
        hasArray = hasArray or res[1]
        hasBaos = hasBaos or res[2]
    
        
        #Replace imports
        if hasArray:
          code = code.replace("ARRAYLIST", "import java.util.ArrayList;\nimport java.util.List;\n")
        else:
          code = code.replace("ARRAYLIST", "\n")
        if hasBaos:
          code = code.replace("BAOS", "import java.io.ByteArrayOutputStream;\n")
        else:
          code = code.replace("BAOS", "\n")
          
        #Write "code" string to the suitable file
        sourcefile.write(code)
    
        
        sourcefile.write("}\n")
        
    sourcefile.close()
    