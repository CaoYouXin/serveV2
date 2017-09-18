import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Location } from "@angular/common";

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

  pageModel: any = {
    'type': '学习'
  };
  relations: Array<string> = [];
  contents: Array<any> = [{ text: "" }];

  pageForm: FormGroup;
  pageDateError: string;
  relatedError: string;
  contentsError: string;
  pageFormErrors = {
    'title': '',
    'startDate': '',
    'endDate': '',
    'type': '',
    'location': ''
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
    'title': {
      'required': '标题是必填项.',
      'minlength': '标题最短1个字符.',
      'maxlength': '标题最长255个字符.',
    },
    'startDate': {
      'required': '开始日期是必填项.'
    },
    'endDate': {
      'required': '结束日期是必填项.'
    },
    'type': {
      'required': '类型是必填项.'
    },
    'location': {
      'required': '地点是必填项.',
      'minlength': '地点最短1个字符.',
      'maxlength': '地点最长255个字符.',
    }
  };

  constructor(private fb: FormBuilder,
    private location: Location) { }

  ngOnInit() {
    this.buildPageForm();
  }

  buildPageForm(): void {
    this.pageForm = this.fb.group({
      'title': [this.pageModel.title, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(255)
      ]],
      'startDate': [this.pageModel.startDate, [
        Validators.required
      ]],
      'endDate': [this.pageModel.endDate, [
        Validators.required
      ]],
      'location': [this.pageModel.location, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(255)
      ]],
      'type': [this.pageModel.type, [
        Validators.required
      ]]
    }, { validator: compareDates('startDate', 'endDate') });

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

    if (!this.validRelations()) {
      return;
    }

    if (!this.validContents()) {
      return;
    }

    // save api invoked
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

    return true;
  }

  private validRelations() {
    let related = this.relations.join("|");

    this.relatedError = '';
    if (related.length > 255) {
      this.relatedError += this.pageFormValidationMessages.relatedError.maxLength;
      return false;
    }

    return true;
  }
}