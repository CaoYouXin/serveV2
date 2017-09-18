import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TableletService } from '../../service';
import { API } from '../../http';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.css']
})
export class PageComponent implements OnInit {

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
      { text: (idx) => "编辑", handler: this.editHandler.bind(this) },
      { text: (idx) => this.data[idx].DiaryPageDisabled ? "启用" : "禁用", handler: this.disableHandler.bind(this) },
      { text: (idx) => "里程碑", handler: this.milestoneHandler.bind(this) },
      { text: (idx) => "相册", handler: this.fakeHandler.bind(this) }
    ],
    ctrlsWidth: 320
  };

  data: Array<any> = [];

  specShow: boolean = true;

  constructor(private router: Router,
    private tablelet: TableletService) { }

  ngOnInit() {
    this.tablelet.getData(TableletService.PAGEs).subscribe(
      data => this.data = data
    );

    this.refreshTable();
  }

  newPage() {
    this.tablelet.setHandlingIdx(TableletService.PAGEs, null);
    this.router.navigate(['/editpage']);
  }

  refreshTable() {
    this.tablelet.setData(TableletService.PAGEs, []);
    this.tablelet.setDataByAPI(TableletService.PAGEs, API.getAPI("page/list"));
  }

  fakeHandler() {
    alert('clicked.');
  }

  milestoneHandler(idx: number) {
    this.tablelet.setHandlingIdx(TableletService.PAGEs, idx);
    this.router.navigate(['/milestone']);
  }

  disableHandler(idx: number) {
    this.tablelet.addDataByAPI(TableletService.PAGEs, API.getAPI("page/save"), Object.assign(this.data[idx], {
      DiaryPageDisabled: !this.data[idx].DiaryPageDisabled
    }), idx);
  }

  editHandler(idx: number) {
    this.tablelet.setHandlingIdx(TableletService.PAGEs, idx);
    this.router.navigate(['/editpage']);
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
