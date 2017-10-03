import { Component, OnInit } from '@angular/core';
import { AlbumEditorService } from '../../services/album-editor.service';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.css']
})
export class AlbumComponent implements OnInit {

  albums: Array<any>;

  constructor(private editorService: AlbumEditorService) { }

  ngOnInit() {
  }

  addAlbum() {
    this.editorService.newOne();
  }

}
