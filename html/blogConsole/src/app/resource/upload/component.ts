import { Component, OnInit, OnDestroy, HostBinding, ElementRef, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { slideInUpAnimation } from "../../animation/route.animation";
import { RouteService, FileService, UploadService } from "../../service";
import { DaoUtil, RestCode } from "../../http";
import { API } from "../../const";

@Component({
  selector: 'resource-upload',
  templateUrl: './component.html',
  styleUrls: ['../../common-style/form.component.css', './component.css'],
  animations: [slideInUpAnimation]
})
export class ResourceUploadComponent {

  @HostBinding("@routeAnimation") animation = true;
  @HostBinding("style.display") display = 'block';
  @HostBinding("style.position") position = 'absolute';
  @HostBinding("style.width") width = '100%';

  codeRoot: string = "upload/serve-resource/";
  model: any = {};
  loading: boolean = false;

  codeForm: FormGroup;

  @ViewChild("f")
  currentForm: ElementRef;

  constructor(private route: ActivatedRoute,
    private routeService: RouteService,
    private fb: FormBuilder,
    private fileService: FileService,
    private uploadService: UploadService,
    private rest: RestCode) { }

  ngOnInit() {
    this.routeService.setCurrentRouteConfig({
      init: true,
      route: this.route.routeConfig
    });

    this.buildForm();
  }

  ngOnDestroy() {
    this.routeService.setCurrentRouteConfig({
      init: false,
      route: this.route.routeConfig
    });
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

  deploy(filePath) {
    if (this.loading) {
      return;
    }

    if (!filePath.match(/\.zip$/)) {
      alert("需要部署.zip文件.");
      return;
    }

    this.loading = true;
    const self = this;
    this.fileService.unzip(filePath, "upload/ready2deploy/")
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          self.loading = false;
          alert(ret);
        }),
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

  uploadPkg(): void {
    this.onValueChanged();
    this.loading = true;

    const date = new Date;
    const self = this;
    this.uploadService.send(this.currentForm.nativeElement,
      API.getAPI("upload/code")("/serve-resource/"
        + date.getFullYear() + "/"
        + (date.getMonth() + 1) + "/"
        + date.getDate() + "/"
        + date.getHours() + ":"
        + date.getMinutes() + ":"
        + date.getSeconds() + "/"),
      (responseText) => {
        let ret = JSON.parse(responseText);
        self.formErrors.filename = ret.body;
        self.loading = false;
      });
  }

  uploadFile() {
    this.onValueChanged();
    this.loading = true;

    const date = new Date;
    const self = this;
    this.uploadService.send(this.currentForm.nativeElement,
      API.getAPI("upload/code")("/ready2deploy/"),
      (responseText) => {
        let ret = JSON.parse(responseText);
        self.formErrors.filename = ret.body;
        self.loading = false;
      });
  }

}