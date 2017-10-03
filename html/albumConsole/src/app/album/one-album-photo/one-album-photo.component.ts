import { Component, OnInit, HostBinding, Input, Output, EventEmitter } from '@angular/core';
import { AlbumPhotoService } from '../../services/album-photo.service';

@Component({
  selector: 'app-one-album-photo',
  templateUrl: './one-album-photo.component.html',
  styleUrls: ['./one-album-photo.component.css']
})
export class OneAlbumPhotoComponent implements OnInit {

  @HostBinding("style.width") hostWidth: string = "202px";
  @HostBinding("style.height") hostHeight: string = "202px";
  @HostBinding("style.border") hostBorder: string = "1px solid #eee";
  @HostBinding("style.margin") hostMargin: string = "10px 0 10px 20px";

  @Input('bind')
  bind: number;

  @Input('data')
  data: any;

  @Input('mode')
  mode: boolean;

  @Input('database')
  database: Array<any>;

  @Output('onGallery')
  onGallery: EventEmitter<any> = new EventEmitter<any>();

  @Output('onChange')
  onChange: EventEmitter<boolean> = new EventEmitter<boolean>();

  loading: boolean;

  constructor(private albumPhotoService: AlbumPhotoService) { }

  ngOnInit() {
  }

  gallery() {
    if (!this.mode) this.onGallery.emit();
  }

  isChecked() {
    if (!this.database) {
      return false;
    }

    return this.database.some(db => db.AlbumPhotoId === this.data.AlbumPhotoId);
  }

  checkChange(e) {
    if (this.loading) {
      e.preventDefault();
      return;
    }

    const self = this;
    self.loading = true;
    if (e.target.checked) {
      self.albumPhotoService.attach(self.bind, self.data.AlbumPhotoId, () => {
        self.onChange.emit(e.target.checked);
        self.loading = false;
      });
    } else {
      self.albumPhotoService.release(self.bind, self.data.AlbumPhotoId, () => {
        self.onChange.emit(e.target.checked);
        self.loading = false;
      });
    }
  }

}
