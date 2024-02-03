import { Directive, ElementRef } from '@angular/core';
import { DecoratorTypesService } from '../featureManagement/decoratorsVariationPointManagement/decorator-types.service';

@Directive({
  selector: '[appRemove]'
})
@DecoratorTypesService.wholeBlockFile({})
export class RemoveDirective {

  constructor(element: ElementRef) {
    element.nativeElement.addEventListener('click', () => {
      element.nativeElement.remove();
   });
  }
}
