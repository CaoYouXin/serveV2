import { Component, OnInit, OnDestroy, HostBinding } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ResourceManangerService, RouteService } from "../../service/index";
import { slideInUpAnimation } from "../../animation/route.animation";

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
      { key: "ControllerId", text: "ID", width: 50 },
      { key: "ControllerName", text: "名称", width: 200 },
      { key: "ControllerClassName", text: "类路径", width: 300 },
      {
        key: "ControllerDisabled", text: "状态", width: 100, render: (disabled) => {
          return disabled ? "禁用" : "启用";
        }
      },
    ],
    ctrls: [],
    ctrlsWidth: 130
  };

  data: Array<any> = [];

  constructor(private service: ResourceManangerService,
    private routeService: RouteService,
    private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.routeService.setCurrentRouteConfig({
      init: true,
      route: this.route.routeConfig
    });
  }

  ngOnDestroy() {
    this.routeService.setCurrentRouteConfig({
      init: false,
      route: this.route.routeConfig
    });
  }

  toAdd() {
    this.service.setMaskStatus(true);
  }

}