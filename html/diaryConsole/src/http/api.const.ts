import { environment } from "../environments/environment";

export class API {

  static mode = environment.production ? 'prod' : 'dev';

  static api = {
    "server/origin": {
      "prod": `http://server.caols.tech:9999`,
      "dev": `http://localhost:9999`
    },
    "categories": {
      "prod": `http://server.caols.tech:9999/api/blog/category/list/client`,
      "dev": `http://localhost:9999/api/blog/category/list/client`
    },
    "category": {
      "prod": function (id) {
        return `http://server.caols.tech:9999/api/blog/category/${id}`;
      },
      "dev": function (id) {
        return `http://localhost:9999/api/blog/category/${id}`;
      }
    },
    "all/posts": {
      "prod": `http://server.caols.tech:9999/api/blog/post/list/client`,
      "dev": `http://localhost:9999/api/blog/post/list/client`
    },
    "posts": {
      "prod": function (categoryId) {
        return `http://server.caols.tech:9999/api/blog/post/list/client/${categoryId}`;
      },
      "dev": function (categoryId) {
        return `http://localhost:9999/api/blog/post/list/client/${categoryId}`;
      }
    },
    "post": {
      "prod": function (id) {
        return `http://server.caols.tech:9999/api/blog/post/${id}`;
      },
      "dev": function (id) {
        return `http://localhost:9999/api/blog/post/${id}`;
      }
    },
    "like": {
      "prod": function (postId) {
        return `http://server.caols.tech:9999/api/blog/like/${postId}`;
      },
      "dev": function (postId) {
        return `http://localhost:9999/api/blog/like/${postId}`;
      }
    },
    "liked": {
      "prod": function (postId) {
        return `http://server.caols.tech:9999/api/blog/liked/${postId}`;
      },
      "dev": function (postId) {
        return `http://localhost:9999/api/blog/liked/${postId}`;
      }
    },
    "FetchComments": {
      "prod": function (postId) {
        return `http://server.caols.tech:9999/api/blog/comment/client/${postId}`;
      },
      "dev": function (postId) {
        return `http://localhost:9999/api/blog/comment/client/${postId}`;
      }
    },
    "Comment": {
      "prod": `http://server.caols.tech:9999/api/blog/comment/set`,
      "dev": `http://localhost:9999/api/blog/comment/set`
    },
    "login": {
      "prod": `http://server.caols.tech:9999/api/blog/user/login`,
      "dev": `http://localhost:9999/api/blog/user/login`
    },
    "register": {
      "prod": `http://server.caols.tech:9999/api/blog/user/register`,
      "dev": `http://localhost:9999/api/blog/user/register`
    },
    "CheckUserName": {
      "prod": function (username) {
        return `http://server.caols.tech:9999/api/blog/username/check/${username}`;
      },
      "dev": function (username) {
        return `http://localhost:9999/api/blog/username/check/${username}`;
      }
    },
    "captcha": {
      "prod": function (phone) {
        return `http://server.caols.tech:9999/api/captcha?phone=${phone}`;
      },
      "dev": function (phone) {
        return `http://localhost:9999/api/user_api/captcha?phone=${phone}`;
      }
    },
    "FindPassword": {
      "prod": function (phone) {
        return `http://server.caols.tech:9999/api/password/find?phone=${phone}`;
      },
      "dev": function (phone) {
        return `http://localhost:9999/api/user_api/password/find?phone=${phone}`;
      }
    },
    "ResetPassword": {
      "prod": `http://server.caols.tech:9999/api/password/reset`,
      "dev": `http://localhost:9999/api/user_api/password/reset`
    },
    "CaptchaImage": {
      "prod": function (width, height, token) {
        return `url(http://server.caols.tech:9999/api/blog/user/register/captcha/${width}/${height}?token=${token})`;
      },
      "dev": function (width, height, token) {
        return `url(http://localhost:9999/api/blog/user/register/captcha/${width}/${height}?token=${token})`;
      }
    },
    "PreviousPost": {
      "prod": (date) => `http://server.caols.tech:9999/api/blog/previous-post/${date}`,
      "dev": (date) => `http://localhost:9999/api/blog/previous-post/${date}`
    },
    "NextPost": {
      "prod": (date) => `http://server.caols.tech:9999/api/blog/next-post/${date}`,
      "dev": (date) => `http://localhost:9999/api/blog/next-post/${date}`
    },
    "Top5Posts": {
      "prod": `http://server.caols.tech:9999/api/blog/post/list/top5`,
      "dev": `http://localhost:9999/api/blog/post/list/top5`
    },
    "SearchCategory": {
      "prod": `http://server.caols.tech:9999/api/search/category`,
      "dev": `http://localhost:9999/api/blog_api/search/category`
    },
    "SearchPost": {
      "prod": `http://server.caols.tech:9999/api/search/post`,
      "dev": `http://localhost:9999/api/blog_api/search/post`
    },
    "SearchPostWithCategory": {
      "prod": `http://server.caols.tech:9999/api/search/post_with_category`,
      "dev": `http://localhost:9999/api/blog_api/search/post_with_category`
    }
  };

  static getAPI(name: string) {
    return API.api[name][API.mode];
  }

}