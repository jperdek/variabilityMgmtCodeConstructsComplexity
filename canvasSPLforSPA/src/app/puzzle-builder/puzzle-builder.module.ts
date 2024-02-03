import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PuzzleBuilderRoutingModule } from './puzzle-builder-routing.module';
import { PuzzleBoardComponent } from './pages/puzzle-board/puzzle-board.component';
import { PuzzleBuilderComponent } from './pages/puzzle-builder-component/puzzle-builder.component';
import { MaterialModule } from '../material/material.module';
import { PuzzleChooserComponent } from './pages/puzzle-chooser/puzzle-chooser.component';
import { RemoveDirective } from '../directives/remove.directive';
import { TemplatePreviewComponent } from './pages/template-preview/template-preview.component';
import { InsertTemplateImageComponent } from './pages/insert-template-image/insert-template-image.component';
import { DragAndDropDirective } from '../directives/drag-and-drop.directive';
import { FlexLayoutModule } from '@angular/flex-layout';
import { PuzzleChooserBottomSheetComponent } from './pages/bottom-sheets/puzzle-chooser-bottom-sheet/puzzle-chooser-bottom-sheet.component';
import { TemplatePreviewBottomSheetComponent } from './pages/bottom-sheets/template-preview-bottom-sheet/template-preview-bottom-sheet.component';
import { InsertTemplateImageBottomSheetComponent } from './pages/bottom-sheets/insert-template-image-bottom-sheet/insert-template-image-bottom-sheet.component';

// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"OR": { "zoomCoordinates": "true", "zoomValue": "true" }}, "[IMPORT=ZoomManagementComponent]") var newA;
import { ZoomManagementComponent } from './pages/zoom-management/zoom-management.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"zoom": "true"}, "[IMPORT=ZoomMenuComponent]") var newA;
import { ZoomMenuComponent } from './components/zoom-menu/zoom-menu.component';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"imageGallery": "true"}, "[IMPORT=GalleryComponent]") var newA;
import { GalleryComponent } from './pages/gallery/gallery.component';
import { DragAndDropImageComponent } from './components/drag-and-drop-image/drag-and-drop-image.component';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"imageGallery": "true"}, "[IMPORT=GalleryBottomSheetComponent]") var newA;
import { GalleryBottomSheetComponent } from './pages/bottom-sheets/gallery-bottom-sheet/gallery-bottom-sheet.component';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"OR": { "zoomCoordinates": "true", "zoomValue": "true" }}, "[IMPORT=ZoomManagementBottomSheetComponent]") var newA;
import { ZoomManagementBottomSheetComponent } from './pages/bottom-sheets/zoom-management-bottom-sheet/zoom-management-bottom-sheet.component';
import { GameConfigurationComponent } from '../game-configuration/game-configuration.component';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"zoom": "true"}, "[IMPORT=ZoomBlockComponent]") var newA;
import { ZoomBlockComponent } from './components/zoom-block/zoom-block.component';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"zoomValue": "true"}, "[IMPORT=SetZoomComponent]") var newA;
import { SetZoomComponent } from './pages/zoom-management/set-zoom/set-zoom.component';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"zoomCoordinates": "true"}, "[IMPORT=SetZoomPositionComponent]") var newA;
import { SetZoomPositionComponent } from './pages/zoom-management/set-zoom-position/set-zoom-position.component';
import { DecoratorTypesService } from '../featureManagement/decoratorsVariationPointManagement/decorator-types.service';


// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"OR": { "zoomCoordinates": "true", "zoomValue": "true" }}, "[NOT=let declZoomManagementComponent = PuzzleBoardComponent;]")
const declZoomManagementComponent = ZoomManagementComponent;

 // @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"zoom": "true"}, "[NOT=let declZoomMenuComponent = PuzzleBoardComponent;]")
const declZoomMenuComponent = ZoomMenuComponent;

 // @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"imageGallery": "true"}, "[NOT=let declGalleryComponent = PuzzleBoardComponent;]")
const declGalleryComponent = GalleryComponent;

 // @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"imageGallery": "true"}, "[NOT=let declGalleryBottomSheetComponent = PuzzleBoardComponent;]")
const declGalleryBottomSheetComponent = GalleryBottomSheetComponent;

 // @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"OR": { "zoomCoordinates": "true", "zoomValue": "true" }}, "[NOT=let declZoomManagementBottomSheetComponent = PuzzleBoardComponent;]")
const declZoomManagementBottomSheetComponent = ZoomManagementBottomSheetComponent;

 // @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"zoom": "true"}, "[NOT=let declZoomBlockComponent = PuzzleBoardComponent;]")
const declZoomBlockComponent = ZoomBlockComponent;

 // @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"zoomValue": "true"}, "[NOT=let declSetZoomComponent = PuzzleBoardComponent;]")
const declSetZoomComponent = SetZoomComponent;

 // @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"zoomCoordinates": "true"}, "[NOT=let declSetZoomPositionComponent = PuzzleBoardComponent;]")
const declSetZoomPositionComponent = SetZoomPositionComponent;


@NgModule({
  declarations: [PuzzleBoardComponent,
    PuzzleBuilderComponent,
    PuzzleChooserComponent,
    RemoveDirective,
    TemplatePreviewComponent,
    InsertTemplateImageComponent,
    DragAndDropDirective,
    PuzzleChooserBottomSheetComponent,
    TemplatePreviewBottomSheetComponent,
    InsertTemplateImageBottomSheetComponent,
    declZoomManagementComponent,
    declZoomMenuComponent,
    declGalleryComponent,
    DragAndDropImageComponent,
    declGalleryBottomSheetComponent,
    declZoomManagementBottomSheetComponent,
    GameConfigurationComponent,
    declZoomBlockComponent,
    declSetZoomComponent,
    declSetZoomPositionComponent],
  imports: [CommonModule,
    PuzzleBuilderRoutingModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    FlexLayoutModule],
  exports: [InsertTemplateImageComponent]
})
@DecoratorTypesService.wholeBlockFile({})
export class PuzzleBuilderModule {
}
