import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TableletService } from '../../service';
import { API, DaoUtil, RestCode } from '../../http';

@Component({
  selector: 'app-editbookpage',
  templateUrl: './editbookpage.component.html',
  styleUrls: ['./editbookpage.component.css']
})
export class EidtBookPageComponent implements OnInit {

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
      {
        key: "DiaryPageIncluded", text: "状态", width: 100, render: (included) => {
          return included ? "已收录" : "未收录";
        }
      },
    ],
    ctrls: [
      { text: (idx) => this.data[idx].DiaryPageIncluded ? "删除" : "收录", handler: this.editHandler.bind(this) }
    ],
    ctrlsWidth: 100
  };

  data: Array<any> = [];

  specShow: boolean = true;

  constructor(private router: Router,
    private tablelet: TableletService,
    private dao: DaoUtil,
    private rest: RestCode) { }

  ngOnInit() {
    this.tablelet.getData(TableletService.PAGEs).subscribe(
      data => this.data = data
    );

    this.refreshTable();
  }

  refreshTable() {
    this.tablelet.setData(TableletService.PAGEs, []);
    const self = this;
    self.dao.getJSON(API.getAPI("list/page/by/book")(
      self.tablelet.getHandlingData(TableletService.BOOKs).DiaryBookId
    )).subscribe(
      ret => self.rest.checkCode(ret, included => {
        self.tablelet.setDataByAPI(TableletService.PAGEs, API.getAPI("page/list"), (pages) => {
          for (var i = 0; i < pages.length; i++) {
            var page = pages[i];
            if (included.some(iP => iP.DiaryPageId === page.DiaryPageId)) {
              page.DiaryPageIncluded = true;
            } else {
              page.DiaryPageIncluded = false;
            }
          }
        });
      }),
      err => DaoUtil.logError(err)
      );
  }

  editHandler(idx: number) {
    const self = this;
    self.dao.getJSON((this.data[idx].DiaryPageIncluded ? API.getAPI("release/page") : API.getAPI("attach/page"))(
      self.tablelet.getHandlingData(TableletService.BOOKs).DiaryBookId,
      this.data[idx].DiaryPageId
    )).subscribe(
      ret => self.rest.checkCode(ret, retBody => {
        if (retBody) {
          self.tablelet.addData(TableletService.PAGEs, idx, Object.assign(self.data[idx], {
            DiaryPageIncluded: !self.data[idx].DiaryPageIncluded
          }));
        }
      }),
      err => DaoUtil.logError(err)
      );
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
