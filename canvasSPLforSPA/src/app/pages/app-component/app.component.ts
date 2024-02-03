import { Component } from '@angular/core';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"toOmitCompletely": "true"}, "[IMPORT=TreeManagerService]") var newA;
import { TreeManagerService } from 'src/app/featureManagement/tree-manager.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'puzzleToPlay';
  constructor(
@DecoratorTypesService.skipLineParameter({"toOmitCompletely": "true"})
private treeManagerService: TreeManagerService
    ) { }
}

// @ts-ignore
@DecoratorTypesService.skipLineFile({"toOmitCompletely": "false"}, "[NOTHING]") var newA;
//

