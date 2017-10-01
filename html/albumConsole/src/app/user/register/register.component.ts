import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { UserService } from "../../services/user.service";
import { API } from "../../services/api.const";
import { Md5 } from "ts-md5/dist/md5";

const matchingPasswords = (passwordKey: string, confirmPasswordKey: string) => {
  return (group: FormGroup): { [key: string]: any } => {
    let password = group.controls[passwordKey];
    let confirmPassword = group.controls[confirmPasswordKey];

    if (password.value !== confirmPassword.value) {
      return {
        'MismatchedPasswords': true
      };
    }
  }
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../../commons/form.component.css', '../../commons/mao.component.css']
})
export class RegisterComponent implements OnInit {

  model: any = {};
  loading: boolean = false;
  captchaImage: string;
  captchaImageToken: string;

  registerForm: FormGroup;

  constructor(private fb: FormBuilder,
    private service: UserService,
    private router: Router) {
  }

  register(): void {
    if (!this.registerForm.valid) {
      return;
    }

    const self = this;
    self.loading = true;

    const model = this.registerForm.value;

    self.service.register(Object.assign(model, {
      "UserPassword": Md5.hashStr(model.UserPassword),
      "UserPasswordConfirmed": Md5.hashStr(model.UserPasswordConfirmed),
      "ImageCaptchaToken": self.captchaImageToken
    }), (ret) => {
      self.loading = false;
      self.router.navigateByUrl('/login');
    }, () => self.loading = false);
  }

  getImageCaptcha() {
    this.captchaImageToken = Md5.hashStr(new Date().toISOString()).toString();
    this.captchaImage = API.getAPI("CaptchaImage")(190, 30, this.captchaImageToken);
  }

  ngOnInit(): void {
    this.buildForm();
    this.getImageCaptcha();
  }

  buildForm(): void {
    this.registerForm = this.fb.group({
      'UserName': [this.model.UserName, [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(24)
      ]],
      'UserPassword': [this.model.UserPassword, [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(24)
      ]],
      'UserPasswordConfirmed': [this.model.UserPasswordConfirmed, [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(24)
      ]],
      'ImageCaptcha': [this.model.ImageCaptcha, [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(4)
      ]]
    }, { validator: matchingPasswords('UserPassword', 'UserPasswordConfirmed') });

    this.registerForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.registerForm) {
      return;
    }
    const form = this.registerForm;
    const formErrorsObj = form.errors || {};
    const formErrors = Object.keys(formErrorsObj).filter(key => formErrorsObj[key]);

    for (const field in this.formErrors) {
      // clear previous error message (if any)
      this.formErrors[field] = '';
      const control = form.get(field);
      const messages = this.validationMessages[field];

      if (control && control.dirty && !control.valid) {
        for (const key in control.errors) {
          this.formErrors[field] += messages[key] + ' ';
        }
      } else if (formErrors && formErrors.some(err => err === field)) {
        this.formErrors[field] += messages + ' ';
      }
    }
  }

  formErrors = {
    'MismatchedPasswords': '',
    'UserName': '',
    'UserPassword': '',
    'UserPasswordConfirmed': '',
    'ImageCaptcha': ''
  };

  validationMessages = {
    'MismatchedPasswords': '两次输入的密码不一致',
    'UserName': {
      'required': '用户名是必填项.',
      'minlength': '用户名最短6个字符.',
      'maxlength': '用户名最长24个字符.',
    },
    'UserPassword': {
      'required': '密码是必填项.',
      'minlength': '密码最短6个字符.',
      'maxlength': '密码最长24个字符.',
    },
    'UserPasswordConfirmed': {
      'required': '密码是必填项.',
      'minlength': '密码最短6个字符.',
      'maxlength': '密码最长24个字符.',
    },
    'ImageCaptcha': {
      'required': '密码是必填项.',
      'minlength': '密码最短6个字符.',
      'maxlength': '密码最长24个字符.',
    }
  };

}
