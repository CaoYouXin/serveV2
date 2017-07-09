import {Component, OnInit} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {FileService} from "../service/index";

@Component({
  selector: 'upload-code',
  templateUrl: './upload.code.component.html',
  styleUrls: ['./upload.code.component.css']
})
export class UploadCodeComponent implements OnInit {

  root: string = "upload/code/";

  path: string = "";
  clicked: string = "";
  loading: boolean = false;

  files: Array<any> = [];

  constructor(private service: FileService) {
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
        ret => {
          self.loading = false;
          self.files = ret;
        },
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

  deploy(filePath) {
    if (!filePath.match(/\.zip$/)) {
      alert("需要部署.zip文件.");
      return;
    }

    this.loading = true;
    const self = this;
    this.service.unzip(filePath, "classpath/")
      .subscribe(
        ret => {
          self.loading = false;
          alert(ret);
        },
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }
}
