import {Component, OnInit, HostBinding} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {slideInDownAnimation} from "../animation/route.animation";

@Component({
  selector: 'tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.css'],
  animations: [ slideInDownAnimation ]
})
export class TabsComponent implements OnInit {

  @HostBinding('@routeAnimation') routeAnimation = true;
  @HostBinding('style.display')   display = 'block';
  @HostBinding('style.position')  position = 'absolute';
  @HostBinding('style.width')  width = '80%';

  children: any;
  selectedR: any;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit () {
    this.children = this.route.routeConfig.children;
  }

}
