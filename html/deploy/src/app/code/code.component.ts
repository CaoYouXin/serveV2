import {Component, EventEmitter, Output} from "@angular/core";
import {FileService} from "../service/index";
import {DaoUtil} from "caols-common-modules";
import {RestCode} from "../const/index";

@Component({
  selector: 'code-res',
  templateUrl: './code.component.html',
  styleUrls: ['./code.component.css']
})
export class CodeComponent {

  @Output()
  change: EventEmitter<string> = new EventEmitter();

  root: string = "classpath/";

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
        ret => this.rest.checkCode(ret, (ret) => {
          self.loading = false;
          self.files = ret;
        }),
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

  fileClicked(fileName) {
    this.clicked = fileName;
    this.change.emit(this.path.substr("classpath/".length) + this.clicked);
  }

}
