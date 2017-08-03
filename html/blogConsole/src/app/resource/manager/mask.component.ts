import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'resource-manager-mask',
  templateUrl: './mask.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', '../../common-style/form.component.css', './mask.component.css']
})
export class ResourceManangerMaskComponent implements OnInit {

  private idx: number;

  mask: boolean;
  submitText: string;

  model: any = {};
  loading: boolean;

  resourceManagerForm: FormGroup;
  formErrors = {
    'ResourceLevelId': '',
    'ResourceLevelName': '',
    'ResourceLevelExpMsg': ''
  };
  validationMessages = {
    'ResourceLevelId': {},
    'ResourceLevelName': {
      'required': '资源等级名称是必填项.',
      'maxlength': '资源等级名称最长255个字符.',
    },
    'ResourceLevelExpMsg': {
      'required': '资源等级例外说明是必填项.',
      'maxlength': '资源等级例外说明最长255个字符.',
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
    this.tablelet.getMaskStatus(TableletService.RESOURCE_MANAGER).subscribe(
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
    this.resourceManagerForm = this.fb.group({
      'ResourceLevelId': [{ value: this.model.ResourceLevelId, disabled: true }],
      'ResourceLevelName': [this.model.ResourceLevelName, [
        Validators.required,
        Validators.maxLength(255)
      ]],
      'ResourceLevelExpMsg': [this.model.ResourceLevelExpMsg, [
        Validators.required,
        Validators.maxLength(255)
      ]],
    });

    this.resourceManagerForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.resourceManagerForm) {
      return;
    }
    const form = this.resourceManagerForm;

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
    if (!this.resourceManagerForm.valid) {
      return;
    }

    this.loading = true;
    let data = this.resourceManagerForm.value;
    data.ResourceLevelId = this.model.ResourceLevelId || null;

    const self = this;
    this.dao.postJSON(API.getAPI("resource-level/set"), data).subscribe(
      ret => {
        self.loading = false;
        self.restCode.checkCode(ret, (retBody) => {
          self.tablelet.addData(TableletService.RESOURCE_MANAGER, !data.ResourceLevelId ? null : self.idx, retBody);
          self.tablelet.setMaskStatus(TableletService.RESOURCE_MANAGER, { mask: false });
        });
      },
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

  cancel() {
    this.tablelet.setMaskStatus(TableletService.RESOURCE_MANAGER, { mask: false });
    this.loading = false;
  }

}