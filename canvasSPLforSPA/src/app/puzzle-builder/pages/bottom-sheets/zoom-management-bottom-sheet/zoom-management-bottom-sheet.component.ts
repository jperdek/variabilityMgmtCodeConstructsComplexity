import { Component } from '@angular/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';


@Component({
  selector: 'app-zoom-management-bottom-sheet',
  templateUrl: './zoom-management-bottom-sheet.component.html',
  styleUrls: ['./zoom-management-bottom-sheet.component.scss']
})
@DecoratorTypesService.wholeBlockFile({})
export class ZoomManagementBottomSheetComponent {

  constructor(private bottomSheetRef: MatBottomSheetRef<ZoomManagementBottomSheetComponent>) {}

  public openLink(event: MouseEvent): void {
    this.bottomSheetRef.dismiss();
    event.preventDefault();
  }
}
