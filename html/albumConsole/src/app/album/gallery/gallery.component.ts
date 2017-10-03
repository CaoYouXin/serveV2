import { Component, OnInit } from '@angular/core';
import { GalleryService } from '../../services/gallery.service';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent implements OnInit {

  private posReset = {
    left: false,
    center: false,
    right: false
  }

  private slot0Reset = Object.assign({}, this.posReset, { left: true });
  private slot1Reset = Object.assign({}, this.posReset, { center: true });
  private slot2Reset = Object.assign({}, this.posReset, { right: true });

  private posRestAll = {
    slot0: this.slot0Reset,
    slot1: this.slot1Reset,
    slot2: this.slot2Reset
  };

  private canChange: boolean = true;

  show: boolean;
  pos: any = this.posRestAll;
  pic: any = {
    slot0: '',
    slot1: '',
    slot2: ''
  };
  ani: any = {
    slot0: true,
    slot1: true,
    slot2: true
  };
  idx: number = -2;
  data: Array<any>;

  constructor(private galleryService: GalleryService) { }

  getLeftPic() {
    return this.getPosPic(this.idx - 1);
  }

  getCenterPic() {
    return this.getPosPic(this.idx);
  }

  getRightPic() {
    return this.getPosPic(this.idx + 1);
  }

  getPosPic(idx) {
    let datum = this.data[idx];

    if (!datum) {
      return '';
    }

    return 'url(' + encodeURI(datum.src) + ')';
  }

  ngOnInit() {
    const self = this;
    self.galleryService.bindGalleryShow().subscribe(cmd => {
      self.show = cmd.show;
      self.idx = cmd.idx;
      self.data = cmd.data;

      if (self.show) {
        self.reset();
      }
    });
  }

  reset() {
    this.ani = {
      slot0: false,
      slot1: false,
      slot2: false
    }
    this.pos = this.posRestAll;
    this.pic = {
      slot0: this.getLeftPic(),
      slot1: this.getCenterPic(),
      slot2: this.getRightPic()
    };
    this.canChange = true;
  }

  prevent() {
    this.canChange = false;
  }

  isPreventing() {
    return !this.canChange;
  }

  prev() {
    if (this.isPreventing()) {
      return;
    }

    if (this.idx === 0) {
      alert('已经是该页第一张了！');
      return;
    }

    this.prevent();

    this.ani = {
      slot0: this.pos.slot0 != this.slot2Reset,
      slot1: this.pos.slot1 != this.slot2Reset,
      slot2: this.pos.slot2 != this.slot2Reset
    }
    this.pos = {
      slot0: this.pos.slot1,
      slot1: this.pos.slot2,
      slot2: this.pos.slot0
    }

    setTimeout((self) => {
      self.idx--;
      self.reset();
    }, 1000, this);
  }

  next() {
    if (this.isPreventing()) {
      return;
    }

    if (this.idx === this.data.length - 1) {
      alert('已经是该页最后一张了！');
      return;
    }

    this.prevent();

    this.ani = {
      slot0: this.pos.slot0 != this.slot0Reset,
      slot1: this.pos.slot1 != this.slot0Reset,
      slot2: this.pos.slot2 != this.slot0Reset
    }
    this.pos = {
      slot0: this.pos.slot2,
      slot1: this.pos.slot0,
      slot2: this.pos.slot1
    }

    setTimeout((self) => {
      self.idx++;
      self.reset();
    }, 1000, this);
  }

  close() {
    this.galleryService.hide();
  }

}
