import { AfterViewChecked, Component, ElementRef, HostBinding, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'fallback',
  templateUrl: './fallback.component.html',
  styleUrls: ['./fallback.component.css']
})
export class FallbackComponent implements OnInit, AfterViewChecked {

  renderText: string;

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
