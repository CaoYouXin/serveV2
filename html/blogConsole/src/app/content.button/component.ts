import { Component, Input, Output, EventEmitter, OnChanges, HostBinding } from "@angular/core";
import { DaoUtil, RestCode } from "../http";
import { API } from "../const";

@Component({
  selector: 'content-button',
  templateUrl: './component.html',
  styleUrls: ['./component.css']
})
export class ContentButtonComponent implements OnChanges {

  @HostBinding("style.display") display = 'block';
  @HostBinding("style.marginTop") marginTop = '1em';

  @Input()
  buttonMode: boolean;

  @Input()
  buttonText: string;

  @Input()
  postId: number;

  @Output()
  handler: EventEmitter<any> = new EventEmitter();

  loading: boolean;
  comments: Array<any> = [];

  constructor(
    private dao: DaoUtil,
    private rest: RestCode
  ) {
  }

  ngOnChanges(simple) {
    if (simple.buttonMode) {
      if (!simple.buttonMode.currentValue) {
        this.loading = true;
        const self = this;
        this.dao.getJSON(API.getAPI("comment/list")(this.postId)).subscribe(
          ret => self.rest.checkCode(ret, retBody => {
            self.comments = retBody;
            self.loading = false;
          }),
          err => DaoUtil.logError(err)
        );
      }
    }
  }

  replyCommentBtnClicked(c, cc, idx, i) {
    let data = Object.assign({}, cc || c);
    let pointer = !cc ? this.comments : c.Leafs;
    let index = i || idx;

    data.CommentDisabled = !data.CommentDisabled;

    const self = this;
    this.dao.postJSON(API.getAPI("comment/set"), data).subscribe(
      ret => self.rest.checkCode(ret, retBody => {
        pointer[index] = retBody;
      }),
      err => DaoUtil.logError(err)
    );
  }

}