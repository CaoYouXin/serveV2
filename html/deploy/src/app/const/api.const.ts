import {environment} from "../../environments/environment";

export class API {

  private static mode = environment.production ? 'prod' : 'dev';

  private static api = {
    "database/status": {
      "prod": "http://server.caols.tech/metaApi/database/status",
      "dev": "http://localhost:9999/metaApi/database/status"
    },
    "admin/setting": {
      "prod": "http://server.caols.tech/metaApi/admin/setting",
      "dev": "http://localhost:9999/metaApi/admin/setting"
    },
    "admin/check": {
      "prod": "http://server.caols.tech/metaApi/admin/check",
      "dev": "http://localhost:9999/metaApi/admin/check"
    },
    "admin/verify": {
      "prod": "http://server.caols.tech/metaApi/admin/verify",
      "dev": "http://localhost:9999/metaApi/admin/verify"
    },
    "server/restart": {
      "prod": "http://server.caols.tech/metaApi/server/restart",
      "dev": "http://localhost:9999/metaApi/server/restart"
    },
    "upload/code": {
      "prod": function (path) {
        return `http://server.caols.tech/upload${path}`;
      },
      "dev": function (path) {
        return `http://localhost:9999/upload${path}`;
      }
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}
