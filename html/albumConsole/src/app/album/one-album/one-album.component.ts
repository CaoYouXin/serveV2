import { Component, OnInit, HostBinding, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { AlbumEditorService } from '../../services/album-editor.service';

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

  @Output('onDelete')
  onDelete: EventEmitter<any> = new EventEmitter<any>();

  show: boolean;

  constructor(private router: Router, private editorService: AlbumEditorService) { }

  ngOnInit() {
  }

  goToDetails() {
    this.router.navigate(['album', this.data.AlbumId]);
  }

  showInfo() {
    this.show = true;
  }

  hideInfo() {
    this.show = false;
  }

  edit() {
    this.editorService.edit(this.data);
  }

}
