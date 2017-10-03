import { environment } from "../../environments/environment";

export class API {

  private static mode = environment.production ? 'prod' : 'dev';

  private static api = {
    "domain": {
      "prod": "http://server.caols.tech:9999/",
      "dev": `http://${document.domain}:9999/`
    },
    "login": {
      "prod": `http://server.caols.tech:9999/api/blog/user/login`,
      "dev": `http://${document.domain}:9999/api/blog/user/login`
    },
    "register": {
      "prod": `http://server.caols.tech:9999/api/blog/user/register`,
      "dev": `http://${document.domain}:9999/api/blog/user/register`
    },
    "CaptchaImage": {
      "prod": function (width, height, token) {
        return `url(http://server.caols.tech:9999/api/blog/user/register/captcha/${width}/${height}?token=${token})`;
      },
      "dev": function (width, height, token) {
        return `url(http://${document.domain}:9999/api/blog/user/register/captcha/${width}/${height}?token=${token})`;
      }
    },
    "upload/code": {
      "prod": function (path) {
        return `http://server.caols.tech:9999/upload${path}`;
      },
      "dev": function (path) {
        return `http://${document.domain}:9999/upload${path}`;
      }
    },
    "copy/file": {
      "prod": "http://server.caols.tech:9999/metaApi/copy",
      "dev": `http://${document.domain}:9999/metaApi/copy`
    },
    "photo/save": {
      "prod": "http://server.caols.tech:9999/api/album/photo/save",
      "dev": `http://${document.domain}:9999/api/album/photo/save`
    },
    "photo/list": {
      "prod": (page, size) => `http://server.caols.tech:9999/api/album/photo/list/${page}/${size}`,
      "dev": (page, size) => `http://${document.domain}:9999/api/album/photo/list/${page}/${size}`
    },
    "album/save": {
      "prod": "http://server.caols.tech:9999/api/album/album/save",
      "dev": `http://${document.domain}:9999/api/album/album/save`
    },
    "album/list": {
      "prod": "http://server.caols.tech:9999/api/album/album/list",
      "dev": `http://${document.domain}:9999/api/album/album/list`
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}
