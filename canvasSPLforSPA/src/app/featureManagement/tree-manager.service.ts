import { Injectable } from '@angular/core';
import { FeatureConfigLoaderService } from './feature-config-loader.service';
import { featureConfig } from './feature-config';
import { MenuManagerService } from './services/management-services/menu-manager.service';
import { PuzzleAlgorithmManagerService } from './services/puzzle-algorithm-manager.service';
import { VariableSettingsConfigService } from './services/variable-settings-config.service';
import { ZoomSettingsConfigService } from './services/zoom-settings-config.service';


@Injectable({
  providedIn: 'root'
})
export class TreeManagerService {

  //MAPPING NEEDS INIT method and CALLER otherwise instance specific variables are undefined!!!
  private functionalityMapping = {
    "puzzleAlgorithmType": { "method": this.puzzleAlgorithManagerService.initialize,   "service": this.puzzleAlgorithManagerService}, //later calls are without injected services
    "imageLoader":         { "method": VariableSettingsConfigService.manageImageLoaderConfig, "service": VariableSettingsConfigService},
    "imageGallery":        { "method": VariableSettingsConfigService.manageGalleryConfig, "service": VariableSettingsConfigService},
    "zoom":                { "method": this.zoomSettingsConfig.initialize, "service": this.zoomSettingsConfig},
    "zoomValue":           { "method": this.zoomSettingsConfig.initializeZoomValue, "service": this.zoomSettingsConfig},
    "zoomCoordinates":     { "method": this.zoomSettingsConfig.initializeZoomCoordinates, "service": this.zoomSettingsConfig},
    "returnItem":          { "method": this.menuManagerService.initializeReturn, "service": this.menuManagerService},
    "showPositionOnBoard": { "method": this.menuManagerService.initializeHint, "service": this.menuManagerService},
    "toBack":              { "method": this.menuManagerService.initializeBringToBack, "service": this.menuManagerService},
    "toFront":             { "method": this.menuManagerService.initializeBringToFront, "service": this.menuManagerService}
  }

  constructor(
    private menuManagerService: MenuManagerService,
    private puzzleAlgorithManagerService: PuzzleAlgorithmManagerService,
    private zoomSettingsConfig: ZoomSettingsConfigService) {
    //1. solution without parser
    //this.functionalityMapping["deleteItem"](featureConfig["environmentModule"]["applicationCore"]["item"]["controls"]["deleteItem"]);

    //2. solution with parser
    FeatureConfigLoaderService.parseConfig(this.functionalityMapping, featureConfig);
  }
}
