import { Component, Input, Output, EventEmitter, OnChanges, HostBinding } from "@angular/core";

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

  @Output()
  handler: EventEmitter<any> = new EventEmitter();

  loading: boolean;

  ngOnChanges(simple) {
    if (simple.buttonMode) {
      if (!simple.buttonMode.currentValue) {
        this.loading = true;
      }
    }
  }

}