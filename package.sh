#!/usr/bin/env bash

cp -a production/blog/blog out/artifacts/serveV2_prod/serveV2/classpath
cp -a production/diary/diary out/artifacts/serveV2_prod/serveV2/classpath
cp -a production/album/album out/artifacts/serveV2_prod/serveV2/classpath

echo "go to package a zip"

cd out/artifacts/serveV2_prod/
zip -r serveV2.zip serveV2
cp serveV2.zip ~/Downloads/
cd ../../../

echo "go to check the zip file in Download folder"