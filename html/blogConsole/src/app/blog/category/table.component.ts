import { Component, HostBinding, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { slideInUpAnimation } from "../../animation/route.animation";
import { RouteService, TableletService } from "../../service";
import { DaoUtil, RestCode } from "../../http";
import { API } from "../../const";

@Component({
  selector: 'blog-category',
  templateUrl: './table.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', './table.component.css'],
  animations: [slideInUpAnimation]
})
export class BlogCategoryComponent implements OnInit, OnDestroy {

  @HostBinding("@routeAnimation") animation = true;
  @HostBinding("style.display") display = "block";
  @HostBinding("style.position") position = "absolute";
  @HostBinding("style.width") width = "100%";

  tableDef: any = {
    heads: [
      { key: "BlogCategoryId", text: "ID", width: 50 },
      { key: "ParentBlogCategoryName", text: "父级名称", width: 100 },
      { key: "BlogCategoryName", text: "名称", width: 100 },
      { key: "BlogCategoryUrl", text: "URL", width: 200 },
      { key: "BlogCategoryScript", text: "SCRIPT", width: 200 },
      {
        key: "BlogCategoryDisabled", text: "状态", width: 100, render: (disabled) => {
          return disabled ? "禁用" : "启用";
        }
      },
    ],
    ctrls: [
      { text: (idx) => "编辑", handler: this.edit.bind(this) },
      { text: (idx) => this.data[idx].BlogCategoryDisabled ? "启用" : "禁用", handler: this.toggle.bind(this) }
    ],
    ctrlsWidth: 110
  };

  data: Array<any> = [];

  constructor(private route: ActivatedRoute,
    private routeService: RouteService,
    private tablelet: TableletService,
    private rest: RestCode,
    private dao: DaoUtil) { }

  ngOnInit() {
    this.routeService.setCurrentRouteConfig({
      init: true,
      route: this.route.routeConfig
    });

    const self = this;
    this.tablelet.getData(TableletService.BLOG_CATEGORY).subscribe(
      ret => {
        ret.forEach(element => {
          if (element.ParentBlogCategoryName) {
            return;
          }

          ret.forEach(elem => {
            if (elem.BlogCategoryId + '' === element.ParentBlogCategoryId + '') {
              element.ParentBlogCategoryName = elem.BlogCategoryName;
            }
          });

          if (!element.ParentBlogCategoryName) {
            element.ParentBlogCategoryName = '顶级分类';
          }
        });
        self.data = ret;
      },
      err => DaoUtil.logError(err)
    );

    this.dao.getJSON(API.getAPI("category/list")).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        self.tablelet.setData(TableletService.BLOG_CATEGORY, retBody);
      }),
      err => DaoUtil.logError(err)
    );
  }

  ngOnDestroy() {
    this.routeService.setCurrentRouteConfig({
      init: false,
      route: this.route.routeConfig
    });
  }

  toAdd() {
    this.tablelet.setMaskStatus(TableletService.BLOG_CATEGORY, {
      maskName: 'mask',
      mask: true,
      submitText: '添加',
      data: this.data
    });
  }

  preview() {
    this.tablelet.setMaskStatus(TableletService.BLOG_CATEGORY, {
      maskName: 'preview',
      mask: true
    });
  }

  edit(idx) {
    this.tablelet.setMaskStatus(TableletService.BLOG_CATEGORY, {
      maskName: 'mask',
      mask: true,
      submitText: '保存',
      idx: idx,
      model: this.data[idx],
      data: this.data
    });
  }

  toggle(idx) {
    let data = this.data[idx];
    data.BlogCategoryDisabled = !data.BlogCategoryDisabled;

    const self = this;
    this.dao.postJSON(API.getAPI("category/set"), data).subscribe(
      ret => {
        self.rest.checkCode(ret, (retBody) => {
          self.tablelet.addData(TableletService.BLOG_CATEGORY, idx, retBody);
        });
      },
      err => {
        DaoUtil.logError(err);
      }
    );
  }

}