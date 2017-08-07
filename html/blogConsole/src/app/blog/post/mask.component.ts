import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'blog-post-mask',
  templateUrl: './mask.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', '../../common-style/form.component.css', './mask.component.css']
})
export class BlogPostMaskComponent implements OnInit {

  private idx: number;

  mask: boolean;
  submitText: string;

  model: any = {};
  loading: boolean;

  postForm: FormGroup;
  formErrors = {
    'BlogPostId': '',
    'BlogCategoryId': '',
    'BlogPostName': '',
    'BlogPostType': '',
    'BlogPostUrl': '',
    'BlogPostScript': ''
  };
  validationMessages = {
    'BlogPostId': {},
    'BlogCategoryId': {
      'required': '分类是必填项.'
    },
    'BlogPostName': {
      'required': '名称是必填项.',
      'maxLength': '名称的最大长度是255.'
    },
    'BlogPostType': {
      'required': '类型是必填项.'
    },
    'BlogPostUrl': {
      'required': 'URL是必填项.',
      'maxLength': 'URL的最大长度是1024.'
    },
    'BlogPostScript': {
      'required': 'SCRIPT是必填项.',
      'maxLength': 'SCRIPT的最大长度是1024.'
    }
  };

  categories: Array<any> = [];
  types: Array<any> = [
    { BlogPostType: 1, BlogPostTypeName: 'APP' },
    { BlogPostType: 2, BlogPostTypeName: '文章' }
  ];

  constructor(private tablelet: TableletService,
    private fb: FormBuilder,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    this.model = {};

    const self = this;
    this.tablelet.getMaskStatus(TableletService.BLOG_POST).subscribe(
      msg => {
        if ('mask' !== msg.maskName) {
          return;
        }

        self.idx = msg.idx;
        self.model = msg.model || {};
        self.mask = msg.mask;
        self.submitText = msg.submitText;
        self.categories = msg.categories || [];

        if (!self.mask) {
          return;
        }

        self.buildForm();
      }
    );
  }

  buildForm(): void {
    this.postForm = this.fb.group({
      'BlogPostId': [{ value: this.model.BlogPostId, disabled: true }],
      'BlogCategoryId': [this.model.BlogCategoryId, [
        Validators.required
      ]],
      'BlogPostName': [this.model.BlogPostName, [
        Validators.required,
        Validators.maxLength(255)
      ]],
      'BlogPostType': [this.model.BlogPostType, [
        Validators.required
      ]],
      'BlogPostUrl': [this.model.BlogPostUrl, [
        Validators.required,
        Validators.maxLength(1024)
      ]],
      'BlogPostScript': [this.model.BlogPostScript, [
        Validators.required,
        Validators.maxLength(1024)
      ]],
    });

    this.postForm.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.postForm) {
      return;
    }
    const form = this.postForm;

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
    if (!this.postForm.valid) {
      return;
    }

    this.loading = true;
    let data = this.postForm.value;
    data.BlogPostId = this.model.BlogPostId || null;

    const self = this;
    this.dao.postJSON(API.getAPI("post/set"), data).subscribe(
      ret => {
        self.loading = false;
        self.restCode.checkCode(ret, (retBody) => {
          self.tablelet.addData(TableletService.BLOG_POST, null === data.BlogPostId ? null : self.idx, retBody);
          self.tablelet.setMaskStatus(TableletService.BLOG_POST, { maskName: 'mask', mask: false });
        });
      },
      err => {
        self.loading = false;
        DaoUtil.logError(err);
      }
    );
  }

  cancel() {
    this.tablelet.setMaskStatus(TableletService.BLOG_POST, { maskName: 'mask', mask: false });
    this.loading = false;
  }

}