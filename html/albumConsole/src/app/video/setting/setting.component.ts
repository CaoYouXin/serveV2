import { Component, OnInit, Input, HostListener, HostBinding, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-setting',
  templateUrl: './setting.component.html',
  styleUrls: ['./setting.component.css']
})
export class SettingComponent implements OnInit {

  @HostBinding('style.display') display = 'block';
  @HostBinding('style.overflow') overflow = 'hidden';
  @HostBinding('style.height') height = '100px';

  @Input('no')
  no: number;

  @Input('mouse')
  mouse: number;

  @Output('drag')
  drag: EventEmitter<any> = new EventEmitter<any>();

  constructor() { }

  @HostListener('mousedown')
  handleMouseDown() {
    this.drag.emit();
  }

  ngOnInit() {
  }

}
