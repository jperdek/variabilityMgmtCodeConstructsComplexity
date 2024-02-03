import { Injectable } from '@angular/core';
import { PuzzleControllerManagerService2 } from 'src/app/services/puzzleControllers/decoratorFabricMenuServices/puzzle-controller-manager2.service';
import { PuzzleControllerManagerService } from 'src/app/services/puzzleControllers/puzzle-controller-manager.service';
import { aop, hookName, createHook, unAop  } from 'to-aop';


@Injectable({
  providedIn: 'root'
})
export class MenuManagerService {

  menuManageHook: any;
  createdHook = false;
  serviceContext: PuzzleControllerManagerService | null = null;
  serviceArguments: any[] = [null, null, null, null];

  constructor() { }

  private getArguments(args: any): any[] {
    const callArguments: any[] = [null, null, null, null];
    if (this.serviceArguments[0] === "APPLY") {
      callArguments[0] = args.context.returnPuzzleService;
    }
    if (this.serviceArguments[1] === "APPLY") {
      callArguments[1] = args.context.helpPuzzleService;
    }
    if (this.serviceArguments[2] === "APPLY") {
      callArguments[2] = args.context.bringToFrontService;
    }
    if (this.serviceArguments[3] === "APPLY") {
      callArguments[3] = args.context.bringToBackService;
    }
    return callArguments;
  }

  public initializeHint(menuConfig: any): void {
    if (this.createdHook) {
      unAop(PuzzleControllerManagerService);
    }

    if (menuConfig["include"]) {
      this.serviceArguments[1] = "APPLY";
    } else {
      this.serviceArguments[1] = null;
    }

    this.menuManageHook = createHook(hookName.aroundMethod, 'applyToMe',  (args: any) => {
      this.serviceContext = args.context;

      const callArguemnts = this.getArguments(args);
      return new PuzzleControllerManagerService2(
        callArguemnts[0],
        callArguemnts[1],
        callArguemnts[2],
        callArguemnts[3]);
    });
    aop(PuzzleControllerManagerService , this.menuManageHook, { constructor: true });
    this.createdHook = true;
  }

  public initializeReturn(menuConfig: any): void {
    if (this.createdHook) {
      unAop(PuzzleControllerManagerService);
    }

    if (menuConfig["include"]) {
      this.serviceArguments[0] = "APPLY";
    } else {
      this.serviceArguments[0] = null;
    }

    this.menuManageHook = createHook(hookName.aroundMethod, 'applyToMe',  (args: any) => {
      this.serviceContext = args.context;

      const callArguemnts = this.getArguments(args);
      return new PuzzleControllerManagerService2(
        callArguemnts[0],
        callArguemnts[1],
        callArguemnts[2],
        callArguemnts[3]);
    });
    aop(PuzzleControllerManagerService , this.menuManageHook, { constructor: true });
    this.createdHook = true;
  }

  public initializeBringToFront(menuConfig: any): void {
    if (this.createdHook) {
      unAop(PuzzleControllerManagerService);
    }

    if (menuConfig["include"]) {
      this.serviceArguments[2] = "APPLY";
    } else {
      this.serviceArguments[2] = null;
    }

    this.menuManageHook = createHook(hookName.aroundMethod, 'applyToMe',  (args: any) => {
      this.serviceContext = args.context;

      const callArguemnts = this.getArguments(args);
      return new PuzzleControllerManagerService2(
        callArguemnts[0],
        callArguemnts[1],
        callArguemnts[2],
        callArguemnts[3]);
    });
    aop(PuzzleControllerManagerService , this.menuManageHook, { constructor: true });
    this.createdHook = true;
  }

  public initializeBringToBack(menuConfig: any): void {
    if (this.createdHook) {
      unAop(PuzzleControllerManagerService);
    }

    if (menuConfig["include"]) {
      this.serviceArguments[3] = "APPLY";
    } else {
      this.serviceArguments[3] = null;
    }

    this.menuManageHook = createHook(hookName.aroundMethod, 'applyToMe',  (args: any) => {
      this.serviceContext = args.context;

      const callArguemnts = this.getArguments(args);
      return new PuzzleControllerManagerService2(
        callArguemnts[0],
        callArguemnts[1],
        callArguemnts[2],
        callArguemnts[3]);
    });
    aop(PuzzleControllerManagerService, this.menuManageHook, { constructor: true });
    this.createdHook = true;
  }
}
