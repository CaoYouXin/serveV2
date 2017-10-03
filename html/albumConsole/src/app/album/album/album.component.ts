import { Component, OnInit } from '@angular/core';
import { AlbumEditorService } from '../../services/album-editor.service';
import { AlbumService } from '../../services/album.service';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.css']
})
export class AlbumComponent implements OnInit {

  albums: Array<any>;

  constructor(private albumService: AlbumService, private editorService: AlbumEditorService) { }

  ngOnInit() {
    this.albumService.list((ret) => this.albums = ret);
  }

  addAlbum() {
    this.editorService.newOne();
  }

  onSave(evt) {
    let idx = this.albums.findIndex(album => album.AlbumId === evt.AlbumId);
    if (-1 === idx) {
      this.albums = [...this.albums, evt];
    } else {
      this.albums = [...this.albums.slice(0, idx), evt, ...this.albums.slice(idx + 1)];
    }
  }

  onDelete(idx, album) {
    this.albumService.delete(album.AlbumId, () => this.albums = [...this.albums.slice(0, idx), ...this.albums.slice(idx + 1)]);
  }

}
