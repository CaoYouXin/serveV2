#!/usr/bin/env bash

ng build --env=prod -base / \
   && cp -a ./dist/* ~/Dev/Git/personal/infinitely/html/server_management/docs/ \
   && cp ~/Dev/Git/personal/infinitely/html/server_management/docs/index.html ~/Dev/Git/personal/infinitely/html/server_management/docs/404.html
