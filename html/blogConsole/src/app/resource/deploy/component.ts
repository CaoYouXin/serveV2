import { Component, OnInit, OnDestroy, HostBinding, ElementRef, ViewChild } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { slideInUpAnimation } from "../../animation/route.animation";
import { RouteService, FileService } from "../../service";
import { DaoUtil, RestCode } from "../../http";
import { API } from "../../const";

@Component({
  selector: 'resource-deploy',
  templateUrl: './component.html',
  styleUrls: ['./component.css'],
  animations: [slideInUpAnimation]
})
export class ResourceDeployComponent {

  @HostBinding("@routeAnimation") animation = true;
  @HostBinding("style.display") display = 'block';
  @HostBinding("style.position") position = 'absolute';
  @HostBinding("style.width") width = '100%';

  dstFileRoot: string = "serve/";

  dstFile: string;

  constructor(private route: ActivatedRoute,
    private routeService: RouteService,
    private fileService: FileService,
    private rest: RestCode) { }

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

  dstChange(crtDst) {
    this.dstFile = crtDst;
    console.log(this.dstFile);
  }

}