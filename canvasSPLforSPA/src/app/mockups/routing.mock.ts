import { PuzzleChooserBottomSheetComponent } from "../puzzle-builder/pages/bottom-sheets/puzzle-chooser-bottom-sheet/puzzle-chooser-bottom-sheet.component";
import { TemplatePreviewBottomSheetComponent } from "../puzzle-builder/pages/bottom-sheets/template-preview-bottom-sheet/template-preview-bottom-sheet.component";
import { RoutingModel } from "../models/routingModel";
import { PuzzleChooserComponent } from "../puzzle-builder/pages/puzzle-chooser/puzzle-chooser.component";
import { TemplatePreviewComponent } from "../puzzle-builder/pages/template-preview/template-preview.component";
import { GameConfigurationComponent } from "../game-configuration/game-configuration.component";


// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"OR": { "zoomCoordinates": "true", "zoomValue": "true" }}, 'import { ZoomManagementComponent } from "../puzzle-builder/pages/zoom-management/zoom-management.component";') var newA;
//import { ZoomManagementComponent } from "../puzzle-builder/pages/zoom-management/zoom-management.component";
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"OR": { "zoomCoordinates": "true", "zoomValue": "true" }}, 'import { ZoomManagementBottomSheetComponent } from "../puzzle-builder/pages/bottom-sheets/zoom-management-bottom-sheet/zoom-management-bottom-sheet.component"') var newA;
//import { ZoomManagementBottomSheetComponent } from "../puzzle-builder/pages/bottom-sheets/zoom-management-bottom-sheet/zoom-management-bottom-sheet.component";

// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"imageGallery": "true"}, 'import { GalleryBottomSheetComponent } from "../puzzle-builder/pages/bottom-sheets/gallery-bottom-sheet/gallery-bottom-sheet.component";') var newA;
//import { GalleryBottomSheetComponent } from "../puzzle-builder/pages/bottom-sheets/gallery-bottom-sheet/gallery-bottom-sheet.component";
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"imageGallery": "true"}, 'import { GalleryComponent } from "../puzzle-builder/pages/gallery/gallery.component"') var newA;
//import { GalleryComponent } from "../puzzle-builder/pages/gallery/gallery.component";


let RoutingModelData: RoutingModel[] = [
  {
    "name": "Play",
    "path": "/puzzle/selectPuzzles",
    "bottomSheetComponent": PuzzleChooserBottomSheetComponent,
    "componentPathInModule": "selectPuzzles",
    "componentRef": PuzzleChooserComponent
  },
  {
    "name": "Preview",
    "path": "/puzzle/preview",
    "bottomSheetComponent": TemplatePreviewBottomSheetComponent,
    "componentPathInModule": "preview",
    "componentRef": TemplatePreviewComponent
  },
  {
    "name": "Config",
    "path": "/puzzle/config",
    "bottomSheetComponent": null,
    "componentPathInModule": "config",
    "componentRef": GameConfigurationComponent
  }
];

// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"imageGallery": "true"}, 'RoutingModelData.push({"name": "Gallery","path": "/puzzle/gallery","bottomSheetComponent": GalleryBottomSheetComponent,"componentPathInModule": "gallery","componentRef": GalleryComponent});') var newA;
//RoutingModelData.push({"name": "Gallery","path": "/puzzle/gallery","bottomSheetComponent": GalleryBottomSheetComponent,"componentPathInModule": "gallery","componentRef": GalleryComponent});

// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"OR": { "zoomCoordinates": "true", "zoomValue": "true" }}, 'RoutingModelData.push({"name": "Zoom", "path": "/puzzle/zoom", "bottomSheetComponent": ZoomManagementBottomSheetComponent, "componentPathInModule": "zoom", "componentRef": ZoomManagementComponent});') var newA;
//RoutingModelData.push({"name": "Zoom", "path": "/puzzle/zoom", "bottomSheetComponent": ZoomManagementBottomSheetComponent, "componentPathInModule": "zoom", "componentRef": ZoomManagementComponent});


export class RoutingModelMock {
  static getRoutingModelData(): RoutingModel[] {
    return RoutingModelData;
  }
}


// @ts-ignore
@DecoratorTypesService.skipLineFile({"imageGallery": "false"}, "[NOTHING]") var newA;
//
