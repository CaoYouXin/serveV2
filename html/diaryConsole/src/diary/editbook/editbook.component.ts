import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { TableletService } from "../../service";
import { API, DaoUtil, RestCode } from "../../http";

@Component({
  selector: 'diary-editbook',
  templateUrl: './editbook.component.html',
  styleUrls: ['./editbook.component.css']
})
export class EditBookComponent implements OnInit {

  titleFocused: boolean;

  bookModel: any = {};
  resourceLevels: Array<any> = [];

  pageForm: FormGroup;
  pageFormErrors = {
    'DiaryBookTitle': '',
    'ResourceLevelId': ''
  };
  pageFormValidationMessages = {
    'DiaryBookTitle': {
      'required': '标题是必填项.',
      'minlength': '标题最短1个字符.',
      'maxlength': '标题最长255个字符.',
    },
    'ResourceLevelId': {
      'required': '资源等级是必填项.'
    }
  };

  constructor(private fb: FormBuilder,
    private router: Router,
    private tablelet: TableletService,
    private dao: DaoUtil,
    private rest: RestCode) { }

  ngOnInit() {
    let datum = this.tablelet.getHandlingData(TableletService.BOOKs);
    if (datum) {
      this.bookModel = datum;
    }

    this.buildPageForm();

    const self = this;
    self.dao.getJSON(API.getAPI("resource-level/list")).subscribe(
      ret => self.rest.checkCode(ret, (retBody) => {
        self.resourceLevels = [{ ResourceLevelId: 0 }].concat(retBody);
      }),
      err => DaoUtil.logError(err)
    );
  }

  buildPageForm(): void {
    this.pageForm = this.fb.group({
      'DiaryBookTitle': [this.bookModel.DiaryBookTitle, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(255)
      ]],
      'ResourceLevelId': [this.bookModel.ResourceLevelId, [
        Validators.required
      ]]
    });

    this.pageForm.valueChanges
      .subscribe(data => this.onValueChanged(this.pageForm, this.pageFormErrors, this.pageFormValidationMessages));

    this.onValueChanged(this.pageForm, this.pageFormErrors, this.pageFormValidationMessages); // (re)set validation messages now
  }

  onValueChanged(form: FormGroup, formErrors: any, validationMessages: any, show?: boolean) {
    if (!form) {
      return;
    }

    for (const field in formErrors) {
      // clear previous error message (if any)
      formErrors[field] = '';
      const control = form.get(field);

      if (control && (show || control.dirty) && !control.valid) {
        const messages = validationMessages[field];
        for (const key in control.errors) {
          formErrors[field] += messages[key] + ' ';
        }
      }
    }
  }

  savePage() {
    console.log(this.pageForm);
    if (!this.pageForm.valid) {
      this.onValueChanged(this.pageForm, this.pageFormErrors, this.pageFormValidationMessages, true);
      return;
    }

    const self = this;
    this.tablelet.addDataByAPI(TableletService.BOOKs, API.getAPI("book/save"), Object.assign(this.pageForm.value, {
      DiaryBookId: self.bookModel.DiaryBookId
    }), this.tablelet.getHandlingIdx(TableletService.BOOKs), () => {
      self.router.navigate(['/book']);
    });
  }

}