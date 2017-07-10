import {Component, Input, OnDestroy, OnInit} from "@angular/core";

@Component({
  selector: 'table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit, OnDestroy {

  dragI: number = null;
  dragIdx: number = null;

  @Input()
  table: any;

  @Input()
  data: Array<Array<any>>;

  ngOnInit() {
    let total = 0;
    let len = this.table.width.length;
    this.table.width.forEach(w => total += w + 2);
    this.table.tWidth = total;

    this.drag = this.drag.bind(this);
    this.afterDrag = this.afterDrag.bind(this);

    document.addEventListener('mousemove', this.drag);
    document.addEventListener('mouseup', this.afterDrag);
  }

  ngOnDestroy(): void {
    document.removeEventListener('mousemove', this.drag);
    document.removeEventListener('mouseup', this.afterDrag);
  }

  getHeight(i) {
    if (!this.table.height) {
      this.table.height = [];
    }

    if (!this.table.height[i]) {
      this.table.height[i] = 50;
    }
    return this.table.height[i];
  }

  beforeDragI(i) {
    this.dragI = i;
  }

  beforeDragIdx(idx) {
    this.dragIdx = idx;
  }

  afterDrag() {
    this.dragIdx = null;
    this.dragI = null;
  }

  drag(e) {
    if (e.buttons !== 1 || (null === this.dragI && null === this.dragIdx)) {
      return;
    }

    let _table = this.table;

    if (null !== this.dragI) {
      _table.height[this.dragI] += e.movementY;
      _table.height[this.dragI] = Math.max(20, _table.height[this.dragI]);
    }

    if (null !== this.dragIdx) {
      _table.width[this.dragIdx] += e.movementX;
      _table.tWidth += e.movementX;

      _table.width[this.dragIdx] = Math.max(50, _table.width[this.dragIdx]);

      let min = 52;
      _table.width.forEach((w, i) => min += i === this.dragIdx ? 0 : (w + 2));
      _table.tWidth = Math.max(min, _table.tWidth);
    }


  }
}
