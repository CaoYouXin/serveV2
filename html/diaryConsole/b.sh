#!/usr/bin/env bash

ng build --env=prod -base / \
   && cp -a ./dist/* ~/Dev/Git/personal/infinitely/html/resume.console/ \
   && cp ~/Dev/Git/personal/infinitely/html/resume.console/index.html ~/Dev/Git/personal/infinitely/html/resume.console/404.html \
   && cd ~/Dev/Git/personal/infinitely/html/resume.console/ \
   && git add --all \
   && git commit -m "update" \
   && git push origin master \
   && cd ~/Dev/Git/personal/infinitely/serveV2/html/diaryConsole/