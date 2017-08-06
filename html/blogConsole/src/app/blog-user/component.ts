import { Component, HostBinding, OnInit } from "@angular/core";
import { slideInUpAnimation } from "../animation/route.animation";
import { TableletService } from "../service";
import { DaoUtil, RestCode } from "../http";
import { API } from "../const";

@Component({
  selector: 'blog-user',
  templateUrl: './component.html',
  styleUrls: ['./component.css'],
  animations: [slideInUpAnimation]
})
export class BlogUserComponent implements OnInit {

  @HostBinding('@routeAnimation') routeAnimation = true;
  @HostBinding('style.display') display = 'block';
  @HostBinding('style.position') position = 'absolute';
  @HostBinding('style.width') width = '80%';

  tableDef: any = {
    heads: [
      { key: "UserId", text: "ID", width: 50 },
      { key: "UserName", text: "用户名", width: 100 },
      { key: "UserNickName", text: "昵称", width: 100 },
      { key: "UserPhone", text: "手机号", width: 100 },
      {
        key: "UserSex", text: "性别", width: 100, render: (byteSex) => {
          return 1 === byteSex ? "男" : 2 === byteSex ? "女" : "未知";
        }
      },
      { key: "UserAge", text: "年龄", width: 100 },
      { key: "UserProfession", text: "职业", width: 100 },
      { key: "UserAvatar", text: "头像", width: 100 },
      {
        key: "UserDisabled", text: "状态", width: 100, render: (disabled) => {
          return disabled ? "禁用" : "可用";
        }
      },
    ],
    ctrls: [
      { text: (idx) => this.data[idx].UserDisabled ? "恢复" : "禁用", handler: this.toggle.bind(this) }
    ],
    ctrlsWidth: 50
  };

  data: Array<any> = [];

  constructor(private tablelet: TableletService,
    private dao: DaoUtil,
    private rest: RestCode) {
  }

  ngOnInit() {
    const self = this;
    this.tablelet.getData(TableletService.BLOG_USER).subscribe(
      data => self.data = data
    );

    this.dao.getJSON(API.getAPI("user/list")).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        self.tablelet.setData(TableletService.BLOG_USER, retBody);
      }),
      err => DaoUtil.logError(err)
    );
  }

  toggle(idx) {
    let data = this.data[idx];
    const self = this;
    this.dao.getJSON(API.getAPI("user/disabled")(data.UserId, !data.UserDisabled)).subscribe(
      ret => this.rest.checkCode(ret, (retBody) => {
        data.UserDisabled = retBody;
        self.tablelet.addData(TableletService.BLOG_USER, idx, data);
      }),
      err => DaoUtil.logError(err)
    );
  }

}
