import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SceneComponent } from './scene/scene.component';
import { GeneratorComponent } from './generator/generator.component';
import { SettingComponent } from './setting/setting.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [SceneComponent, GeneratorComponent, SettingComponent]
})
export class VideoModule { }
