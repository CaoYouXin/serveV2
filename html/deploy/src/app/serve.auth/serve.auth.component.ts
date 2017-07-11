import {Component} from "@angular/core";
import {ServeAuthService} from "../service/index";
import {DaoUtil} from "caols-common-modules";

@Component({
  selector: 'serve-auth',
  templateUrl: './serve.auth.component.html',
  styleUrls: ['./serve.auth.component.css']
})
export class ServeAuthComponent {

  codeRoot: string = 'classpath/';
  loading: boolean = false;

  constructor(private service: ServeAuthService) {
  }

  set(filePath) {
    if (this.loading) {
      return;
    }

    if (!filePath.match(/\.class$/)) {
      alert("需要部署.zip文件.");
      return;
    }

    this.loading = true;
    const self = this;
    this.service.set(filePath.substr("classpath/".length).replace(/\//g, ".").replace(".class", ""))
      .subscribe(
        ret => {
          self.loading = false;
          if (ret) {
            alert(ret);
          }
        },
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

  reset() {
    if (this.loading) {
      return;
    }

    this.loading = true;
    const self = this;
    this.service.set("")
      .subscribe(
        ret => {
          self.loading = false;
          if (ret) {
            alert(ret);
          }
        },
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

}
