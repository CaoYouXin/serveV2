#!/usr/bin/env bash

ng build --env=prod -base / \
   && cp -a ./dist/* ~/Dev/Git/personal/infinitely/html/server_management/docs/