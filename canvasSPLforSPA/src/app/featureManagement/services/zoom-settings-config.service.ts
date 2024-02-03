import { ComponentRef, Injectable } from '@angular/core';
import { createHook, hookName, aop, unAop } from 'to-aop';
import { ZoomBlockComponent } from 'src/app/puzzle-builder/components/zoom-block/zoom-block.component';
import { ComponentFactoryService } from '../utils/component-factory.service';
import { VariableSettingsConfigService } from './variable-settings-config.service';
import { SetZoomComponent } from 'src/app/puzzle-builder/pages/zoom-management/set-zoom/set-zoom.component';
import { ZoomManagementComponent } from 'src/app/puzzle-builder/pages/zoom-management/zoom-management.component';
import { SetZoomPositionComponent } from 'src/app/puzzle-builder/pages/zoom-management/set-zoom-position/set-zoom-position.component';


@Injectable({
  providedIn: 'root'
})
export class ZoomSettingsConfigService {

  private static createdHook = false;
  private static zoomingFunctionality:Function[] = [];

  constructor(private componentFactoryService:  ComponentFactoryService) { }

  public manageZoomConfig(config: any): void {
    const manageZoomConfigHook = createHook(hookName.aroundMethod, 'getCanvasElement',  (args: any) => {
      if (config["include"]) {
        const puzzleBoardHTML = args.original();
        const zoomRef = this.componentFactoryService.createComponent(ZoomBlockComponent, puzzleBoardHTML);
        return puzzleBoardHTML;
      }
      return args.context;
    });

    //aop(PuzzleBoardComponent , manageZoomConfigHook);
  }

  public initialize(config: any): void {
    this.manageZoomConfig(config);
    VariableSettingsConfigService.manageZoomConfig(config);
  }



  public instantiateZoomValue(zoomHTML: any, args: any, positionAfterHeading: Function): void {
    const zoomValueRef = this.componentFactoryService.createComponent(
      SetZoomComponent, zoomHTML, undefined, positionAfterHeading);
    zoomValueRef.instance.zoomEmitter.subscribe((result: number) => args.args[0].setZoomValue(result));
  }

  public instantiateZoomCoordinates(zoomHTML: any, args: any, positionAfterHeading: Function): void {
    const zoomCoordinatesRef = this.componentFactoryService.createComponent(
      SetZoomPositionComponent, zoomHTML, undefined, positionAfterHeading);

    zoomCoordinatesRef.instance.centerXEmitter.subscribe((result: number) => args.args[0].setCenterX(result));
    zoomCoordinatesRef.instance.centerYEmitter.subscribe((result: number) => args.args[0].setCenterY(result));
  }

  public initializeZoomValue(config: any): void {
    if (ZoomSettingsConfigService.createdHook) {
      unAop(ZoomManagementComponent);
    }

    if (ZoomSettingsConfigService.zoomingFunctionality.indexOf(this.instantiateZoomValue) === -1) {
      ZoomSettingsConfigService.zoomingFunctionality.push(this.instantiateZoomValue);
    }

    const manageZoomValueConfigHook = createHook(hookName.aroundMethod, 'getComponentElement',  (args: any) => {
      if (config["include"]) {
        const zoomHTML = args.original(args.args[0]);
        const positionAfterHeading = function<T>(domLocation: HTMLElement, componentRef: ComponentRef<T>) {
          domLocation.insertBefore(componentRef.location.nativeElement, domLocation.children.item(1));
        }


        for(let func of ZoomSettingsConfigService.zoomingFunctionality) {
          func.call(this, zoomHTML, args, positionAfterHeading);
        }

        return zoomHTML;
      }
      return args.context;
    });

    aop(ZoomManagementComponent, manageZoomValueConfigHook);
    ZoomSettingsConfigService.createdHook = true;
  }

  public initializeZoomCoordinates(config: any): void {
    if (ZoomSettingsConfigService.createdHook) {
      unAop(ZoomManagementComponent);
    }

    if (ZoomSettingsConfigService.zoomingFunctionality.indexOf(this.instantiateZoomCoordinates) === -1) {
      ZoomSettingsConfigService.zoomingFunctionality.push(this.instantiateZoomCoordinates);
    }

    const manageZoomValueConfigHook = createHook(hookName.aroundMethod, 'getComponentElement',  (args: any) => {
      if (config["include"]) {
        const zoomHTML = args.original(args.args[0]);
        const positionAfterHeading = function<T>(domLocation: HTMLElement, componentRef: ComponentRef<T>) {
          domLocation.insertBefore(componentRef.location.nativeElement, domLocation.children.item(1));
        }

        for(let func of ZoomSettingsConfigService.zoomingFunctionality) {
          func.call(this, zoomHTML, args, positionAfterHeading);
        }

        return zoomHTML;
      }
      return args.context;
    });

    aop(ZoomManagementComponent, manageZoomValueConfigHook);
    ZoomSettingsConfigService.createdHook = true;
  }
}
