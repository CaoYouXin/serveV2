import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AlbumEditorService } from '../../../services/album-editor.service';

@Component({
  selector: 'app-edit-album',
  templateUrl: './edit-album.component.html',
  styleUrls: ['../../../commons/form.component.css', './edit-album.component.css']
})
export class EditAlbumComponent implements OnInit {

  private id: number;

  show: boolean;
  model: any = {};
  loading: boolean = false;

  form: FormGroup;

  @Output("onSave")
  onSave: EventEmitter<any> = new EventEmitter<any>();

  constructor(private fb: FormBuilder, private editorService: AlbumEditorService) { }

  cancel() {
    this.editorService.cancel();
  }

  save() {
    if (!this.form.valid) {
      return;
    }

    const self = this;
    self.loading = true;

    const model = this.form.value;

    self.editorService.save(Object.assign({}, model, {
      'AlbumId': self.id
    }), (ret) => {
      self.loading = false;
      self.onSave.emit(ret);
    }, () => self.loading = false);
  }

  ngOnInit() {
    const self = this;
    self.buildForm();
    self.editorService.bindAlbumEditorCmd().subscribe(cmd => {
      self.show = cmd.show;
      self.model = cmd.model;

      if (self.show) {
        self.buildForm();

        if (cmd.model) {
          self.id = cmd.model.AlbumId;
        }
      }
    });
  }

  buildForm(): void {
    this.form = this.fb.group({
      'AlbumId': [{ value: this.model.AlbumId, disabled: true }],
      'AlbumName': [this.model.AlbumName, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(24)
      ]],
    });

    this.form.valueChanges
      .subscribe(data => this.onValueChanged(data));

    this.onValueChanged(); // (re)set validation messages now
  }

  onValueChanged(data?: any) {
    if (!this.form) {
      return;
    }
    const form = this.form;

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

  formErrors = {
    'AlbumId': '',
    'AlbumName': ''
  };

  validationMessages = {
    'AlbumId': {
    },
    'AlbumName': {
      'required': '相册名称是必填项.',
      'minlength': '相册名称最短1个字符.',
      'maxlength': '相册名称最长24个字符.',
    }
  };

}
