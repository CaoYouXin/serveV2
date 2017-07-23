import {Component} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {ServiceService} from "../service/index";
import {RestCode} from "../const/index";

@Component({
  selector: 'micro-service',
  templateUrl: './microservice.component.html',
  styleUrls: ['./microservice.component.css']
})
export class MicroServiceComponent {

  private intfClassName: string;
  private implClassName: string;

  mask: boolean = false;
  loading: boolean = false;

  tableDef: any = {
    heads: [
      {key: "ServiceId", text: "ID", width: 50},
      {key: "ServiceIntfClassName", text: "接口类名称", width: 300},
      {key: "ServiceImplClassName", text: "实现类路径", width: 300},
      {key: "ServiceDisabled", text: "禁用", width: 100},
    ],
    ctrls: [
      {text: (idx) => 'Reload', handler: this.reload.bind(this)},
      {text: (idx) => this.data[idx]["ServiceDisabled"] ? "启用" : "禁用", handler: this.toggle.bind(this)}
    ],
    ctrlsWidth: 130
  };

  data: Array<any> = [];

  constructor(private service: ServiceService,
              private rest: RestCode) {
  }

  ngOnInit() {
    const self = this;
    this.service.list()
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          self.data = ret;
        }),
        err => DaoUtil.logError(err)
      );
  }

  add() {
    if (!/\.class$/.test(this.intfClassName) || !/\.class$/.test(this.implClassName)) {
      alert("需要添加Class.");
      return;
    }

    const self = this;
    this.loading = true;
    let intfClassName = this.intfClassName.replace(/\//g, '.').replace(".class", "");
    let implClassName = this.implClassName.replace(/\//g, '.').replace(".class", "");
    this.service.set(intfClassName, implClassName)
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          self.loading = false;
          self.mask = false;
          if (!self.data.find(d => d.ServiceId === ret.ServiceId)) {
            self.data = [ret, ...self.data];
          } else {
            let index = self.data.findIndex(d => d.ServiceId === ret.ServiceId);
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

  changeIntf(className) {
    this.intfClassName = className;
  }

  changeImpl(className) {
    this.implClassName = className;
  }

  reload(idx) {
    if (this.loading) {
      return;
    }

    const self = this;
    this.loading = true;
    this.service.set(this.data[idx].ServiceIntfClassName, this.data[idx].ServiceImplClassName)
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          self.loading = false;
          let index = self.data.findIndex(d => d.ServiceId === ret.ServiceId);
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
    this.service.disable(this.data[idx].ServiceId, !this.data[idx].ServiceDisabled)
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          self.loading = false;
          self.data[idx].ServiceDisabled = ret;
        }),
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

}
