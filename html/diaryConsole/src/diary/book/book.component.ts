import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TableletService } from '../../service';
import { API } from '../../http';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  tableDef: any = {
    heads: [
      { key: "DiaryBookId", text: "ID", width: 50 },
      { key: "DiaryBookTitle", text: "标题", width: 100 },
      { key: "DiaryBookPageCount", text: "页数", width: 50 },
      { key: "ResourceLevelName", text: "资源等级", width: 150 },
      {
        key: "DiaryBookDisabled", text: "状态", width: 100, render: (disabled) => {
          return disabled ? "禁用" : "启用";
        }
      },
    ],
    ctrls: [
      { text: (idx) => "编辑", handler: this.editHandler.bind(this) },
      { text: (idx) => this.data[idx].DiaryBookDisabled ? "启用" : "禁用", handler: this.disableHandler.bind(this) },
      { text: (idx) => "页码编辑", handler: this.pageEditHandler.bind(this) }
    ],
    ctrlsWidth: 256
  };

  data: Array<any> = [];

  specShow: boolean = true;

  constructor(private router: Router,
    private tablelet: TableletService) { }

  ngOnInit() {
    this.tablelet.getData(TableletService.BOOKs).subscribe(
      data => this.data = data
    );

    this.tablelet.setDataByAPI(TableletService.BOOKs, API.getAPI("book/list"));
  }

  refreshTable() {
    this.tablelet.setData(TableletService.BOOKs, []);
    this.tablelet.setDataByAPI(TableletService.BOOKs, API.getAPI("book/list"));
  }

  newBook() {
    this.tablelet.setHandlingIdx(TableletService.BOOKs, null);
    this.router.navigate(['/editbook']);
  }

  pageEditHandler(idx: number) {
    this.tablelet.setHandlingIdx(TableletService.BOOKs, idx);
    this.router.navigate(['/editbookpage']);
  }

  editHandler(idx: number) {
    this.tablelet.setHandlingIdx(TableletService.BOOKs, idx);
    this.router.navigate(['/editbook']);
  }

  disableHandler(idx: number) {
    this.tablelet.addDataByAPI(TableletService.BOOKs, API.getAPI("book/save"), Object.assign(this.data[idx], {
      DiaryBookDisabled: !this.data[idx].DiaryBookDisabled
    }), idx);
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
