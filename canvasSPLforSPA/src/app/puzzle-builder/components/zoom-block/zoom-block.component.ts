import { Component, OnInit } from '@angular/core';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';

@Component({
  selector: 'app-zoom-block',
  templateUrl: './zoom-block.component.html',
  styleUrls: ['./zoom-block.component.scss']
})
@DecoratorTypesService.wholeBlockFile({"zoom": "true"})
export class ZoomBlockComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
