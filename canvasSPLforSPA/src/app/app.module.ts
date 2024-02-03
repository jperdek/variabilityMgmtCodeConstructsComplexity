import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './pages/app-component/app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { MainNavigationComponent } from './components/main-navigation/main-navigation.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { EffectsModule } from '@ngrx/effects';
import { environment } from 'src/environments/environment';
import { StoreModule } from '@ngrx/store';
import { reducers } from './store';
import { AppEffects } from './effects/app.effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { InitialPageComponent } from './puzzle-builder/pages/initial-page/initial-page.component';
import { SmallMainMenuComponent } from './components/small-main-menu/small-main-menu.component';
import { PuzzleBuilderModule } from './puzzle-builder/puzzle-builder.module';
import { DecoratorTypesService } from './featureManagement/decoratorsVariationPointManagement/decorator-types.service';


@NgModule({
  declarations: [
    AppComponent,
    MainNavigationComponent,
    InitialPageComponent,
    SmallMainMenuComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FlexLayoutModule,
    PuzzleBuilderModule,
    StoreModule.forRoot(reducers),
    EffectsModule.forRoot([AppEffects]),
    !environment.production ? StoreDevtoolsModule.instrument() : []
  ],
  providers: [],
  bootstrap: [AppComponent]
})

@DecoratorTypesService.wholeBlockFile({})
export class AppModule { }
