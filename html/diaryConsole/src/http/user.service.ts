export class UserService {

  static store(user: any) {
    localStorage.setItem('currentUser', user.UserToken);
    localStorage.setItem('currentUserDetail', JSON.stringify(user));
  }

  static token() {
    return localStorage.getItem('currentUser');
  }

  static getUser() {
    const data = localStorage.getItem('currentUserDetail');

    if (!data) {
      return null;
    }

    return JSON.parse(data);
  }

}