import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { FileService } from "../service/index";
import { DaoUtil, RestCode } from "../http";

@Component({
  selector: 'deploy-dst',
  templateUrl: './component.html',
  styleUrls: ['../common-style/resource.component.css', './component.css']
})
export class DeployDstComponent {

  LOCK_MODE = "LOCK_MODE";
  CAN_LOCK_MODE = "CAN_LOCK_MODE";
  FREE_MODE = "FREE_MODE";

  mode: string;

  @Input()
  root: string;

  @Output()
  handler: EventEmitter<string> = new EventEmitter();

  path: string = "";
  clicked: string = "";
  loading: boolean = false;

  files: Array<any> = [];

  constructor(private service: FileService,
    private rest: RestCode) {
  }

  ngOnInit() {
    this.fetch(this.root);
  }

  addDir(e) {
    if (!e.altKey) {
      return;
    }

    let input = prompt("请输入路径");
    if (!input) {
      return;
    }

    const self = this;
    this.service.create(this.path + input).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        self.fetch('.');
      }),
      err => DaoUtil.logError(err)
    );
  }

  append(e, file) {
    if (this.LOCK_MODE === this.mode) {
      return;
    }

    if (e.altKey) {
      return;
    }

    if (e.metaKey) {
      let confirmed = confirm("是否删除该文件(夹)？");

      if (confirmed) {
        const self = this;
        this.service.delete(this.path + file.Name).subscribe(
          ret => this.rest.checkCode(ret, (retBody) => {
            self.fetch('.');
          }),
          err => DaoUtil.logError(err)
        );
      }

      return;
    }

    if (file.Dir) {
      this.clicked = file.Name + '/';
      this.mode = this.CAN_LOCK_MODE;
    } else {
      this.clicked = file.Name;
      this.mode = this.FREE_MODE;
    }
  }

  fetch(append) {
    if (this.LOCK_MODE === this.mode) {
      return;
    }

    if (!append) {
      return;
    }

    if ('..' === append) {
      this.path = this.path.substr(0, this.path.lastIndexOf("/", this.path.length - 2)).concat("/");
    } else if ('.' === append) {
      // current path
    } else {
      this.path += append;
    }
    this.clicked = "";

    this.loading = true;
    const self = this;
    this.service.list(this.path).subscribe(
      ret => this.rest.checkCode(ret, ret => {
        self.loading = false;
        self.files = ret;

        self.mode = self.CAN_LOCK_MODE;
      }),
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

  lock() {
    if (this.CAN_LOCK_MODE === this.mode) {
      this.mode = this.LOCK_MODE;

      this.handler.emit(this.path + this.clicked);
      return;
    }

    if (this.LOCK_MODE === this.mode) {
      this.mode = this.CAN_LOCK_MODE;

      this.fetch('.');
      return;
    }
  }

}