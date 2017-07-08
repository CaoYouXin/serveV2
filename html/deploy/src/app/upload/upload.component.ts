import {Component, ElementRef, ViewChild} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UploadUtil} from "./upload.util";
import {API} from "../const/api.const";

@Component({
  selector: 'upload',
  templateUrl: './upload.component.html',
  styleUrls: ['../form/form.component.css']
})
export class UploadComponent {

  model: any = {};
  loading: boolean = false;

  codeForm: FormGroup;

  @ViewChild("f")
  currentForm: ElementRef;

  constructor(private fb: FormBuilder,
              private util: UploadUtil) {
  }

  upload(): void {
    this.onValueChanged();
    this.loading = true;

    const date = new Date;
    const self = this;
    this.util.send(this.currentForm.nativeElement,
      API.getAPI("upload/code")("/code/"
        + date.getFullYear() + "/"
        + date.getMonth() + "/"
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

  ngOnInit(): void {
    this.buildForm();
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

}
