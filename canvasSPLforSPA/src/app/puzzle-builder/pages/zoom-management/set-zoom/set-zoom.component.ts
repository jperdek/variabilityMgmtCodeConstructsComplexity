import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';


@Component({
  selector: 'app-set-zoom',
  templateUrl: './set-zoom.component.html',
  styleUrls: ['./set-zoom.component.scss']
})
@DecoratorTypesService.wholeBlockFile({"zoomValue": "true"})
export class SetZoomComponent  {

  @Output()
  zoomEmitter: EventEmitter<number> = new EventEmitter();

  zoomValue: number = 1.0;

  constructor() {
  }

  setZoomFromDefaultToPoint(): void {
    this.zoomEmitter.emit(this.zoomValue);
  }
}
