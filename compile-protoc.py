import os
from sys import platform

proto_file="packets.proto"

protoc=""
src_dir = ""
java_src_dir=""
kotlin_src_dir=""

def parse_OS(os):
    if os == "darwin":
        return ("/", "mac")
    if os == "linux":
        return ("/", "linux")
    if os == "win32":
        return ("\\", "windows")

def construct_paths(sep, os):
    global src_dir
    global protoc
    global java_src_dir
    global kotlin_src_dir
    src_dir = f'.{sep}'
    protoc = f'.{sep}protoc{sep}{os}{sep}bin{sep}protoc'
    java_src_dir = f'.{sep}src{sep}main{sep}java'
    kotlin_src_dir = f'.{sep}src{sep}main{sep}kotlin'

data = parse_OS(platform)
construct_paths(data[0], data[1])

#print(protoc+" -I="+src_dir+" --java_out="+java_src_dir+" --kotlin_out="+kotlin_src_dir+" "+proto_file)
os.system(protoc+" -I="+src_dir+" --java_out="+java_src_dir+" --kotlin_out="+kotlin_src_dir+" "+proto_file)
