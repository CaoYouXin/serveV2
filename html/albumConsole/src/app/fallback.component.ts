import { AfterViewChecked, Component, ElementRef, HostBinding, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'fallback',
  templateUrl: './fallback.component.html',
  styleUrls: ['./fallback.component.css']
})
export class FallbackComponent implements OnInit, AfterViewChecked {

  renderText: string;

  myCode: string = `
const minDiff = (nums) => {
  let sum = nums.reduce((pv, cv) => pv + cv, 0);
  console.log('total : ' +  sum);

  let half = Math.floor(sum / 2);
  let dp = [true];

  for (let i = 0; i < nums.length; i++) {
      for (let j = half; j >= nums[i]; j--) {
          dp[j] = dp[j] || dp[j - nums[i]];
      }
  }

  for (let k = half; k >= 0; k--) {
      if (dp[k]) {
          return (half - k) * 2 + (sum % 2 === 0 ? 0 : 1);
      }
  }
  return -1;
}
  `;

  @ViewChild("render")
  render: ElementRef;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    const self = this;
    this.route.data.subscribe((data: { width: string, name: string }) => {
      self.renderText = data.name;
    });
  }

  ngAfterViewChecked(): void {
    const self = this;
    this.route.data.subscribe((data: { color: string }) => {
      self.render.nativeElement.style.backgroundColor = data.color;
    });
  }

}
