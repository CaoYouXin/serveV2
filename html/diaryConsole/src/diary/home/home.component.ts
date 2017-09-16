import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'diary-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  goFront: boolean;
  goBehind: boolean = true;

  constructor() { }

  ngOnInit() {
  }

  clicked() {
    this.goBehind = !this.goBehind;
    this.goFront = !this.goFront;
  }

}
