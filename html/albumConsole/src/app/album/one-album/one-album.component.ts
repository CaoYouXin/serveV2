import { Component, OnInit, HostBinding, Input } from '@angular/core';

@Component({
  selector: 'app-one-album',
  templateUrl: './one-album.component.html',
  styleUrls: ['./one-album.component.css']
})
export class OneAlbumComponent implements OnInit {

  @HostBinding("style.width") hostWidth: string = "202px";
  @HostBinding("style.height") hostHeight: string = "202px";
  @HostBinding("style.border") hostBorder: string = "1px solid #eee";
  @HostBinding("style.margin") hostMargin: string = "20px 0 0 20px";

  @Input('data')
  data: any;

  constructor() { }

  ngOnInit() {
  }

}
