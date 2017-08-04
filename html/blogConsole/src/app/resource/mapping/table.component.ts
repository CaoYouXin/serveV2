import { Component, OnInit, OnDestroy, HostBinding } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { RouteService, TableletService } from "../../service";
import { slideInUpAnimation } from "../../animation/route.animation";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'resource-mapping',
  templateUrl: './table.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', './table.component.css'],
  animations: [slideInUpAnimation]
})
export class ResourceMappingComponent implements OnInit, OnDestroy {

  @HostBinding('@routeAnimation') routeAnimation = true;
  @HostBinding('style.display') display = 'block';
  @HostBinding('style.position') position = 'absolute';
  @HostBinding('style.width') width = '100%';

  tableDef: any = {
    heads: [
      { key: "ResourceLevelMappingId", text: "ID", width: 50 },
      { key: "ResourceLevelName", text: "资源等级名称", width: 150 },
      { key: "ResourceUrlPrefix", text: "资源文件路径头", width: 350 },
      { key: "ResourceLevelMappingDisabled", text: "状态", width: 100, render: (disabled) => {
        return disabled ? '禁用' : '启用';
      } }
    ],
    ctrls: [
      { text: (idx) => '编辑', handler: this.edit.bind(this) },
      { text: (idx) => this.data[idx].ResourceLevelMappingDisabled ? '启用' : '禁用', handler: this.toggle.bind(this) }
    ],
    ctrlsWidth: 120
  };

  data: Array<any> = [];

  constructor(private route: ActivatedRoute,
    private routeService: RouteService,
    private tablelet: TableletService,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    this.routeService.setCurrentRouteConfig({
      init: true,
      route: this.route.routeConfig
    });

    const self = this;
    this.tablelet.getData(TableletService.RESOURCE_MAPPING).subscribe(
      data => self.data = data
    );

    this.dao.getJSON(API.getAPI("resource-mapping/list")).subscribe(
      ret => this.restCode.checkCode(ret,
        (retBody) => self.tablelet.setData(TableletService.RESOURCE_MAPPING, retBody)
      ),
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
    this.tablelet.setMaskStatus(TableletService.RESOURCE_MAPPING, {
      mask: true,
      submitText: '添加'
    });
  }

  edit(idx) {
    this.tablelet.setMaskStatus(TableletService.RESOURCE_MAPPING, {
      idx: idx,
      model: this.data[idx],
      mask: true,
      submitText: '保存'
    });
  }

  toggle(idx) {
    this.data[idx].ResourceLevelMappingDisabled = !this.data[idx].ResourceLevelMappingDisabled;

    const self = this;
    this.dao.postJSON(API.getAPI("resource-level-mapping/set"), this.data[idx]).subscribe(
      ret => {
        self.restCode.checkCode(ret, (retBody) => {
          self.tablelet.addData(TableletService.RESOURCE_MAPPING, idx, retBody);
        });
      },
      err => {
        DaoUtil.logError(err);
      }
    );
  }

}