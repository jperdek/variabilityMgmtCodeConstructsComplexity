import { Component } from '@angular/core';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';


@Component({
  selector: 'app-puzzle-builder',
  templateUrl: './puzzle-builder.component.html',
  styleUrls: ['./puzzle-builder.component.scss']
})
@DecoratorTypesService.wholeBlockFile({})
export class PuzzleBuilderComponent {

  constructor() { }

}
