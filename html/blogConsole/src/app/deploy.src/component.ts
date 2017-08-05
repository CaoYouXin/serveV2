import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { FileService } from "../service/index";
import { DaoUtil, RestCode } from "../http";

@Component({
  selector: 'deploy-src',
  templateUrl: './component.html',
  styleUrls: ['../common-style/resource.component.css', './component.css']
})
export class DeploySrcComponent {

  @Input()
  root: string;

  @Output()
  handler: EventEmitter<any> = new EventEmitter();

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

  append(e, file) {
    if (!e.metaKey) {
      return;
    }

    file.selected = !file.selected;
    this.handler.emit({
      selected: file.selected,
      filepath: this.path + file.Name
    });
  }

  fetch(append) {
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

        self.handler.emit({clear: true});
      }),
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

}