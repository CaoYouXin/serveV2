import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { UserService } from "../../services/user.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../../commons/form.component.css', '../../commons/mao.component.css']
})
export class RegisterComponent implements OnInit {

  model: any = {};
  loading: boolean = false;

  loginForm: FormGroup;

  constructor(private fb: FormBuilder,
    private service: UserService,
    private router: Router) {
  }

  login(): void {
    if (!this.loginForm.valid) {
      return;
    }

    this.loading = true;
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

}
