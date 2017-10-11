import { Component, OnInit, Input, HostBinding, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-setting',
  templateUrl: './setting.component.html',
  styleUrls: ['./setting.component.css']
})
export class SettingComponent implements OnInit {

  @HostBinding('style.display') display = 'block';
  @HostBinding('style.position') position = 'relative';
  @HostBinding('style.overflow') overflow = 'hidden';
  @HostBinding('style.height') height = '100px';

  @Input('photo')
  photo: number;

  @Input('mouse')
  mouse: number;

  @Output('drag')
  drag: EventEmitter<any> = new EventEmitter<any>();

  constructor() { }

  handleMouseDown(e) {
    if (e.buttons !== 1) {
      return;
    }
    this.drag.emit();
  }

  ngOnInit() {
  }

}
