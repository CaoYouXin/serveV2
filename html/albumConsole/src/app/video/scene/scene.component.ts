import { Component, OnInit, ViewChild, ElementRef, HostListener } from '@angular/core';
import { WebGLRenderer, PerspectiveCamera, Vector3, Scene, Mesh, BoxGeometry, MeshBasicMaterial } from 'three';

@Component({
  selector: 'app-scene',
  templateUrl: './scene.component.html',
  styleUrls: ['./scene.component.css']
})
export class SceneComponent implements OnInit {

  private renderer: WebGLRenderer;
  private camera: PerspectiveCamera;
  private scene: Scene;

  @ViewChild('wrapper')
  wrapper: ElementRef;

  constructor() { }

  @HostListener("window:resize")
  handleResize() {
    const container = this.wrapper.nativeElement;
    this.renderer.setSize(container.offsetWidth, container.offsetHeight);
    this.camera.aspect = container.offsetWidth / container.offsetHeight;
    this.renderer.render(this.scene, this.camera);
  }

  ngOnInit() {
    const container = this.wrapper.nativeElement;
    this.renderer = new WebGLRenderer({ antialias: true });
    this.renderer.setSize(container.offsetWidth, container.offsetHeight);
    container.appendChild(this.renderer.domElement);

    this.camera = new PerspectiveCamera(
      45,
      container.offsetWidth / container.offsetHeight,
      1,
      10
    );
    this.camera.position.set(0, 0, 3);
    this.camera.lookAt(new Vector3(0, 0, 0));

    this.scene = new Scene();
    this.scene.add(new Mesh(new BoxGeometry(1, 1, 1), new MeshBasicMaterial({ color: '#0000ff' })));

    this.renderer.render(this.scene, this.camera);
  }

}
