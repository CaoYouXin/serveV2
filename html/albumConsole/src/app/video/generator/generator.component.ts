import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-generator',
  templateUrl: './generator.component.html',
  styleUrls: ['./generator.component.css']
})
export class GeneratorComponent implements OnInit {

  photos: Array<any> = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20];

  @ViewChild('listContent')
  listContent: ElementRef;

  @ViewChild('listContainer')
  listContainer: ElementRef;

  listTransform: string = 'translate3d(0, 0, 0)';
  pressing: any = null;
  unpressings: Array<any>;

  constructor() { }

  ngOnInit() {
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
    if (this.pressing === null) {
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
