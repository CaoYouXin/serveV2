import { Component, HostBinding, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { slideInUpAnimation } from "../../animation/route.animation";
import { RouteService, TableletService } from "../../service";
import { DaoUtil, RestCode } from "../../http";
import { API } from "../../const";

@Component({
  selector: 'blog-post',
  templateUrl: './table.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', './table.component.css'],
  animations: [slideInUpAnimation]
})
export class BlogPostComponent implements OnInit, OnDestroy {

  @HostBinding("@routeAnimation") animation = true;
  @HostBinding("style.display") display = "block";
  @HostBinding("style.position") position = "absolute";
  @HostBinding("style.width") width = "100%";

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
      { text: (idx) => "编辑", handler: this.edit.bind(this) },
      { text: (idx) => this.data[idx].BlogCategoryDisabled ? "启用" : "禁用", handler: this.toggle.bind(this) },
      { text: (idx) => "截图", handler: this.screenshot.bind(this) }
    ],
    ctrlsWidth: 150
  };

  data: Array<any> = [];

  categoryArray: Array<any> = [];
  categories: any = {};

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
    this.tablelet.getData(TableletService.BLOG_POST).subscribe(
      ret => {
        ret.forEach(element => {
          if (element.BlogCategoryName) {
            return;
          }

          element.BlogCategoryName = self.categories[element.BlogCategoryId + ''].BlogCategoryName;
        });
        self.data = ret;
      },
      err => DaoUtil.logError(err)
    );

    this.dao.getJSON(API.getAPI("category/list")).subscribe(
      ret => self.rest.checkCode(ret, (retBody) => {
        self.categoryArray = retBody;
        retBody.forEach(c => {
          self.categories[c.BlogCategoryId + ''] = c;
        });

        self.dao.getJSON(API.getAPI("post/list")).subscribe(
          ret => self.rest.checkCode(ret, (retBody) => {
            self.tablelet.setData(TableletService.BLOG_POST, retBody);
          }),
          err => DaoUtil.logError(err)
        );
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
    this.tablelet.setMaskStatus(TableletService.BLOG_POST, {
      maskName: 'mask',
      mask: true,
      submitText: '添加',
      categories: this.categoryArray
    });
  }

  edit(idx) {
    this.tablelet.setMaskStatus(TableletService.BLOG_POST, {
      maskName: 'mask',
      mask: true,
      submitText: '保存',
      idx: idx,
      model: this.data[idx],
      categories: this.categoryArray
    });
  }

  toggle(idx) {
    let data = this.data[idx];
    data.BlogPostDisabled = !data.BlogPostDisabled;

    const self = this;
    this.dao.postJSON(API.getAPI("post/set"), data).subscribe(
      ret => {
        self.rest.checkCode(ret, (retBody) => {
          self.tablelet.addData(TableletService.BLOG_POST, idx, retBody);
        });
      },
      err => {
        DaoUtil.logError(err);
      }
    );
  }

  screenshot(idx) {
    this.tablelet.setMaskStatus(TableletService.BLOG_POST, {
      maskName: 'screenshot',
      mask: true,
      postId: this.data[idx].BlogPostId
    });
  }

}