import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.css']
})
export class PageComponent implements OnInit {

  tableDef: any = {
    heads: [
      { key: "BlogPostId", text: "ID", width: 50 },
      { key: "BlogCategoryName", text: "分类", width: 100 },
      { key: "BlogPostName", text: "名称", width: 100 },
      {
        key: "BlogPostType", text: "类型", width: 100, render: (type) => {
          return '1' === type + '' ? "APP" : '2' === type + '' ? "文章" : "未知";
        }
      },
      { key: "BlogPostUrl", text: "URL", width: 200 },
      { key: "BlogPostScript", text: "SCRIPT", width: 200 },
      { key: "BlogPostCreateTime", text: "创建时间", width: 150 },
      { key: "BlogPostUpdateTime", text: "更新时间", width: 150 },
      {
        key: "BlogPostDisabled", text: "状态", width: 100, render: (disabled) => {
          return disabled ? "禁用" : "启用";
        }
      },
    ],
    // ctrls: [
    // { text: (idx) => "编辑", handler: this.edit.bind(this) },
    // { text: (idx) => this.data[idx].BlogCategoryDisabled ? "启用" : "禁用", handler: this.toggle.bind(this) },
    // { text: (idx) => "截图", handler: this.screenshot.bind(this) }
    // ],
    // ctrlsWidth: 150
  };

  data: Array<any> = [
    {
      "BlogPostId": 1
    },

    {
      "BlogPostId": 10
    }
  ];

  constructor() { }

  ngOnInit() {
  }

}
