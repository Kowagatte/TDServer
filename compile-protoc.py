import os
from sys import platform

proto_file="packets.proto"

protoc=".\\protoc"
src_dir = ".\\"
java_src_dir=".\\src\\main\\java"
kotlin_src_dir=".\\src\\main\\kotlin"

if platform == "darwin":
    protoc += "\\mac\\bin\\protoc"
elif platform == "win32":
    protoc += "\\windows\\bin\\protoc.exe"

os.system(protoc+" -I="+src_dir+" --java_out="+java_src_dir+" --kotlin_out="+kotlin_src_dir+" "+proto_file)
