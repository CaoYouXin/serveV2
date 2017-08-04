import {environment} from "../../environments/environment";

export class API {

  private static mode = environment.production ? 'prod' : 'dev';

  private static api = {
    "blog/init": {
      "prod": "http://server.caols.tech:9999/api/blog/init",
      "dev": "http://localhost:9999/api/blog/init"
    },
    "admin/verify": {
      "prod": "http://server.caols.tech:9999/metaApi/admin/verify",
      "dev": "http://localhost:9999/metaApi/admin/verify"
    },
    "resource-level/set": {
      "prod": "http://server.caols.tech:9999/api/blog/resource-level/set",
      "dev": "http://localhost:9999/api/blog/resource-level/set"
    },
    "resource-level/list": {
      "prod": "http://server.caols.tech:9999/api/blog/resource-level/list",
      "dev": "http://localhost:9999/api/blog/resource-level/list"
    },
    "resource-mapping/list": {
      "prod": "http://server.caols.tech:9999/api/blog/resource-level-mapping/list",
      "dev": "http://localhost:9999/api/blog/resource-level-mapping/list"
    },
    "resource-level-mapping/set": {
      "prod": "http://server.caols.tech:9999/api/blog/resource-level-mapping/set",
      "dev": "http://localhost:9999/api/blog/resource-level-mapping/set"
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}
