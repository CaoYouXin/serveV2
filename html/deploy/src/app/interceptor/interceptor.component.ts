import {Component} from "@angular/core";
import {InterceptorService} from "../service/index";
import {RestCode} from "../const/index";
import {DaoUtil} from "caols-common-modules";

@Component({
  selector: 'interceptor',
  templateUrl: './interceptor.component.html',
  styleUrls: ['./interceptor.component.css']
})
export class InterceptorComponent {
  private className: string;

  mask: boolean = false;
  loading: boolean = false;

  tableDef: any = {
    heads: [
      {key: "InterceptorId", text: "ID", width: 50},
      {key: "PostInterceptor", text: "服务后", width: 100},
      {key: "InterceptorName", text: "名称", width: 200},
      {key: "InterceptorClassName", text: "类路径", width: 300},
      {key: "InterceptorDisabled", text: "禁用", width: 100},
    ],
    ctrls: [
      {text: (idx) => 'Reload', handler: this.reload.bind(this)},
      {text: (idx) => this.data[idx]["InterceptorDisabled"] ? "启用" : "禁用", handler: this.toggle.bind(this)}
    ],
    ctrlsWidth: 130
  };

  data: Array<any> = [];

  constructor(private service: InterceptorService,
              private rest: RestCode) {
  }

  ngOnInit() {
    const self = this;
    this.service.list()
      .subscribe(
        ret => this.rest.checkCode(ret, (ret) => {
          self.data = ret;
        }),
        err => DaoUtil.logError(err)
      );
  }

  add() {
    if (!/\.class$/.test(this.className)) {
      alert("需要添加Class.");
      return;
    }

    const self = this;
    this.loading = true;
    let className = this.className.replace(/\//g, '.').replace(".class", "");
    this.service.set(className)
      .subscribe(
        ret => this.rest.checkCode(ret, (ret) => {
          self.loading = false;
          self.mask = false;
          if (!self.data.find(d => d.ControllerId === ret.ControllerId)) {
            self.data = [ret, ...self.data];
          } else {
            let index = self.data.findIndex(d => d.ControllerId === ret.ControllerId);
            self.data = [
              ...self.data.slice(0, index),
              ret,
              ...self.data.slice(index + 1)
            ];
          }
        }),
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

  change(className) {
    this.className = className;
  }

  reload(idx) {
    if (this.loading) {
      return;
    }

    const self = this;
    this.loading = true;
    this.service.set(this.data[idx].ControllerClassName)
      .subscribe(
        ret => this.rest.checkCode(ret, (ret) => {
          self.loading = false;
          let index = self.data.findIndex(d => d.ControllerId === ret.ControllerId);
          self.data = [
            ...self.data.slice(0, index),
            ret,
            ...self.data.slice(index + 1)
          ];
        }),
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

  toggle(idx) {
    if (this.loading) {
      return;
    }

    const self = this;
    this.loading = true;
    this.service.setDisabled(this.data[idx].ControllerId, !this.data[idx].ControllerDisabled)
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          self.loading = false;
          self.data[idx].ControllerDisabled = ret;
        }),
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }
}
