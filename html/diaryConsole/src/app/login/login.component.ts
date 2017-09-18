import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AdminService } from "../../service";
import { RestCode } from "../../http";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['../../common-style/form.component.css']
})
export class LoginComponent implements OnInit {

  model: any = {};
  loading: boolean = false;

  loginForm: FormGroup;
  formErrors = {
    'username': '',
    'password': ''
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

  constructor(private fb: FormBuilder,
    private service: AdminService,
    private router: Router) {
  }

  login(): void {
    if (!this.loginForm.valid) {
      return;
    }

    this.loading = true;
    const self = this;
    this.service.verify(this.loginForm.value,
      (ret) => {
        localStorage.setItem('currentUser', ret);
        let retUrl = RestCode.getLoginRetUrl();
        if (!retUrl) {
          window.location.href = retUrl;
        } else {
          self.router.navigate(['/']);
        }
      }, () => {
        self.loading = false;
      });
  }

  ngOnInit(): void {
    this.buildForm();
  }

  buildForm(): void {
    this.loginForm = this.fb.group({
      'username': [this.model.username, [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(24)
      ]],
      'password': [this.model.password, [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(24)
      ]],
    });

    this.loginForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.loginForm) {
      return;
    }
    const form = this.loginForm;

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

}
