import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { TableletService } from "../../service";
import { API, DaoUtil, RestCode } from "../../http";

const compareDates = (startDateKey: string, endDateKey: string) => {
  return (group: FormGroup): { [key: string]: any } => {
    let startDate = group.controls[startDateKey];
    let endDate = group.controls[endDateKey];

    if (null === startDate.value || null === endDate.value) {
      return {};
    }

    if (new Date(startDate.value).getTime() > new Date(endDate.value).getTime()) {
      return {
        compared: true
      };
    }
  }
}

@Component({
  selector: 'diary-newpage',
  templateUrl: './newpage.component.html',
  styleUrls: ['./newpage.component.css']
})
export class NewPageComponent implements OnInit {

  titleFocused: boolean;
  startDateFocused: boolean;
  endDateFocused: boolean;
  typeFocused: boolean;
  locationFocused: boolean;
  relatedFocused: boolean;

  pageModel: any = { 'DiaryPageType': '学习' };
  relations: Array<string> = [];
  contents: Array<any> = [{ 'text': "" }];

  pageForm: FormGroup;
  pageDateError: string;
  relatedError: string;
  contentsError: string;
  pageFormErrors = {
    'DiaryPageTitle': '',
    'DiaryPageStartDate': '',
    'DiaryPageEndDate': '',
    'DiaryPageType': '',
    'DiaryPageLocation': ''
  };
  pageFormValidationMessages = {
    'pageDateError': {
      'compared': '开始日期应该早于结束日期.'
    },
    'relatedError': {
      'maxLength': '相关人过多，请进行删减后再保存.'
    },
    'contentsError': {
      'required': '内容是必填项.',
      'maxLength': '内容过长，请进行删减后再保存.'
    },
    'DiaryPageTitle': {
      'required': '标题是必填项.',
      'minlength': '标题最短1个字符.',
      'maxlength': '标题最长255个字符.',
    },
    'DiaryPageStartDate': {
      'required': '开始日期是必填项.'
    },
    'DiaryPageEndDate': {
      'required': '结束日期是必填项.'
    },
    'DiaryPageType': {
      'required': '类型是必填项.'
    },
    'DiaryPageLocation': {
      'required': '地点是必填项.',
      'minlength': '地点最短1个字符.',
      'maxlength': '地点最长255个字符.',
    }
  };

  constructor(private fb: FormBuilder,
    private router: Router,
    private tablelet: TableletService,
    private dao: DaoUtil,
    private rest: RestCode) { }

  ngOnInit() {
    let datum = this.tablelet.getHandlingData(TableletService.PAGEs);
    if (datum) {
      this.pageModel = Object.assign(datum, {
        DiaryPageStartDate: datum.DiaryPageStartDate.substring(0, 10),
        DiaryPageEndDate: datum.DiaryPageEndDate.substring(0, 10)
      });

      this.relations = datum.DiaryPageRelated.split('|').filter(r => !!r);
      this.contents = datum.DiaryPageContent.split('</p><p>').filter(c => !!c).map(text => ({ text }));
    }

    this.buildPageForm();
  }

  buildPageForm(): void {
    this.pageForm = this.fb.group({
      'DiaryPageTitle': [this.pageModel.DiaryPageTitle, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(255)
      ]],
      'DiaryPageStartDate': [this.pageModel.DiaryPageStartDate, [
        Validators.required
      ]],
      'DiaryPageEndDate': [this.pageModel.DiaryPageEndDate, [
        Validators.required
      ]],
      'DiaryPageLocation': [this.pageModel.DiaryPageLocation, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(255)
      ]],
      'DiaryPageType': [this.pageModel.DiaryPageType, [
        Validators.required
      ]]
    }, { validator: compareDates('DiaryPageStartDate', 'DiaryPageEndDate') });

    this.pageForm.valueChanges
      .subscribe(data => this.onValueChanged(this.pageForm, this.pageFormErrors, this.pageFormValidationMessages));

    this.onValueChanged(this.pageForm, this.pageFormErrors, this.pageFormValidationMessages); // (re)set validation messages now
  }

  onValueChanged(form: FormGroup, formErrors: any, validationMessages: any, show?: boolean) {
    if (!form) {
      return;
    }

    this.pageDateError = '';
    if ((show || form.dirty) && !form.valid) {
      for (const key in form.errors) {
        this.pageDateError += this.pageFormValidationMessages['pageDateError'][key] + ' ';
      }
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

  addRelated(e) {
    if (!e.target.value) {
      return;
    }

    if (e.target.value.indexOf('|') !== -1) {
      alert('含有非法字符"|"');
      return;
    }

    this.relations.push(e.target.value.trim());
    e.target.value = '';

    let filteredRelations = [];
    this.relations.forEach(related => {
      if (!filteredRelations.some(fR => fR === related)) {
        filteredRelations.push(related);
      }
    });
    this.relations = filteredRelations;
  }

  removeRelated(toRemove) {
    this.relations = this.relations.filter(related => related !== toRemove);
  }

  addInputBlock(e, i) {
    this.contents = [...this.contents.slice(0, i + 1), { text: "" }, ...this.contents.slice(i + 1, this.contents.length)];
  }

  removeInputBlock(e, i) {
    this.contents = [...this.contents.slice(0, i), ...this.contents.slice(i + 1, this.contents.length)];
  }

  savePage() {
    console.log(this.pageForm);
    if (!this.pageForm.valid) {
      this.onValueChanged(this.pageForm, this.pageFormErrors, this.pageFormValidationMessages, true);
      return;
    }

    let DiaryPageRelated = this.validRelations();
    console.log(DiaryPageRelated);
    if (false === DiaryPageRelated) {
      return;
    }

    let DiaryPageContent = this.validContents();
    console.log(DiaryPageContent);
    if (!DiaryPageContent) {
      return;
    }

    const self = this;
    this.tablelet.addDataByAPI(TableletService.PAGEs, API.getAPI("page/save"), Object.assign(this.pageForm.value, {
      DiaryPageId: self.pageModel.DiaryPageId,
      DiaryPageContent,
      DiaryPageRelated,
      DiaryPageStartDate: this.pageForm.value.DiaryPageStartDate + ' 00:00:00',
      DiaryPageEndDate: this.pageForm.value.DiaryPageEndDate + ' 00:00:00'
    }), this.tablelet.getHandlingIdx(TableletService.PAGEs), () => {
      self.router.navigate(['/page']);
    });
  }

  private validContents() {
    let wholeContent = this.contents.reduce((pv, cv, ci, array) => {
      if (ci !== array.length - 1) {
        return pv + cv.text.trim() + '</p><p>';
      }
      return pv + cv.text.trim();
    }, '');

    this.contentsError = '';
    if (wholeContent.length > 102400) {
      this.contentsError += this.pageFormValidationMessages.contentsError.maxLength;
      return false;
    }
    if (wholeContent.length === 0) {
      this.contentsError += this.pageFormValidationMessages.contentsError.required;
      return false;
    }

    return wholeContent;
  }

  private validRelations() {
    let related = this.relations.join("|");

    this.relatedError = '';
    if (related.length > 255) {
      this.relatedError += this.pageFormValidationMessages.relatedError.maxLength;
      return false;
    }

    return related;
  }
}