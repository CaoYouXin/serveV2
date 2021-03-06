import { Component, HostBinding, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { slideInUpAnimation } from "../animation/route.animation";
import { RouteService, SoundService } from "../service/index";

@Component({
  selector: 'tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.css'],
  animations: [slideInUpAnimation]
})
export class TabsComponent implements OnInit {

  @HostBinding('@routeAnimation') routeAnimation = true;
  @HostBinding('style.display') display = 'block';
  @HostBinding('style.position') position = 'absolute';
  @HostBinding('style.width') width = '80%';

  children: any;
  selectedR: any;

  constructor(private route: ActivatedRoute,
    private service: RouteService,
    private router: Router,
    private soundService: SoundService) {
  }

  ngOnInit() {
    this.children = this.route.routeConfig.children;
    this.selectedR = null;

    const self = this;
    this.service.getCurrentRouteConfig().subscribe(
      msg => {
        // if (msg.init && null !== self.selectedR) {
        //   return;
        // }

        let eqauls = false;
        let selected = null;
        self.children.forEach(router => {
          if (router === msg.route) {
            eqauls = true;
            selected = router;
          }
        });

        if (eqauls) {
          if (msg.init) {
            setTimeout((self) => { self.selectedR = selected; }, 0, self);
          } else {
            setTimeout((self) => { self.selectedR = null; }, 0, self);
          }
        }
        // self.selectedR = routeConfig;
      }
    );

    setTimeout((self) => {
      if (null === self.selectedR && self.children && self.children.length) {
        self.selectedR = self.children[0];
        self.router.navigate([self.route.routeConfig.path, self.children[0].path]);
      }
    }, 1000, this);
  }

  clicked() {
    this.soundService.addCmd(SoundService.SEC_ROUTE_CLICK);
  }

}
