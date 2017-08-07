import { Component, OnInit, ViewChild, ElementRef } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService, UploadService, FileService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";
import { Md5 } from "ts-md5/dist/md5";

@Component({
  selector: 'blog-screenshot-mask',
  templateUrl: './screenshot.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', '../../common-style/form.component.css', './screenshot.component.css']
})
export class BlogScreenshotComponent implements OnInit {

  private postId: number;

  prefix: string = API.getAPI('serverLoc');

  mask: boolean;
  model: any = {};
  loading: boolean;

  screenshots: Array<any> = [];

  codeForm: FormGroup;

  @ViewChild("f")
  currentForm: ElementRef;

  constructor(private tablelet: TableletService,
    private uploadService: UploadService,
    private fileService: FileService,
    private fb: FormBuilder,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    const self = this;
    this.tablelet.getMaskStatus(TableletService.BLOG_POST).subscribe(
      msg => {
        if ('screenshot' !== msg.maskName) {
          return;
        }

        self.postId = msg.postId;
        self.mask = msg.mask;

        if (!self.mask) {
          return;
        }

        self.loading = true;
        self.dao.getJSON(API.getAPI("screenshot/list")(msg.postId)).subscribe(
          ret => {
            self.loading = false;
            self.restCode.checkCode(ret, (retBody) => {
              self.screenshots = retBody;
            });
          },
          err => {
            self.loading = false;
            DaoUtil.logError(err);
          }
        );

        self.buildForm();
      }
    );
  }

  buildForm(): void {
    this.codeForm = this.fb.group({
      'filename': [this.model.filename, Validators.required]
    });

    this.codeForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.codeForm) {
      return;
    }
    const form = this.codeForm;

    for (const field in this.formErrors) {
      // clear previous error message (if any)
      this.formErrors[field] = '';
      const control = form.get(field);

      if (control && control.dirty && !control.valid) {
        const messages = this.validationMessages[field];
        for (const key in control.errors) {
          this.formErrors[field] += messages[key] + ' ';
        }
      }
    }
  }

  formErrors = {
    'filename': ''
  };

  validationMessages = {
    'filename': {
      'required': '文件名是必填项.'
    }
  };

  upload() {
    this.onValueChanged();
    this.loading = true;

    const date = new Date;
    const self = this;
    const md5 = Md5.hashStr(new Date() + '');
    this.uploadService.send(this.currentForm.nativeElement,
      API.getAPI("upload/code")("/tmp/" + md5 + '/'),
      (responseText, filename) => {
        let ret = JSON.parse(responseText);
        self.formErrors.filename = ret.body;

        const serveDir = 'serve/screenshot/' + md5 + '/';
        self.fileService.copy(['upload/tmp/' + md5 + '/' + filename], serveDir).subscribe(
          ret => self.restCode.checkCode(ret, (retBody) => {
            self.formErrors.filename = retBody;

            self.dao.postJSON(API.getAPI("screenshot/set"), {
              BlogPostId: self.postId,
              ScreenshotUrl: serveDir + filename,
              ScreenshotDisabled: false
            }).subscribe(
              ret => self.restCode.checkCode(ret, (retBody) => {
                self.screenshots.push(retBody);

                self.loading = false;
              }),
              err => DaoUtil.logError(err)
              );
          }),
          err => DaoUtil.logError(err)
        );
      });
  }

  delete(screenshot) {
    this.loading = true;
    screenshot.ScreenshotDisabled = true;

    const self = this;
    this.dao.postJSON(API.getAPI("screenshot/set"), screenshot).subscribe(
      ret => self.restCode.checkCode(ret, (retBody) => {
        self.loading = false;

        if (!retBody.ScreenshotDisabled) {
          return;
        }
        
        let idx = self.screenshots.findIndex(s => s.ScreenshotId + '' === retBody.ScreenshotId + '');

        self.screenshots = [
          ...self.screenshots.slice(0, idx),
          ...self.screenshots.slice(idx + 1)
        ];
      }),
      err => DaoUtil.logError(err)
    );
  }

  cancel() {
    this.tablelet.setMaskStatus(TableletService.BLOG_POST, { maskName: 'screenshot', mask: false });
    this.loading = false;
  }

}