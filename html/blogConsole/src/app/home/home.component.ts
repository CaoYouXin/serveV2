import {Component, HostBinding} from "@angular/core";
import {slideInUpAnimation} from "../animation/route.animation";

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  animations: [slideInUpAnimation]
})
export class HomeComponent {

  @HostBinding('@routeAnimation') routeAnimation = true;
  @HostBinding('style.display') display = 'block';
  @HostBinding('style.position') position = 'absolute';
  @HostBinding('style.width') width = '80%';

}
