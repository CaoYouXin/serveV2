import { Component, HostBinding, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { slideInUpAnimation } from "../../animation/route.animation";
import { RouteService, TableletService } from "../../service";
import { DaoUtil, RestCode } from "../../http";
import { API } from "../../const";

@Component({
  selector: 'favour-detail',
  templateUrl: './table.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', './table.component.css'],
  animations: [slideInUpAnimation]
})
export class FavourDetailComponent implements OnInit, OnDestroy {

  @HostBinding("@routeAnimation") animation = true;
  @HostBinding("style.display") display = "block";
  @HostBinding("style.position") position = "absolute";
  @HostBinding("style.width") width = "100%";

  tableDef: any = {
    heads: [
      { key: "UserFavourId", text: "ID", width: 50 },
      { key: "UserFavourValue", text: "好感度", width: 100 },
      { key: "UserName", text: "用户名", width: 100 },
      { key: "UserNickName", text: "昵称", width: 100 },
      { key: "UserPhone", text: "手机号", width: 100 },
      {
        key: "UserSex", text: "性别", width: 100, render: (byteSex) => {
          return 1 === byteSex ? "男" : 2 === byteSex ? "女" : "未知";
        }
      },
      { key: "UserAge", text: "年龄", width: 100 },
      { key: "UserProfession", text: "职业", width: 100 },
      { key: "UserAvatar", text: "头像", width: 100 },
      {
        key: "UserDisabled", text: "状态", width: 100, render: (disabled) => {
          return disabled ? "禁用" : "可用";
        }
      },
    ],
    ctrls: [
      { text: (idx) => "编辑", handler: this.edit.bind(this) }
    ],
    ctrlsWidth: 50
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
    this.tablelet.getData(TableletService.USER_FAVOUR).subscribe(
      ret => self.data = ret,
      err => DaoUtil.logError(err)
    );

    this.dao.getJSON(API.getAPI("favour/list")).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        self.tablelet.setData(TableletService.USER_FAVOUR, retBody);
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
    this.tablelet.setMaskStatus(TableletService.USER_FAVOUR, {
      mask: true,
      submitText: '添加'
    });
  }

  edit(idx) {
    this.tablelet.setMaskStatus(TableletService.USER_FAVOUR, {
      mask: true,
      submitText: '保存',
      idx: idx,
      model: this.data[idx]
    });
  }

}