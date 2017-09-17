import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'diary-newpage',
  templateUrl: './newpage.component.html',
  styleUrls: ['./newpage.component.css']
})
export class NewPageComponent implements OnInit {

  titleFocused: boolean;

  pageModel: any = {};

  pageForm: FormGroup;
  pageFormErrors = {
    'title': ''
  };
  pageFormValidationMessages = {
    'title': {
      'required': '用户名是必填项.',
      'minlength': '用户名最短1个字符.',
      'maxlength': '用户名最长255个字符.',
    }
  };

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.buildPageForm();
  }

  buildPageForm(): void {
    this.pageForm = this.fb.group({
      'title': [this.pageModel.title, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(255)
      ]]
    });

    this.pageForm.valueChanges
      .subscribe(data => this.onValueChanged(this.pageForm, this.pageFormErrors, this.pageFormValidationMessages));

    this.onValueChanged(this.pageForm, this.pageFormErrors, this.pageFormValidationMessages); // (re)set validation messages now
  }

  onValueChanged(form: FormGroup, formErrors: any, validationMessages: any) {
    if (!form) {
      return;
    }

    for (const field in formErrors) {
      // clear previous error message (if any)
      formErrors[field] = '';
      const control = form.get(field);

      if (control && control.dirty && !control.valid) {
        const messages = validationMessages[field];
        for (const key in control.errors) {
          formErrors[field] += messages[key] + ' ';
        }
      }
    }
  }
}