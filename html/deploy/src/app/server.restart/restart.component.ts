import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {ServerService} from "../service/index";
import {DaoUtil} from "caols-common-modules";
import {RestCode} from "../const/index";

@Component({
  selector: 'restart',
  templateUrl: './restart.component.html',
  styleUrls: ['./restart.component.css']
})
export class RestartComponent {
  processLong: string = '15s';
  process: number = 0;
  processGo: Promise<boolean> = Promise.resolve(false);

  constructor(private router: Router,
              private service: ServerService,
              private rest: RestCode) {
  }

  restartSys() {
    this.service.restart()
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          localStorage.removeItem("currentUser");

          this.process = ret;
          this.processLong = this.process + 's';
          this.processGo = new Promise<boolean>((resolve, reject) => {
            setTimeout(() => {
              resolve(true);
            }, 100);
          });

          const interval = setInterval((self) => {
            self.process -= 1;
            if (!self.process) {
              clearInterval(interval);

              this.router.navigate(['/']);
            }
          }, 1000, this);
        }),
        err => DaoUtil.logError(err)
      );
  }
}
