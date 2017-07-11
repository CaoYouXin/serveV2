import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {DatabaseService} from "../service/index";
import {DaoUtil} from "caols-common-modules";

@Component({
  selector: 'db-setting',
  templateUrl: './db.component.html',
  styleUrls: ['../form/form.component.css', './db.component.css']
})
export class DBSettingComponent implements OnInit {

  notLogin: boolean;
  loading: boolean = false;
  serverToken: string;
  schemaName: string;

  currentSchema: string;
  schemas: string[] = [];

  formErrors: any = {
    token: '',
    schema: ''
  };

  constructor(private route: ActivatedRoute,
              private service: DatabaseService) {
  }

  ngOnInit() {
    const self = this;
    this.route.data.subscribe(
      (data: { isLogin: boolean, status: any }) => {
        self.notLogin = !data.isLogin;
        self.schemas = data.status['Configs'] || [];
        self.currentSchema = data.status['ActiveConfig'] || '';
      }
    );
  }

  initSchema() {
    this.reInitSchema(null, this.schemaName);
  }

  reInitSchema(e, schema: string) {
    if (e) {
      e.preventDefault();
    }

    this.formErrors = {
      token: '',
      schema: ''
    };

    if (this.notLogin && (!this.serverToken || this.serverToken.length !== 32)) {
      this.formErrors.token = '不正确的Token';
      return;
    }

    if (!schema) {
      this.formErrors.schema = 'schema不能为空';
      return;
    }

    let header;
    if (this.notLogin) {
      header = {
        "infinitely-serve-token": this.serverToken
      };
    }

    const self = this;
    this.loading = true;
    this.service.init(schema, header)
      .subscribe(
        ret => {
          self.loading = false;
          if (ret) {
            this.currentSchema = schema;

            if (!this.schemas.find(s => s === schema)) {
              this.schemas = [schema, ...this.schemas];
            }

            localStorage.removeItem("currentUser");
            alert("初始化成功！");
          }
        },
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

}
