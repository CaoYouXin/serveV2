import { Component, HostBinding, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { slideInUpAnimation } from "../../animation/route.animation";
import { RouteService, TableletService } from "../../service";
import { DaoUtil, RestCode } from "../../http";
import { API } from "../../const";

@Component({
  selector: 'blog-comment',
  templateUrl: './component.html',
  styleUrls: ['./component.css'],
  animations: [slideInUpAnimation]
})
export class BlogCommentComponent implements OnInit, OnDestroy {

  @HostBinding("@routeAnimation") animation = true;
  @HostBinding("style.display") display = "block";
  @HostBinding("style.position") position = "absolute";
  @HostBinding("style.width") width = "100%";

  posts: Array<any> = [];

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
    this.dao.getJSON(API.getAPI("post/list")).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        retBody.forEach(p => p.buttonMode = true);
        self.posts = retBody;
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

  clicked(p) {
    if (!p.buttonMode) {
      p.buttonMode = true;
    } else {
      this.posts.forEach(post => post.buttonMode = true);
      p.buttonMode = false;
    }
  }

}