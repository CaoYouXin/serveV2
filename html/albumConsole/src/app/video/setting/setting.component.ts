import { Component, OnInit, Input, HostListener, HostBinding, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-setting',
  templateUrl: './setting.component.html',
  styleUrls: ['./setting.component.css']
})
export class SettingComponent implements OnInit {

  @HostBinding('style.display') display = 'block';
  @HostBinding('style.overflow') overflow = 'hidden';
  @HostBinding('style.transition') transition = 'height 1s';
  @HostBinding('style.height') height = '100px';
  @HostBinding('style.opacity') opacity = '1';

  @Input('no')
  no: number;

  constructor() { }

  @HostListener('mousedown', ['$event'])
  handleMouseDown(e) {
    if (e.buttons !== 1) {
      return;
    }
    this.opacity = '0';
    this.height = '0';
  }

  ngOnInit() {
  }

}
