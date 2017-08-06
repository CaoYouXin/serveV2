import { Component, HostBinding, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { slideInUpAnimation } from "../../animation/route.animation";
import { RouteService, TableletService } from "../../service";
import { DaoUtil, RestCode } from "../../http";
import { API } from "../../const";

@Component({
  selector: 'favour-rule',
  templateUrl: './table.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', './table.component.css'],
  animations: [slideInUpAnimation]
})
export class FavourRuleComponent implements OnInit, OnDestroy {

  @HostBinding("@routeAnimation") animation = true;
  @HostBinding("style.display") display = "block";
  @HostBinding("style.position") position = "absolute";
  @HostBinding("style.width") width = "100%";

  tableDef: any = {
    heads: [
      { key: "UserFavourRuleId", text: "ID", width: 50 },
      { key: "UserFavourRulePattern", text: "模式", width: 300 },
      { key: "UserFavourRuleScore", text: "增加值", width: 100 },
      { key: "UserFavourRuleLimit", text: "次数上限", width: 100 },
      {
        key: "UserFavourRuleDisabled", text: "状态", width: 100, render: (disabled) => {
          return disabled ? "禁用" : "启用";
        }
      },
    ],
    ctrls: [
      { text: (idx) => "编辑", handler: this.edit.bind(this) },
      { text: (idx) => this.data[idx].UserFavourRuleDisabled ? "启用" : "禁用", handler: this.toggle.bind(this) }
    ],
    ctrlsWidth: 110
  };

  data: Array<any> = [];

  constructor(private route: ActivatedRoute,
    private routeService: RouteService,
    private tablelet: TableletService,
    private rest: RestCode,
    private dao: DaoUtil) {}

  ngOnInit() {
    this.routeService.setCurrentRouteConfig({
      init: true,
      route: this.route.routeConfig
    });

    const self = this;
    this.tablelet.getData(TableletService.FAVOUR_RULE).subscribe(
      ret => self.data = ret,
      err => DaoUtil.logError(err)
    );

    this.dao.getJSON(API.getAPI("favour-rule/list")).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        self.tablelet.setData(TableletService.FAVOUR_RULE, retBody);
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
    this.tablelet.setMaskStatus(TableletService.FAVOUR_RULE, {
      mask: true,
      submitText: '添加'
    });
  }

  edit(idx) {
    this.tablelet.setMaskStatus(TableletService.FAVOUR_RULE, {
      mask: true,
      submitText: '保存',
      idx: idx,
      model: this.data[idx]
    });
  }

  toggle(idx) {
    let data = this.data[idx];
    data.UserFavourRuleDisabled = !data.UserFavourRuleDisabled;

    const self = this;
    this.dao.postJSON(API.getAPI("favour-rule/set"), data).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        self.tablelet.addData(TableletService.FAVOUR_RULE, idx, retBody);
      }),
      err => DaoUtil.logError(err)
    );
  }

}