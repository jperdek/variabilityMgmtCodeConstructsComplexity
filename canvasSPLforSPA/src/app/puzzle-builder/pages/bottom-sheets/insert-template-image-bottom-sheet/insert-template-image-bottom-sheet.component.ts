import { Component } from '@angular/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';


@Component({
  selector: 'app-insert-template-image-bottom-sheet',
  templateUrl: './insert-template-image-bottom-sheet.component.html',
  styleUrls: ['./insert-template-image-bottom-sheet.component.scss']
})
@DecoratorTypesService.wholeBlockFile({})
export class InsertTemplateImageBottomSheetComponent {

  constructor(private bottomSheetRef: MatBottomSheetRef<InsertTemplateImageBottomSheetComponent>) {}

  public openLink(event: MouseEvent): void {
    this.bottomSheetRef.dismiss();
    event.preventDefault();
  }
}
