# serveV2
infinitely serve version 2

#### core
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
    * controller & interceptor & service
* auth
    * api access auth
    * file access auth

##### core apis
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