import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

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
    ctrls: [
      { text: (idx) => "编辑", handler: this.fakeHandler.bind(this) },
      { text: (idx) => this.data[idx].BlogCategoryDisabled ? "启用" : "禁用", handler: this.fakeHandler.bind(this) },
      { text: (idx) => "截图", handler: this.fakeHandler.bind(this) }
    ],
    ctrlsWidth: 250
  };

  data: Array<any> = [
    {
      "BlogPostId": 1,
      "BlogPostName": "https://www.google.com/search?newwindow=1&source=hp&q=thead+border+bottom&oq=thead+border+bottom&gs_l=psy-ab.3...1038.6150.0.6536.21.15.0.0.0.0.449.449.4-1.1.0....0...1.1.64.psy-ab..20.1.448.0..0j0i12i30k1j0i12i10i30k1.0.IG3E-kAbV5o"
    },

    {
      "BlogPostId": 10
    }
  ];

  specShow: boolean = true;

  constructor(private router: Router) { }

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
