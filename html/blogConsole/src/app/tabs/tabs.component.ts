import {Component, HostBinding, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
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
              private service: RouteService,
              private router: Router) {
  }

  ngOnInit() {
    this.children = this.route.routeConfig.children;
    
    if (this.children && this.children.length) {
      this.selectedR = this.children[0];
      this.router.navigate([this.route.routeConfig.path, this.children[0].path]);
    }

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
