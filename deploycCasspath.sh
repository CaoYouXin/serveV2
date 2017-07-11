#!/usr/bin/env bash

cp -a production/controller/controller out/classpath
cp -a production/service/service out/classpath
cp -a production/service/auth out/classpath
cp -a production/repository/repository out/classpath
cp -a production/repository/data out/classpath

echo "done"