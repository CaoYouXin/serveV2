import { Component, OnInit } from '@angular/core';
import { AlbumEditorService } from '../../../services/album-editor.service';

@Component({
  selector: 'app-edit-album',
  templateUrl: './edit-album.component.html',
  styleUrls: ['./edit-album.component.css']
})
export class EditAlbumComponent implements OnInit {

  show: boolean;

  constructor(private editorService: AlbumEditorService) { }

  ngOnInit() {
    this.editorService.bindAlbumEditorCmd().subscribe(cmd => this.show = cmd.show);
  }

}
