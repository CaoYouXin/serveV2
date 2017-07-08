import {Component, Input, OnInit} from "@angular/core";
import {RouteService} from "../service/index";

const falsePromise = new Promise<boolean>((resolve, reject) => {
  resolve(false);
});

@Component({
  selector: 'route',
  templateUrl: './route.component.html',
  styleUrls: ['./route.component.css']
})
export class AppRoutingComponent implements OnInit {

  @Input() r: any;

  isShow: boolean = false;
  isAnimated: Promise<boolean> = falsePromise;

  width: string;
  height: string;
  left: string;
  top: string;

  constructor(private service: RouteService) {
  }

  ngOnInit() {
    // this.isSelected = false;
    this.service.getRoute().subscribe(
      msg => {
        let equals = this.r.path === msg.url.substr(1);

        if (equals) {
          this.isSelected(msg.seleted);
        } else {
          this.isSelected(false);
        }
      }
    );
  }

  awesome(e: MouseEvent | any): void {
    let v1 = e.offsetX * e.offsetX;
    let v2 = (e.target['offsetHeight'] - e.offsetY) * (e.target['offsetHeight'] - e.offsetY);
    let v3 = (e.target['offsetWidth'] - e.offsetX) * (e.target['offsetWidth'] - e.offsetX);
    let v4 = e.offsetY * e.offsetY;
    let radius = Math.sqrt(Math.max(v1 + v4, v1 + v2, v3 + v4, v3 + v2));

    this.isShow = true;
    this.width = this.height = 2 * radius + 'px';
    this.left = e.offsetX - radius + 'px';
    this.top = e.offsetY - radius + 'px';

    this.isAnimated = new Promise<boolean>((resolve, reject) => {
      setTimeout(() => {
        resolve(true);
      }, 100);
    });
  }

  @Input() isSelected(s: boolean) {
    if (!s) {
      this.isShow = s;
      this.isAnimated = falsePromise;
    } else if (!this.isShow) {
      this.awesome({
        offsetX: 0,
        offsetY: 0,
        target: {
          offsetHeight: 100,
          offsetWidth: 360
        }
      });
    }
  }
}
