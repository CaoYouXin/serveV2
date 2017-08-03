import { Component, OnInit } from "@angular/core";
import { ResourceManangerService } from "../../service/index";

@Component({
  selector: 'resource-manager-mask',
  templateUrl: './mask.component.html',
  styleUrls: ['../../common-style/mask&btns.component.css']
})
export class ResourceManangerMaskComponent implements OnInit {

  mask: boolean;
  loading: boolean;

  constructor(private service: ResourceManangerService){
  }

  ngOnInit() {
    const self = this;
    this.service.getMaskStatus().subscribe(
      status => {
        self.mask = status;
      }
    );
  }

  add() {
    this.loading = true;
  }

  cancel() {
    this.service.setMaskStatus(false);
    this.loading = false;
  }

}