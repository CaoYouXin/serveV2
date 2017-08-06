import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'blog-category-mask',
  templateUrl: './mask.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', '../../common-style/form.component.css', './mask.component.css']
})
export class BlogCategoryMaskComponent implements OnInit {

  private idx: number;

  mask: boolean;
  submitText: string;

  model: any = {};
  loading: boolean;

  categoryForm: FormGroup;
  formErrors = {
    'BlogCategoryId': '',
    'ParentBlogCategoryId': '',
    'BlogCategoryName': '',
    'BlogCategoryUrl': '',
    'BlogCategoryScript': ''
  };
  validationMessages = {
    'BlogCategoryId': {},
    'ParentBlogCategoryId': {},
    'BlogCategoryName': {
      'required': '名称是必填项.',
      'maxLength': '名称的最大长度是255.'
    },
    'BlogCategoryUrl': {
      'required': 'URL是必填项.',
      'maxLength': 'URL的最大长度是1024.'
    },
    'BlogCategoryScript': {
      'required': 'SCRIPT是必填项.',
      'maxLength': 'SCRIPT的最大长度是1024.'
    }
  };

  categories: Array<any> = [];

  constructor(private tablelet: TableletService,
    private fb: FormBuilder,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    this.model = {};

    const self = this;
    this.tablelet.getMaskStatus(TableletService.BLOG_CATEGORY).subscribe(
      msg => {
        if ('mask' !== msg.maskName) {
          return;
        }

        self.idx = msg.idx;
        self.model = msg.model || {};
        self.mask = msg.mask;
        self.submitText = msg.submitText;

        if (msg.data) {
          let selfIdx = msg.data.findIndex(c => c.BlogCategoryId === self.model.BlogCategoryId);
          if (-1 === selfIdx) {
            self.categories = msg.data;
          } else {
            self.categories = [...msg.data.slice(0, selfIdx), ...msg.data.slice(selfIdx + 1)];
          }
        } else {
          self.categories = [];
        }
        self.categories = [
          { BlogCategoryId: 0, BlogCategoryName: '' },
          ...self.categories
        ];

        self.buildForm();
      }
    );
  }

  buildForm(): void {
    this.categoryForm = this.fb.group({
      'BlogCategoryId': [{ value: this.model.BlogCategoryId, disabled: true }],
      'ParentBlogCategoryId': [this.model.ParentBlogCategoryId, []],
      'BlogCategoryName': [this.model.BlogCategoryName, [
        Validators.required,
        Validators.maxLength(255)
      ]],
      'BlogCategoryUrl': [this.model.BlogCategoryUrl, [
        Validators.required,
        Validators.maxLength(1024)
      ]],
      'BlogCategoryScript': [this.model.BlogCategoryScript, [
        Validators.required,
        Validators.maxLength(1024)
      ]],
    });

    this.categoryForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.categoryForm) {
      return;
    }
    const form = this.categoryForm;

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
    if (!this.categoryForm.valid) {
      return;
    }

    this.loading = true;
    let data = this.categoryForm.value;
    data.BlogCategoryId = this.model.BlogCategoryId || null;

    console.log(data);

    const self = this;
    this.dao.postJSON(API.getAPI("category/set"), data).subscribe(
      ret => {
        self.loading = false;
        self.restCode.checkCode(ret, (retBody) => {
          self.tablelet.addData(TableletService.BLOG_CATEGORY, null === data.BlogCategoryId ? null : self.idx, retBody);
          self.tablelet.setMaskStatus(TableletService.BLOG_CATEGORY, { maskName: 'mask', mask: false });
        });
      },
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

  cancel() {
    this.tablelet.setMaskStatus(TableletService.BLOG_CATEGORY, { maskName: 'mask', mask: false });
    this.loading = false;
  }

}