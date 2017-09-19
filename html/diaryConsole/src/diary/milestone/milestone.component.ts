import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { TableletService } from "../../service";
import { API, DaoUtil, RestCode } from "../../http";

@Component({
  selector: 'diary-milestone',
  templateUrl: './milestone.component.html',
  styleUrls: ['./milestone.component.css']
})
export class MilestoneComponent implements OnInit {

  milestoneList: Array<any> = [];

  constructor(private router: Router,
    private tablelet: TableletService,
    private dao: DaoUtil,
    private rest: RestCode) { }

  ngOnInit() {
    const self = this;
    this.dao.getJSON(API.getAPI("milestone/list")(
      this.tablelet.getHandlingData(TableletService.PAGEs).DiaryPageId
    )).subscribe(
      ret => self.rest.checkCode(ret, retBody => {
        self.milestoneList = retBody.map(milestone => ({
          ...milestone,
          DiaryMilestoneDate: milestone.DiaryMilestoneDate.substring(0, 10)
        }));
      }),
      err => DaoUtil.logError(err)
      );
  }

  saveMilestone(milestone, idx) {
    milestone.error = '';
    if (milestone.DiaryMilestoneTitle.length <= 0) {
      milestone.error += '标题是必填项。';
    }

    if (milestone.DiaryMilestoneTitle.length > 255) {
      milestone.error += '标题的最大长度为255个字符。';
    }

    if (!milestone.DiaryMilestoneDate) {
      milestone.error += '日期是必填项。';
    }

    if (milestone.error) {
      return;
    }

    let data = {
      ...milestone,
      DiaryMilestoneDate: milestone.DiaryMilestoneDate + ' 00:00:00'
    };
    // delete data.error;
    // delete data.titleFocused;
    // console.log(data);

    const self = this;
    this.dao.postJSON(API.getAPI("milestone/save"), data).subscribe(
      ret => self.rest.checkCode(ret, retBody => {
        self.milestoneList[idx] = {
          ...retBody,
          DiaryMilestoneDate: retBody.DiaryMilestoneDate.substring(0, 10)
        };
      }),
      err => DaoUtil.logError(err)
    );
  }

  addMilestone() {
    this.milestoneList.push({
      DiaryMilestoneTitle: '',
      DiaryMilestoneDate: '',
      DiaryPageId: this.tablelet.getHandlingData(TableletService.PAGEs).DiaryPageId
    });
  }

}