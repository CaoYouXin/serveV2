import {Component} from "@angular/core";

@Component({
  selector: 'controller',
  templateUrl: './controller.component.html'
})
export class ControllerComponent {

  tableDef:any={
    heads: ["hello", "world", "ha"],
    width: [100, 200, 300]
  };

  data: Array<Array<any>> = [["hello1 hello1 hello1 hello1", "world1", "ha"], ["hello2", "world2", "ha"]];

}
