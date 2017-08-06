import { environment } from "../../environments/environment";

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
    },
    "upload/code": {
      "prod": function (path) {
        return `http://server.caols.tech:9999/upload${path}`;
      },
      "dev": function (path) {
        return `http://localhost:9999/upload${path}`;
      }
    },
    "list/file": {
      "prod": function (path) {
        return `http://server.caols.tech:9999/metaApi/list/${path}`;
      },
      "dev": function (path) {
        return `http://localhost:9999/metaApi/list/${path}`;
      }
    },
    "unzip/file": {
      "prod": function (path, to) {
        return `http://server.caols.tech:9999/metaApi/unzip/${path}?to=${to}`;
      },
      "dev": function (path, to) {
        return `http://localhost:9999/metaApi/unzip/${path}?to=${to}`;
      }
    },
    "delete/file": {
      "prod": function (path) {
        return `http://server.caols.tech:9999/metaApi/delete/${path}`;
      },
      "dev": function (path) {
        return `http://localhost:9999/metaApi/delete/${path}`;
      }
    },
    "create/file": {
      "prod": function (path) {
        return `http://server.caols.tech:9999/metaApi/create/${path}`;
      },
      "dev": function (path) {
        return `http://localhost:9999/metaApi/create/${path}`;
      }
    },
    "copy/file": {
      "prod": "http://server.caols.tech:9999/metaApi/copy",
      "dev": "http://localhost:9999/metaApi/copy"
    },
    "user/list": {
      "prod": "http://server.caols.tech:9999/api/blog/user/list",
      "dev": "http://localhost:9999/api/blog/user/list"
    },
    "user/disabled": {
      "prod": function (id, disabled) {
        return `http://server.caols.tech:9999/api/blog/user/disabled/${id}/${disabled ? "T" : "F"}`;
      },
      "dev": function (id, disabled) {
        return `http://localhost:9999/api/blog/user/disabled/${id}/${disabled ? "T" : "F"}`;
      }
    },
    "favour/list": {
      "prod": "http://server.caols.tech:9999/api/blog/user-favour/list",
      "dev": "http://localhost:9999/api/blog/user-favour/list"
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}
