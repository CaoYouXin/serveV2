import { environment } from "../environments/environment";

export class API {

  static mode = environment.production ? 'prod' : 'dev';

  static api = {
    "server/origin": {
      "prod": `http://server.caols.tech:9999`,
      "dev": `http://localhost:9999`
    },
    "admin/verify": {
      "prod": "http://server.caols.tech:9999/metaApi/admin/verify",
      "dev": "http://localhost:9999/metaApi/admin/verify"
    },
    "page/save": {
      "prod": "http://server.caols.tech:9999/api/diary/save/page",
      "dev": "http://localhost:9999/api/diary/save/page"
    },
    "page/list": {
      "prod": "http://server.caols.tech:9999/api/diary/list/page/all",
      "dev": "http://localhost:9999/api/diary/list/page/all"
    },
    "book/save": {
      "prod": "http://server.caols.tech:9999/api/diary/save/book",
      "dev": "http://localhost:9999/api/diary/save/book"
    },
    "book/list": {
      "prod": "http://server.caols.tech:9999/api/diary/list/book/all",
      "dev": "http://localhost:9999/api/diary/list/book/all"
    },
    "resource-level/list": {
      "prod": "http://server.caols.tech:9999/api/blog/resource-level/list",
      "dev": "http://localhost:9999/api/blog/resource-level/list"
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}