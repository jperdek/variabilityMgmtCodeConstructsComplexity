import { Component} from '@angular/core';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';


@Component({
  selector: 'app-insert-template-image',
  templateUrl: './insert-template-image.component.html',
  styleUrls: ['./insert-template-image.component.scss']
})
@DecoratorTypesService.wholeBlockFile({})
export class InsertTemplateImageComponent {

}
