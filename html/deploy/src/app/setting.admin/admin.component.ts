import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AdminService} from "../service/admin.service";
import {DaoUtil} from "caols-common-modules";
import {Router} from "@angular/router";
import {
  RestCode
} from "../const/index";

@Component({
  selector: 'admin-setting',
  templateUrl: './admin.component.html',
  styleUrls: ['../form/form.component.css']
})
export class AdminSettingComponent implements OnInit {

  model: any = {};
  loading: boolean = false;

  settingForm: FormGroup;

  constructor(private fb: FormBuilder,
              private service: AdminService,
              private router: Router,
              private rest: RestCode) {
  }

  settingAdmin(): void {
    if (!this.settingForm.valid) {
      return;
    }

    this.loading = true;
    const self = this;
    this.service.setting(this.settingForm.value)
      .subscribe(
        ret => this.rest.checkCode(ret, ret => {
          self.loading = false;
          localStorage.removeItem('currentUser');
          this.router.navigate(['/login']);
        }),
        err => {
          self.loading = false;
          DaoUtil.logError(err);
        }
      );
  }

  ngOnInit(): void {
    this.buildForm();
  }

  buildForm(): void {
    this.settingForm = this.fb.group({
      'oldUsername': [this.model.oldUsername],
      'username': [this.model.username, [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(24)
      ]],
      'oldPassword': [this.model.oldPassword],
      'password': [this.model.password, [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(24)
      ]],
    });

    this.settingForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }


  onValueChanged(data?: any) {
    if (!this.settingForm) {
      return;
    }
    const form = this.settingForm;

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
    'username': '',
    'oldUsername': '',
    'password': '',
    'oldPassword': ''
  };

  validationMessages = {
    'username': {
      'required': '用户名是必填项.',
      'minlength': '用户名最短6个字符.',
      'maxlength': '用户名最长24个字符.',
    },
    'password': {
      'required': '密码是必填项.',
      'minlength': '密码最短6个字符.',
      'maxlength': '密码最长24个字符.',
    }
  };

}
