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
    },
    "list/page/by/book": {
      "prod": (bookId) => `http://server.caols.tech:9999/api/diary/list/page/by/${bookId}`,
      "dev": (bookId) => `http://localhost:9999/api/diary/list/page/by/${bookId}`
    },
    "release/page": {
      "prod": (bookId, pageId) => `http://server.caols.tech:9999/api/diary/release/${bookId}/${pageId}`,
      "dev": (bookId, pageId) => `http://localhost:9999/api/diary/release/${bookId}/${pageId}`
    },
    "attach/page": {
      "prod": (bookId, pageId) => `http://server.caols.tech:9999/api/diary/attach/${bookId}/${pageId}`,
      "dev": (bookId, pageId) => `http://localhost:9999/api/diary/attach/${bookId}/${pageId}`
    },
    "milestone/list": {
      "prod": (pageId) => `http://server.caols.tech:9999/api/diary/list/milestone/${pageId}`,
      "dev": (pageId) => `http://localhost:9999/api/diary/list/milestone/${pageId}`
    },
    "milestone/save": {
      "prod": "http://server.caols.tech:9999/api/diary/save/milestone",
      "dev": "http://localhost:9999/api/diary/save/milestone"
    },
    "photo/list": {
      "prod": (pageId) => `http://server.caols.tech:9999/api/diary/list/photo/${pageId}`,
      "dev": (pageId) => `http://localhost:9999/api/diary/list/photo/${pageId}`
    },
    "photo/save": {
      "prod": "http://server.caols.tech:9999/api/diary/save/photo",
      "dev": "http://localhost:9999/api/diary/save/photo"
    },
    "upload/code": {
      "prod": function (path) {
        return `http://server.caols.tech:9999/upload${path}`;
      },
      "dev": function (path) {
        return `http://localhost:9999/upload${path}`;
      }
    },
    "copy/file": {
      "prod": "http://server.caols.tech:9999/metaApi/copy",
      "dev": "http://localhost:9999/metaApi/copy"
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}