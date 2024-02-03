import { Injectable } from '@angular/core';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';

@Injectable({
  providedIn: 'root'
})
@DecoratorTypesService.wholeBlockFile({})
export class RandomUtilsService {

  public static randomNumber(min: number, max: number): number {
    return Math.random() * (max - min) + min;
  }
}
