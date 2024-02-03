import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InitialPageComponent } from './puzzle-builder/pages/initial-page/initial-page.component';
import { PuzzleBuilderComponent } from './puzzle-builder/pages/puzzle-builder-component/puzzle-builder.component';
import { DecoratorTypesService } from './featureManagement/decoratorsVariationPointManagement/decorator-types.service';

const routes: Routes = [
  {
    path: '',
    component: InitialPageComponent,
    loadChildren: () => import('./puzzle-builder/puzzle-builder.module').then(m => m.PuzzleBuilderModule)
  },
  {
    path: 'puzzle',
    component: PuzzleBuilderComponent,
    loadChildren: () => import('./puzzle-builder/puzzle-builder.module').then(m => m.PuzzleBuilderModule)
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

@DecoratorTypesService.wholeBlockFile({})
export class AppRoutingModule { }
