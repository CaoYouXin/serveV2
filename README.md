# serveV2
---
Infinitely Serve --- version 2

基于 httpcore 实现的服务器框架，最大的优点是API动态修改功能。
并包含基本的文件读取和上传功能。

### core
---
* handler
    * upload support
    * http file serving
    * serving file with interceptors
    * serving apis with interceptors
* in-memory typed cache
* orm
    * datasource
    * transaction
    * interface-oriented programming
* rest
    * uri regexp matcher
    * helper
        * decode uri to url
        * response html、json、image
        * cross origin support
        * request method detecting
        * request body parsing
        * interceptor utilities
        * one service call with exception handler
    * controller & interceptor & service
* auth
    * api access auth
    * file access auth
* bean manager
    * bean cache using hash map
    * bean proxy with origin switch function
    * interface declared bean's initialization

##### core apis
---
* admin existence check
* admin setting
* admin login
* datasource status check
* set datasource
* controller setting
    * set
    * disable
    * list
* service setting
    * same with controller's
* interceptor setting
    * same with controller's
* list files of a dir
* unzip file to a dir
* set file access auth strategy