import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'resource-mapping-mask',
  templateUrl: './mask.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', '../../common-style/form.component.css', './mask.component.css']
})
export class ResourceMappingMaskComponent implements OnInit {

  private idx: number;

  mask: boolean;
  submitText: string;

  model: any = {};
  loading: boolean;

  resourceMappingForm: FormGroup;
  formErrors = {
    'ResourceLevelMappingId': '',
    'ResourceLevelId': '',
    'ResourceUrlPrefix': ''
  };
  validationMessages = {
    'ResourceLevelMappingId': {},
    'ResourceLevelId': {
      'required': '资源等级名称是必填项.'
    },
    'ResourceUrlPrefix': {
      'required': '资源等级例外说明是必填项.',
      'maxlength': '资源等级例外说明最长1024个字符.',
    }
  };

  resourceLevels: Array<any> = [];

  constructor(private tablelet: TableletService,
    private fb: FormBuilder,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    this.model = {};

    const self = this;
    this.tablelet.getMaskStatus(TableletService.RESOURCE_MAPPING).subscribe(
      msg => {
        self.idx = msg.idx;
        self.model = msg.model || {};
        self.mask = msg.mask;
        self.submitText = msg.submitText;

        self.buildForm();
      }
    );

    this.dao.getJSON(API.getAPI("resource-level/list"))
      .subscribe(
        ret => this.restCode.checkCode(ret, (retBody) => {
          self.resourceLevels = retBody;
        }),
        err => DaoUtil.logError(err)
      );
  }

  buildForm(): void {
    this.resourceMappingForm = this.fb.group({
      'ResourceLevelMappingId': [{ value: this.model.ResourceLevelMappingId, disabled: true }],
      'ResourceLevelId': [this.model.ResourceLevelId, [
        Validators.required
      ]],
      'ResourceUrlPrefix': [this.model.ResourceUrlPrefix, [
        Validators.required,
        Validators.maxLength(1024)
      ]],
    });

    this.resourceMappingForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.resourceMappingForm) {
      return;
    }
    const form = this.resourceMappingForm;

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
    if (!this.resourceMappingForm.valid) {
      return;
    }

    this.loading = true;
    let data = this.resourceMappingForm.value;
    data.ResourceLevelMappingId = this.model.ResourceLevelMappingId || null;
    data.ResourceLevelMappingDisabled = this.model.ResourceLevelMappingDisabled || null;

    const self = this;
    this.dao.postJSON(API.getAPI("resource-level-mapping/set"), data).subscribe(
      ret => {
        self.loading = false;
        self.restCode.checkCode(ret, (retBody) => {
          self.resourceLevels.forEach(rl => {
            if (rl.ResourceLevelId + '' === retBody.ResourceLevelId
              || rl.ResourceLevelId === retBody.ResourceLevelId) {
              retBody.ResourceLevelName = rl.ResourceLevelName;
            }
          });

          self.tablelet.addData(TableletService.RESOURCE_MAPPING, null === data.ResourceLevelMappingId ? null : self.idx, retBody);
          self.tablelet.setMaskStatus(TableletService.RESOURCE_MAPPING, { mask: false });
        });
      },
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

  cancel() {
    this.tablelet.setMaskStatus(TableletService.RESOURCE_MAPPING, { mask: false });
    this.loading = false;
  }
}