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
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}
