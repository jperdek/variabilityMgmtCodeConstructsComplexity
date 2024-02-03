import { Injectable } from '@angular/core';
import { RoutingModelMock } from 'src/app/mockups/routing.mock';
import { GalleryBottomSheetComponent } from 'src/app/puzzle-builder/pages/bottom-sheets/gallery-bottom-sheet/gallery-bottom-sheet.component';
import { InsertTemplateImageBottomSheetComponent } from 'src/app/puzzle-builder/pages/bottom-sheets/insert-template-image-bottom-sheet/insert-template-image-bottom-sheet.component';
import { ZoomManagementBottomSheetComponent } from 'src/app/puzzle-builder/pages/bottom-sheets/zoom-management-bottom-sheet/zoom-management-bottom-sheet.component';
import { GalleryComponent } from 'src/app/puzzle-builder/pages/gallery/gallery.component';
import { InsertTemplateImageComponent } from 'src/app/puzzle-builder/pages/insert-template-image/insert-template-image.component';
import { ZoomManagementComponent } from 'src/app/puzzle-builder/pages/zoom-management/zoom-management.component';
import { createHook, hookName, aop, unAop } from 'to-aop';


@Injectable({
  providedIn: 'root'
})
export class VariableSettingsConfigService {

  private static createdHook = false;
  private static configList:any[] = [];

  constructor() { }

  public static manageImageLoaderConfig(config: any): void {
    if (VariableSettingsConfigService.createdHook) {
      unAop(RoutingModelMock);
    }

    if (config["include"]) {
      const loadImageConfig = {
        "name": "Load image",
        "path": "/puzzle/loadImage",
        "bottomSheetComponent": InsertTemplateImageBottomSheetComponent,
        "componentPathInModule": "loadImage",
        "componentRef": InsertTemplateImageComponent
      };
      if (JSON.stringify(VariableSettingsConfigService.configList).indexOf(JSON.stringify(loadImageConfig)) === -1) {
        VariableSettingsConfigService.configList.push(loadImageConfig);
      }
    }

    const manageMenuConfigHook = createHook(hookName.aroundMethod, 'getRoutingModelData',  (args: any) => {
      if (config["include"] || VariableSettingsConfigService.configList.length !== 0) {
        const menu = args.original();
        for (let configDocument of VariableSettingsConfigService.configList) {
          if (JSON.stringify(menu).indexOf(JSON.stringify(configDocument)) === -1) {
            menu.unshift(configDocument);
          }
        }
        return menu;
      }
      return args.context;
    });

    aop(RoutingModelMock, manageMenuConfigHook);
    VariableSettingsConfigService.createdHook = true;
  }

  public static manageGalleryConfig(config: any): void {
    if (VariableSettingsConfigService.createdHook) {
      unAop(RoutingModelMock);
    }

    if (config["include"]) {
      const loadGalleryConfig = {
        "name": "Gallery",
        "path": "/puzzle/gallery",
        "bottomSheetComponent": GalleryBottomSheetComponent,
        "componentPathInModule": "gallery",
        "componentRef": GalleryComponent
      };
      if (JSON.stringify(VariableSettingsConfigService.configList).indexOf(JSON.stringify(loadGalleryConfig)) === -1) {
        VariableSettingsConfigService.configList.push(loadGalleryConfig);
      }
    }

    const manageMenuConfigHook = createHook(hookName.aroundMethod, 'getRoutingModelData',  (args: any) => {
      if (config["include"] || VariableSettingsConfigService.configList.length !== 0) {
        const menu = args.original();
        for (let configDocument of VariableSettingsConfigService.configList) {
          if (JSON.stringify(menu).indexOf(JSON.stringify(configDocument)) === -1) {
            menu.unshift(configDocument);
          }
        }
        return menu;
      }
      return args.context;
    });

    aop(RoutingModelMock, manageMenuConfigHook);
    VariableSettingsConfigService.createdHook = true;
  }

  public static manageZoomConfig(config: any): void {
    if (VariableSettingsConfigService.createdHook) {
      unAop(RoutingModelMock);
    }

    if (config["include"]) {
      const loadGalleryConfig = {
        "name": "Zoom",
        "path": "/puzzle/zoom",
        "bottomSheetComponent": ZoomManagementBottomSheetComponent,
        "componentPathInModule": "zoom",
        "componentRef": ZoomManagementComponent
      };
      if (JSON.stringify(VariableSettingsConfigService.configList).indexOf(JSON.stringify(loadGalleryConfig)) === -1) {
        VariableSettingsConfigService.configList.push(loadGalleryConfig);
      }
    }

    const manageMenuConfigHook = createHook(hookName.aroundMethod, 'getRoutingModelData',  (args: any) => {
      if (config["include"] || VariableSettingsConfigService.configList.length !== 0) {
        const menu = args.original();
        for (let configDocument of VariableSettingsConfigService.configList) {
          if (JSON.stringify(menu).indexOf(JSON.stringify(configDocument)) === -1) {
            menu.unshift(configDocument);
          }
        }
        return menu;
      }
      return args.context;
    });

    aop(RoutingModelMock, manageMenuConfigHook);
    VariableSettingsConfigService.createdHook = true;
  }

  public getAdditionalRoutingConfigurationFile(): any[] {
    return VariableSettingsConfigService.configList;
  }
}
