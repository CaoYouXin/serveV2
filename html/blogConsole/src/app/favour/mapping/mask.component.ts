import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'favour-mapping-mask',
  templateUrl: './mask.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', '../../common-style/form.component.css', './mask.component.css']
})
export class FavourMappingMaskComponent implements OnInit {

  private idx: number;

  mask: boolean;
  submitText: string;

  model: any = {};
  loading: boolean;

  favourMappingForm: FormGroup;
  formErrors = {
    'UserFavourMappingId': '',
    'ResourceLevelId': '',
    'UserFavourThreshold': ''
  };
  validationMessages = {
    'UserFavourMappingId': {},
    'ResourceLevelId': {
      'required': '资源等级是必选的.'
    },
    'UserFavourThreshold': {
      'required': '阈值是必填项.'
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
    this.tablelet.getMaskStatus(TableletService.FAVOUR_MAPPING).subscribe(
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
    this.favourMappingForm = this.fb.group({
      'UserFavourMappingId': [{ value: this.model.UserFavourMappingId, disabled: true }],
      'ResourceLevelId': [this.model.ResourceLevelId, [
        Validators.required
      ]],
      'UserFavourThreshold': [this.model.UserFavourThreshold, [
        Validators.required,
      ]],
    });

    this.favourMappingForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.favourMappingForm) {
      return;
    }
    const form = this.favourMappingForm;

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
    if (!this.favourMappingForm.valid) {
      return;
    }

    this.loading = true;
    let data = this.favourMappingForm.value;
    data.UserFavourMappingId = this.model.UserFavourMappingId || null;

    const self = this;
    this.dao.postJSON(API.getAPI("favour-mapping/set"), data).subscribe(
      ret => {
        self.loading = false;
        self.restCode.checkCode(ret, (retBody) => {
          this.resourceLevels.forEach(rl => {
            if (rl.ResourceLevelId + '' === retBody.ResourceLevelId
              || rl.ResourceLevelId === retBody.ResourceLevelId) {
              retBody = Object.assign(retBody, rl);
            }
          });

          self.tablelet.addData(TableletService.FAVOUR_MAPPING, null === data.UserFavourMappingId ? null : self.idx, retBody);
          self.tablelet.setMaskStatus(TableletService.FAVOUR_MAPPING, { mask: false });
        });
      },
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

  cancel() {
    this.tablelet.setMaskStatus(TableletService.FAVOUR_MAPPING, { mask: false });
    this.loading = false;
  }

}