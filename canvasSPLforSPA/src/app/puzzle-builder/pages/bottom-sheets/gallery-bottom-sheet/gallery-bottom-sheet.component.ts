import { Component } from '@angular/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';

@Component({
  selector: 'app-gallery-bottom-sheet',
  templateUrl: './gallery-bottom-sheet.component.html',
  styleUrls: ['./gallery-bottom-sheet.component.scss']
})
@DecoratorTypesService.wholeBlockFile({"imageGallery": "true"})
export class GalleryBottomSheetComponent {

  constructor(private bottomSheetRef: MatBottomSheetRef<GalleryBottomSheetComponent>) {}

  public openLink(event: MouseEvent): void {
    this.bottomSheetRef.dismiss();
    event.preventDefault();
  }
}
