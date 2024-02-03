import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Route, RouterModule} from '@angular/router';
import RoutingModelDataAll from "../mockups/routing-all.mock";
import { DecoratorTypesService } from '../featureManagement/decoratorsVariationPointManagement/decorator-types.service';

const routes = RoutingModelDataAll.map((model: any) => model = {
  "path": model.componentPathInModule,
  "component": model.componentRef} as Route);
/*
const routes: Routes = RoutingModelMock.getRoutingModelData().map((model: any) => model = {
  "path": model.componentPathInModule,
  "component": model.componentRef} as Route);
console.log(routes);

const routeProvider = {
  provide: ROUTES,
  useFactory: () => {
      return [
        ...routes
      ];
  },
  multi: true
};
*/

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
  //providers: [
  //  routeProvider
  //]
})
@DecoratorTypesService.wholeBlockFile({})
export class PuzzleBuilderRoutingModule { }
