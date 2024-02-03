import { Injectable } from '@angular/core';
import { fabric } from 'fabric';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';

@Injectable({
  providedIn: 'root'
})
@DecoratorTypesService.wholeBlockFile({})
export class DisableControlsService {

  public removeScalingOptions(img: fabric.Image): void {
    img.lockScalingX = true;
    img.lockScalingY = true;

    img.setControlsVisibility({
      mt: false, mb: false, ml: false, mr: false,
      tl: false, tr: false, bl: false, br: false
    });
  }

  public removeScalingOptionsForGroups(): void {
    fabric.Group.prototype.lockScalingX = true;
    fabric.Group.prototype.lockScalingY = true;

    fabric.Group.prototype.setControlsVisibility({
      mt: false, mb: false, ml: false, mr: false,
      tl: false, tr: false, bl: false, br: false
    });
  }
}
