import { Component, Input, ViewChild, ElementRef, OnInit } from "@angular/core"; 

@Component({
  selector: 'blog-category-item',
  templateUrl: './component.html',
  styleUrls: ['./component.css']
})
export class BlogCategoryItemComponent implements OnInit {

  @Input("config")
  config: any;

  @Input("tabCount")
  tabCount: number;

  @ViewChild("item")
  item: ElementRef;

  ngOnInit() {
    this.item.nativeElement.style.textIndent = this.tabCount + 'em';
  }

}