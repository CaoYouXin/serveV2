import {Component, HostBinding, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {slideInDownAnimation} from "../animation/route.animation";
import {RouteService} from "../service/index";

@Component({
  selector: 'tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.css'],
  animations: [slideInDownAnimation]
})
export class TabsComponent implements OnInit {

  @HostBinding('@routeAnimation') routeAnimation = true;
  @HostBinding('style.display') display = 'block';
  @HostBinding('style.position') position = 'absolute';
  @HostBinding('style.width') width = '80%';

  children: any;
  selectedR: any;

  constructor(private route: ActivatedRoute,
              private service: RouteService) {
  }

  ngOnInit() {
    this.children = this.route.routeConfig.children;

    // const self = this;
    // this.service.getRoute().subscribe(
    //   msg => {
    //     let root = self.route.routeConfig.path;
    //     let path = msg.url.substr(1);
    //
    //     self.children.forEach(router => {
    //       let equals = path.startsWith(root + '/' + router.path);
    //
    //       if (equals && msg.seleted) {
    //         self.selectedR = router;
    //       } else if (self.selectedR === router) {
    //         self.selectedR = null;
    //       }
    //     });
    //   }
    // );
  }

}
