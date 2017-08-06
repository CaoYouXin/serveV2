import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'favour-rule-mask',
  templateUrl: './mask.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', '../../common-style/form.component.css', './mask.component.css']
})
export class FavourRuleMaskComponent implements OnInit {

  private idx: number;

  mask: boolean;
  submitText: string;

  model: any = {};
  loading: boolean;

  favourDetailForm: FormGroup;
  formErrors = {
    'UserFavourRuleId': '',
    'UserFavourRulePattern': '',
    'UserFavourRuleScore': '',
    'UserFavourRuleLimit': ''
  };
  validationMessages = {
    'UserFavourRuleId': {},
    'UserFavourRulePattern': {
      'required': '模式是必填项.',
      'maxlength': '模式最长255个字符.',
    },
    'UserFavourRuleScore': {
      'required': '增加值是必填的.',
      'min': '增加值最小为1.'
    },
    'UserFavourRuleLimit': {
      'required': '好感度值是必填项.',
      'min': '次数上限最小为1.'
    }
  };

  constructor(private tablelet: TableletService,
    private fb: FormBuilder,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    this.model = {};

    const self = this;
    this.tablelet.getMaskStatus(TableletService.FAVOUR_RULE).subscribe(
      msg => {
        self.idx = msg.idx;
        self.model = msg.model || {};
        self.mask = msg.mask;
        self.submitText = msg.submitText;

        self.buildForm();
      }
    );
  }

  buildForm(): void {
    this.favourDetailForm = this.fb.group({
      'UserFavourRuleId': [{ value: this.model.UserFavourRuleId, disabled: true }],
      'UserFavourRulePattern': [this.model.UserFavourRulePattern, [
        Validators.required,
        Validators.maxLength(255)
      ]],
      'UserFavourRuleScore': [this.model.UserFavourRuleScore, [
        Validators.required,
        Validators.min(1),
      ]],
      'UserFavourRuleLimit': [this.model.UserFavourRuleLimit, [
        Validators.required,
        Validators.min(1)
      ]],
    });

    this.favourDetailForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.favourDetailForm) {
      return;
    }
    const form = this.favourDetailForm;

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

  submit() {
    if (!this.favourDetailForm.valid) {
      return;
    }

    this.loading = true;
    let data = this.favourDetailForm.value;
    data.UserFavourRuleId = this.model.UserFavourRuleId || null;

    const self = this;
    this.dao.postJSON(API.getAPI("favour-rule/set"), data).subscribe(
      ret => {
        self.loading = false;
        self.restCode.checkCode(ret, (retBody) => {
          self.tablelet.addData(TableletService.FAVOUR_RULE, null === data.UserFavourRuleId ? null : self.idx, retBody);
          self.tablelet.setMaskStatus(TableletService.FAVOUR_RULE, { mask: false });
        });
      },
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

  cancel() {
    this.tablelet.setMaskStatus(TableletService.FAVOUR_RULE, { mask: false });
    this.loading = false;
  }

}