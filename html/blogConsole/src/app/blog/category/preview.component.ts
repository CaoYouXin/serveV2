import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TableletService } from "../../service/index";
import { DaoUtil, RestCode } from "../../http/index";
import { API } from "../../const/index";

@Component({
  selector: 'blog-category-preview',
  templateUrl: './preview.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css', './preview.component.css']
})
export class BlogCategoryPreviewComponent implements OnInit {

  mask: boolean;

  loading: boolean;

  categories: Array<any> = [];

  constructor(private tablelet: TableletService,
    private fb: FormBuilder,
    private dao: DaoUtil,
    private restCode: RestCode) {
  }

  ngOnInit() {
    const self = this;
    this.tablelet.getMaskStatus(TableletService.BLOG_CATEGORY).subscribe(
      msg => {
        if ('preview' !== msg.maskName) {
          return;
        }

        self.mask = msg.mask;
        if (!self.mask) {
          return;
        }

        self.loading = true;
        self.dao.getJSON(API.getAPI("category/list/client")).subscribe(
          ret => {
            self.loading = false;
            self.restCode.checkCode(ret, (retBody) => {
              self.categories = retBody;
            })
          },
          err => {
            self.loading = false;
            DaoUtil.logError(err);
          }
        );
      }
    );
  }

  clicked() {
    this.tablelet.setMaskStatus(TableletService.BLOG_CATEGORY, { maskName: 'preview', mask: false });
  }

}