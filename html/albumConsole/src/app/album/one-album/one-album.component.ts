import { Component, OnInit, HostBinding, Input, Output, EventEmitter } from '@angular/core';
import { DomSanitizer, SafeStyle } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AlbumEditorService } from '../../services/album-editor.service';
import { PhotoService } from '../../services/photo.service';
import { API } from '../../services/api.const';

@Component({
  selector: 'app-one-album',
  templateUrl: './one-album.component.html',
  styleUrls: ['./one-album.component.css']
})
export class OneAlbumComponent implements OnInit {

  @HostBinding("style.width") hostWidth: string = "202px";
  @HostBinding("style.height") hostHeight: string = "202px";
  @HostBinding("style.border") hostBorder: string = "1px solid #eee";
  @HostBinding("style.margin") hostMargin: string = "10px 0 10px 20px";

  @Input('data')
  data: any;

  @Output('onDelete')
  onDelete: EventEmitter<any> = new EventEmitter<any>();

  show: boolean;
  grid: SafeStyle;

  constructor(private router: Router, private sanity: DomSanitizer, private editorService: AlbumEditorService, private photoService: PhotoService) { }

  ngOnInit() {
    const self = this;
    self.photoService.listAlbumPhotos(self.data.AlbumId, 1, 4, (ret) => {
      self.grid = this.sanity.bypassSecurityTrustStyle(ret.Photos.map(p => "url('" + API.getAPI("domain") + p.AlbumPhotoUrl + "')").join(','));
    });
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
