import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { PhotoService } from '../../services/photo.service';
import { GalleryService } from '../../services/gallery.service';
import { API } from '../../services/api.const';

@Component({
  selector: 'app-album-photo',
  templateUrl: './album-photo.component.html',
  styleUrls: ['./album-photo.component.css']
})
export class AlbumPhotoComponent implements OnInit {

  albumId: number;

  photos: Array<any>;
  page: number;
  total: number;
  size: number;
  fliped: boolean;
  flipText: string = '编辑';
  database: Array<any>;

  @ViewChild('wrapper')
  wrapper: ElementRef;

  constructor(private route: ActivatedRoute, private router: Router, private photoService: PhotoService, private galleryService: GalleryService) { }

  ngOnInit() {
    const self = this;

    this.size = this.total = 2 * Math.floor(this.wrapper.nativeElement.offsetWidth / 220);
    this.page = 1;

    self.route.paramMap.switchMap((params: ParamMap) => {
      self.albumId = Number(params.get("id"));
      return self.photoService.listAlbumPhotosObservable(self.albumId, self.page, self.size);
    }).subscribe(paged => self.retCollect(paged, self, self.page));
  }

  private retCollect(paged, self, page) {
    self.page = page;
    self.total = paged.Total || self.size;
    self.photos = paged.Photos.map(photo => ({
      ...photo,
      src: API.getAPI("domain") + photo.AlbumPhotoUrl
    }));
  }

  private listPhotos(page) {
    const self = this;
    self.photoService.list(page, self.size, (paged) => self.retCollect(paged, self, page));
  }

  private listAlbumPhotos(page) {
    const self = this;
    self.photoService.listAlbumPhotos(self.albumId, page, self.size, (paged) => self.retCollect(paged, self, page));
  }

  goToPage(page) {
    if (page < 1 || page > this.getLastPage()) {
      return;
    }

    const self = this;
    if (self.fliped) {
      self.listPhotos(page);
    } else {
      self.listAlbumPhotos(page);
    }
  }

  goToLastPage() {
    this.goToPage(this.getLastPage());
  }

  getLastPage() {
    return Math.floor(this.total / this.size) + (this.total % this.size === 0 ? 0 : 1);
  }

  flip() {
    this.fliped = !this.fliped;
    setTimeout((self) => self.flipText = self.fliped ? '保存' : '编辑', 500, this);

    this.total = this.size;

    if (this.fliped) {
      const self = this;
      self.photoService.fetchDatabase(self.albumId, (ret) => {
        self.database = ret;
        self.goToPage(1);
      });
    } else {
      this.goToPage(1);
    }
  }

  onGallery(idx) {
    this.galleryService.show(idx, this.photos);
  }

  onChange(checked, photo) {
    if (checked) {
      this.database = [photo, ...this.database];
    } else {
      let idx = this.database.findIndex(db => db.AlbumPhotoId === photo.AlbumPhotoId);
      this.database = [...this.database.slice(0, idx), ...this.database.slice(idx + 1)];
    }
  }

  goToMakeVideo() {
    let count = window.prompt('输入视频里包含的照片数量，默认最多20张');
    if (!count) {
      count = 20 + '';
    }

    let realCount = Number(count);
    if (realCount <= 0) {
      realCount = 20;
    }

    this.router.navigate(['makevideo', this.albumId, realCount]);
  }

}
