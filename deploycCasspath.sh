#!/usr/bin/env bash

cp -a production/controller/controller out/classpath
cp -a production/service/service out/classpath
cp -a production/service/auth out/classpath
cp -a production/repository/repository out/classpath
cp -a production/repository/data out/classpath

cp -a production/blog/blog out/classpath
cp -a production/diary/diary out/classpath
cp -a production/album/album out/classpath

echo "done"