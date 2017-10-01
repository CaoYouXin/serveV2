import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  userName: string;

  constructor(private router: Router, private userService: UserService) { }

  ngOnInit() {
    this.userService.bindUserName().subscribe(un => this.userName = un);
    this.userService.refreshUserName();
  }

  logout() {
    this.userService.logout();
    this.userService.setRetUrl(this.router.routerState.snapshot.url);
    this.router.navigateByUrl('/login');
  }
}
