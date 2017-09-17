import { Component, Input, OnInit } from "@angular/core";

@Component({
  selector: 'diary-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {

  @Input()
  width: number;

  @Input()
  table: any;

  @Input()
  data: Array<any>;

  ngOnInit() {
    this.calcTotalWidth();
  }

  calcTotalWidth() {
    let _table = this.table;
    let total = _table.ctrls && _table.ctrls.length ? _table.ctrlsWidth : 0;
    let len = _table.heads.length;
    _table.heads.forEach(head => total += head.width);
    _table.tWidth = total;
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

  tweakHeader(e, i) {
    if (!e.ctrlKey) {
      return;
    }

    if (null === i) {
      // let _table = this.table;
      // _table.ctrlsWidth = Math.max(50, _table.ctrlsWidth - e.deltaY / 2);
      // this.calcTotalWidth();
      return;
    }

    let _header = this.table.heads[i];
    _header.width = Math.max(_header.text.length * 16 + 20, _header.width - e.deltaY / 2);
    this.calcTotalWidth();
  }

  tweakData(e, i) {
    if (!e.ctrlKey) {
      return;
    }

    let _height = this.getHeight(i) - e.deltaY / 2;
    this.table.height[i] = Math.max(50, _height);
  }

}
