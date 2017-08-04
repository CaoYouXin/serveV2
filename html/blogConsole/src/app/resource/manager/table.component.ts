import { Component, OnInit, OnDestroy, HostBinding } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { TableletService, RouteService } from "../../service/index";
import { slideInUpAnimation } from "../../animation/route.animation";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'resource-manager',
  templateUrl: './table.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', './table.component.css'],
  animations: [slideInUpAnimation]
})
export class ResourceManagerComponent implements OnInit, OnDestroy {

  @HostBinding('@routeAnimation') routeAnimation = true;
  @HostBinding('style.display') display = 'block';
  @HostBinding('style.position') position = 'absolute';
  @HostBinding('style.width') width = '100%';

  tableDef: any = {
    heads: [
      { key: "ResourceLevelId", text: "ID", width: 50 },
      { key: "ResourceLevelName", text: "资源等级名称", width: 200 },
      { key: "ResourceLevelExpMsg", text: "资源等级例外说明", width: 450 }
    ],
    ctrls: [
      { text: (idx) => '编辑', handler: this.edit.bind(this) }
    ],
    ctrlsWidth: 50
  };

  data: Array<any> = [];

  constructor(private tablelet: TableletService,
    private routeService: RouteService,
    private route: ActivatedRoute,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    this.routeService.setCurrentRouteConfig({
      init: true,
      route: this.route.routeConfig
    });

    const self = this;
    this.tablelet.getData(TableletService.RESOURCE_MANAGER).subscribe(
      data => self.data = data
    );

    this.dao.getJSON(API.getAPI("resource-level/list"))
      .subscribe(
        ret => this.restCode.checkCode(ret, (retBody) => {
          self.tablelet.setData(TableletService.RESOURCE_MANAGER, retBody);
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
    this.tablelet.setMaskStatus(TableletService.RESOURCE_MANAGER, {
      mask: true,
      submitText: '添加'
    });
  }

  edit(idx) {
    this.tablelet.setMaskStatus(TableletService.RESOURCE_MANAGER, {
      idx: idx,
      model: this.data[idx],
      mask: true,
      submitText: '保存'
    });
  }

}