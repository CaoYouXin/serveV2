import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { PhotoService } from '../../services/photo.service';
import { API } from '../../services/api.const';

@Component({
  selector: 'app-generator',
  templateUrl: './generator.component.html',
  styleUrls: ['./generator.component.css']
})
export class GeneratorComponent implements OnInit {

  private albumId: number;

  photos: Array<any>;

  @ViewChild('listContent')
  listContent: ElementRef;

  @ViewChild('listContainer')
  listContainer: ElementRef;

  listTransform: string = 'translate3d(0, 0, 0)';
  pressing: any = null;
  unpressings: Array<any>;

  constructor(private route: ActivatedRoute, private photoService: PhotoService) { }

  ngOnInit() {
    const self = this;
    this.route.paramMap.switchMap((params: ParamMap) => {
      self.albumId = Number(params.get("id"));
      return self.photoService.listAlbumPhotosObservable(self.albumId, 1, Number(params.get("size")));
    }).subscribe(paged => self.photos = paged.Photos.map(p => ({
      ...p,
      src: API.getAPI("domain") + p.AlbumPhotoUrl
    })));
  }

  scroll(e) {
    if (Math.abs(e.deltaY) < Math.abs(e.deltaX)) {
      return;
    }

    let curPos = parseInt(this.listTransform.match(/translate3d\(0, (\-?\d+(?:\.\d+)?).*?, 0\)/)[1], 10);

    if (e.deltaY <= 0) {
      curPos = Math.min(0, curPos - e.deltaY);
    } else {
      curPos = Math.max(
        Math.min(0, this.listContainer.nativeElement.offsetHeight - this.listContent.nativeElement.offsetHeight)
        , curPos - e.deltaY);
    }

    this.listTransform = 'translate3d(0, ' + curPos + 'px, 0)';
  }

  onDrag(idx) {
    this.pressing = this.photos[idx];
    this.unpressings = [...this.photos.slice(0, idx), ...this.photos.slice(idx + 1)];
  }

  mousemove(e) {
    if (this.pressing === null || e.buttons !== 1) {
      return;
    }

    let curPos = parseInt(this.listTransform.match(/translate3d\(0, (\-?\d+(?:\.\d+)?).*?, 0\)/)[1], 10);
    let on = Math.floor((e.clientY - curPos - 40) / 100);

    this.photos = [
      ...this.unpressings.slice(0, on),
      this.pressing,
      ...this.unpressings.slice(on)
    ];
  }

  mouseup() {
    this.pressing = null;
  }

}
