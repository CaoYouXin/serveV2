import { Component, HostBinding, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { slideInUpAnimation } from "../../animation/route.animation";
import { RouteService, TableletService } from "../../service";
import { DaoUtil, RestCode } from "../../http";
import { API } from "../../const";

@Component({
  selector: 'favour-mapping',
  templateUrl: './table.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', './table.component.css'],
  animations: [slideInUpAnimation]
})
export class FavourMappingComponent implements OnInit, OnDestroy {

  @HostBinding("@routeAnimation") animation = true;
  @HostBinding("style.display") display = "block";
  @HostBinding("style.position") position = "absolute";
  @HostBinding("style.width") width = "100%";

  tableDef: any = {
    heads: [
      { key: "UserFavourMappingId", text: "ID", width: 50 },
      { key: "ResourceLevelName", text: "资源等级", width: 300 },
      { key: "UserFavourThreshold", text: "阈值", width: 300 }
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
    this.tablelet.getData(TableletService.FAVOUR_MAPPING).subscribe(
      ret => self.data = ret,
      err => DaoUtil.logError(err)
    );

    this.dao.getJSON(API.getAPI("favour-mapping/list")).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        self.tablelet.setData(TableletService.FAVOUR_MAPPING, retBody);
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
    this.tablelet.setMaskStatus(TableletService.FAVOUR_MAPPING, {
      mask: true,
      submitText: '添加'
    });
  }

  edit(idx) {
    this.tablelet.setMaskStatus(TableletService.FAVOUR_MAPPING, {
      mask: true,
      submitText: '保存',
      idx: idx,
      model: this.data[idx]
    });
  }

}