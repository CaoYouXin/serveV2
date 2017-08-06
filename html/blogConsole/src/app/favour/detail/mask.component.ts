import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'favour-detail-mask',
  templateUrl: './mask.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', '../../common-style/form.component.css', './mask.component.css']
})
export class FavourDetialMaskComponent implements OnInit {

  private idx: number;

  mask: boolean;
  submitText: string;

  model: any = {};
  loading: boolean;

  favourDetailForm: FormGroup;
  formErrors = {
    'UserFavourId': '',
    'UserId': '',
    'UserFavourValue': ''
  };
  validationMessages = {
    'UserFavourId': {},
    'UserId': {
      'required': '用户是必选的.'
    },
    'UserFavourValue': {
      'required': '好感度值是必填项.'
    }
  };

  users: Array<any> = [];

  constructor(private tablelet: TableletService,
    private fb: FormBuilder,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    this.model = {};

    const self = this;
    this.tablelet.getMaskStatus(TableletService.USER_FAVOUR).subscribe(
      msg => {
        self.idx = msg.idx;
        self.model = msg.model || {};
        self.mask = msg.mask;
        self.submitText = msg.submitText;

        if (!self.mask) {
          return;
        }

        self.buildForm();

        self.dao.getJSON(API.getAPI("user/list")).subscribe(
          ret => self.restCode.checkCode(ret, (retBody) => {
            self.users = retBody;
          }),
          err => DaoUtil.logError(err)
        );
      }
    );
  }

  buildForm(): void {
    this.favourDetailForm = this.fb.group({
      'UserFavourId': [{ value: this.model.UserFavourId, disabled: true }],
      'UserId': [this.model.UserId, [
        Validators.required
      ]],
      'UserFavourValue': [this.model.UserFavourValue, [
        Validators.required,
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
    data.UserFavourId = this.model.UserFavourId || null;

    const self = this;
    this.dao.postJSON(API.getAPI("favour/set"), data).subscribe(
      ret => {
        self.loading = false;
        self.restCode.checkCode(ret, (retBody) => {
          this.users.forEach(u => {
            if (u.UserId + '' === retBody.UserId
              || u.UserId === retBody.UserId) {
              retBody = Object.assign(retBody, u);
            }
          });

          self.tablelet.addData(TableletService.USER_FAVOUR, null === data.UserFavourId ? null : self.idx, retBody);
          self.tablelet.setMaskStatus(TableletService.USER_FAVOUR, { mask: false });
        });
      },
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

  cancel() {
    this.tablelet.setMaskStatus(TableletService.USER_FAVOUR, { mask: false });
    this.loading = false;
  }

}