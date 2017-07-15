import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {FileService} from "../service/index";
import {RestCode} from "../const/index";

@Component({
  selector: 'upload-code',
  templateUrl: './upload.code.component.html',
  styleUrls: ['./upload.code.component.css']
})
export class UploadCodeComponent implements OnInit {

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

  fetch(append) {
    if (!append) {
      return;
    }
    if ('..' === append) {
      this.path = this.path.substr(0, this.path.lastIndexOf("/", this.path.length - 2)).concat("/");
    } else {
      this.path += append;
    }
    this.clicked = "";

    this.loading = true;
    const self = this;
    this.service.list(this.path)
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          self.loading = false;
          self.files = ret;
        }),
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

}
