#!/bin/bash
cd `dirname $0`

# 脚本目录
DIR_BIN=`pwd`
echo DIR_BIN=$DIR_BIN

JAVA_OPTS="-server -Xms64m -Xmx1024m"

java $JAVA_OPTS -jar code-generator-1.0.0.jar --config=config.json
