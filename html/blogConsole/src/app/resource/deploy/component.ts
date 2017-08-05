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

  srcFileRoot: string = "upload/ready2deploy/";
  dstFileRoot: string = "serve/";

  srcFiles: Array<string> = [];
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

  srcChange(msg) {
    if (msg.clear) {
      this.srcFiles = [];
      return;
    }

    if (msg.selected) {
      this.srcFiles = [msg.filepath, ...this.srcFiles];
    } else {
      let idx = this.srcFiles.findIndex((src) => src === msg.filepath);
      this.srcFiles = [...this.srcFiles.slice(0, idx), ...this.srcFiles.slice(idx + 1)];
    }
  }

  dstChange(crtDst) {
    this.dstFile = crtDst;
  }

  deploy() {
    if (this.srcFiles.length <= 0) {
      alert('请选择要部署的文件。');
      return;
    }

    if (!this.dstFile) {
      alert('请锁定目标文件夹。');
      return;
    }

    this.fileService.copy(this.srcFiles, this.dstFile).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        alert(retBody);
      }),
      err => DaoUtil.logError(err)
    );
  }

}