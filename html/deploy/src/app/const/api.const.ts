import {environment} from "../../environments/environment";

export class API {

  private static mode = environment.production ? 'prod' : 'dev';

  private static api = {
    "database/status": {
      "prod": "http://server.caols.tech:9999/metaApi/database/status",
      "dev": "http://localhost:9999/metaApi/database/status"
    },
    "database/init": {
      "prod": (schema) => `http://server.caols.tech:9999/metaApi/database/init/${schema}`,
      "dev": (schema) => `http://localhost:9999/metaApi/database/init/${schema}`
    },
    "admin/setting": {
      "prod": "http://server.caols.tech:9999/metaApi/admin/setting",
      "dev": "http://localhost:9999/metaApi/admin/setting"
    },
    "admin/check": {
      "prod": "http://server.caols.tech:9999/metaApi/admin/check",
      "dev": "http://localhost:9999/metaApi/admin/check"
    },
    "admin/verify": {
      "prod": "http://server.caols.tech:9999/metaApi/admin/verify",
      "dev": "http://localhost:9999/metaApi/admin/verify"
    },
    "server/restart": {
      "prod": "http://server.caols.tech:9999/metaApi/server/restart/125",
      "dev": "http://localhost:9999/metaApi/server/restart/12"
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
    "ctrls/list": {
      "prod": "http://server.caols.tech:9999/metaApi/controller/list",
      "dev": "http://localhost:9999/metaApi/controller/list"
    },
    "ctrls/set": {
      "prod": function (className) {
        return `http://server.caols.tech:9999/metaApi/controller/set/${className}`;
      },
      "dev": function (className) {
        return `http://localhost:9999/metaApi/controller/set/${className}`;
      }
    },
    "ctrls/set/dis": {
      "prod": function (id, disabled) {
        return `http://server.caols.tech:9999/metaApi/controller/set/disabled/${id}/${disabled ? "T" : "F"}`;
      },
      "dev": function (id, disabled) {
        return `http://localhost:9999/metaApi/controller/set/disabled/${id}/${disabled ? "T" : "F"}`;
      }
    },
    "service/list": {
      "prod": "http://server.caols.tech:9999/metaApi/service/list",
      "dev": "http://localhost:9999/metaApi/service/list"
    },
    "service/set": {
      "prod": function (intf, impl) {
        return `http://server.caols.tech:9999/metaApi/service/set/${intf}/${impl}`;
      },
      "dev": function (intf, impl) {
        return `http://localhost:9999/metaApi/service/set/${intf}/${impl}`;
      }
    },
    "service/set/disable": {
      "prod": function (id, disabled) {
        return `http://server.caols.tech:9999/metaApi/service/set/disable/${id}/${disabled}`;
      },
      "dev": function (id, disabled) {
        return `http://localhost:9999/metaApi/service/set/disable/${id}/${disabled}`;
      }
    },
    "serve/auth/set": {
      "prod": function (className) {
        return `http://server.caols.tech:9999/metaApi/serve/auth/${className}`;
      },
      "dev": function (className) {
        return `http://localhost:9999/metaApi/serve/auth/${className}`;
      }
    },
    "inter/list": {
      "prod": "http://server.caols.tech:9999/metaApi/interceptor/list",
      "dev": "http://localhost:9999/metaApi/interceptor/list"
    },
    "inter/set": {
      "prod": function (className) {
        return `http://server.caols.tech:9999/metaApi/interceptor/set/${className}`;
      },
      "dev": function (className) {
        return `http://localhost:9999/metaApi/interceptor/set/${className}`;
      }
    },
    "inter/set/dis": {
      "prod": function (id, disabled) {
        return `http://server.caols.tech:9999/metaApi/interceptor/set/disabled/${id}/${disabled ? "T" : "F"}`;
      },
      "dev": function (id, disabled) {
        return `http://localhost:9999/metaApi/interceptor/set/disabled/${id}/${disabled ? "T" : "F"}`;
      }
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}
