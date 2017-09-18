import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  tableDef: any = {
    heads: [
      { key: "DiaryPageId", text: "ID", width: 50 },
      { key: "DiaryPageType", text: "类型", width: 50 },
      { key: "DiaryPageTitle", text: "标题", width: 100 },
      { key: "DiaryPageStartDate", text: "开始日期", width: 150 },
      { key: "DiaryPageEndDate", text: "结束日期", width: 150 },
      { key: "DiaryPageContent", text: "内容", width: 200 },
      { key: "DiaryPageLocation", text: "地点", width: 100 },
      { key: "DiaryPageRelated", text: "相关人", width: 100 },
      {
        key: "DiaryPageDisabled", text: "状态", width: 100, render: (disabled) => {
          return disabled ? "禁用" : "启用";
        }
      },
    ],
    ctrls: [
      { text: (idx) => "编辑", handler: this.fakeHandler.bind(this) },
      { text: (idx) => this.data[idx].DiaryPageDisabled ? "启用" : "禁用", handler: this.fakeHandler.bind(this) },
      { text: (idx) => "里程碑", handler: this.fakeHandler.bind(this) },
      { text: (idx) => "相册", handler: this.fakeHandler.bind(this) }
    ],
    ctrlsWidth: 320
  };

  data: Array<any> = [];

  specShow: boolean = true;

  constructor() { }

  ngOnInit() {
  }

  fakeHandler() {
    alert('clicked.');
  }

  closeSpec(e) {
    let fontSize = document.defaultView.getComputedStyle(e.target, null).getPropertyValue("font-size");
    let fontSizeI = parseInt(fontSize.match(/\d+/)[0]);

    if (e.target.offsetWidth - fontSizeI * 1.5 < e.offsetX && e.offsetX < e.target.offsetWidth - fontSizeI / 2) {
      if (fontSizeI / 2 < e.offsetY && e.offsetY < fontSizeI * 1.5) {
        this.specShow = false;
      }
    }
  }

}
