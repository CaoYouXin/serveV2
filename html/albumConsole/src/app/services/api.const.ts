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
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}
