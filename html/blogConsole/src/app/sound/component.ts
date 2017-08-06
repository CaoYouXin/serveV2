import { Component, ViewChild, ElementRef, OnInit } from "@angular/core";
import { SoundService } from "../service";

@Component({
  selector: 'sound',
  templateUrl: './component.html',
  styleUrls: ['./component.css']
})
export class SoundComponent implements OnInit {

  private sounds: any = {};

  @ViewChild("routeClick")
  routeClick: ElementRef;

  constructor(private service: SoundService) {
  }

  ngOnInit() {
    this.sounds[SoundService.ROUTE_CLICK] = this.routeClick.nativeElement;

    this.service.getCmd().subscribe(
      cmd => {
        let sound = this.sounds[cmd];
        if (sound) {
          sound = sound.cloneNode();
        }
        sound.play();
      }
    );
  }

}