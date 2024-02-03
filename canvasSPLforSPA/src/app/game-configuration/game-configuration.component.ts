import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { DecoratorTypesService } from '../featureManagement/decoratorsVariationPointManagement/decorator-types.service';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'ANTI-JIGSAW', 'JIGSAW2']"}, "import { Store } from '@ngrx/store';") var newA;
//import { Store } from '@ngrx/store';
import { AlgorithmMap } from '../models/algorithmMap';
import { GameConfigurationService } from '../services/game-configuration.service';
import { ManipulationHandlerService } from '../services/puzzleEvents/manipulation-handler.service';
import { ResizeHandlerService } from '../services/puzzleEvents/resize-handler.service';
import { ZoomManagerService } from '../services/puzzleEvents/zoom-manager.service';
import { ImageSizeManagerService } from '../services/puzzleGenerator/image-size-manager.service';
import { ManageGraphicsService } from '../services/puzzleGenerator/manage-puzzle.service';
import { PuzzleManagerService } from '../services/puzzleGenerator/puzzle-manager.service';

// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "import { DrawBordersService } from '../services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/draw-borders.service';") var newA;
//import { DrawBordersService } from '../services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/draw-borders.service';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "import { PuzzleGeneratorQuadroService } from '../services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/puzzle-generator-quadro.service';") var newA;
//import { PuzzleGeneratorQuadroService } from '../services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/puzzle-generator-quadro.service';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['ANTI-JIGSAW']"}, "import { DrawBordersService2 } from '../services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm2/draw-borders2.service';") var newA;
//import { DrawBordersService2 } from '../services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm2/draw-borders2.service';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['ANTI-JIGSAW']"}, "import { PuzzleGeneratorQuadroService2 } from '../services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm2/puzzle-generator-quadro2.service';") var newA;
//import { PuzzleGeneratorQuadroService2 } from '../services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm2/puzzle-generator-quadro2.service';
import { SetPuzzleAreaOnBoardService } from '../services/puzzleGenerator/set-puzzle-area-on-board.service';
import { DisableControlsService } from '../services/utils/disable-controls.service';
import { ServiceToSkipService } from '../featureManagement/decoratorsVariationPointManagement/service-to-skip.service';

// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'ANTI-JIGSAW', 'JIGSAW2']"}, "import { ShufflePuzzlesService } from '../services/utils/shuffle-puzzles.service';") var newA;
//import { ShufflePuzzlesService } from '../services/utils/shuffle-puzzles.service';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'ANTI-JIGSAW', 'JIGSAW2']"}, "import { PuzzleAppState } from '../store';") var newA;
//import { PuzzleAppState } from '../store';


@Component({
  selector: 'app-game-configuration',
  templateUrl: './game-configuration.component.html',
  styleUrls: ['./game-configuration.component.scss']
})
export class GameConfigurationComponent {

  configurationFormGroup = new FormGroup({
    algorithm: new FormControl("None", [])
  });

  constructor(
    @DecoratorTypesService.skipLineParameter({"puzzleAlgorithmType": "['JIGSAW', 'ANTI-JIGSAW', 'JIGSAW2']"}, "private store: Store<PuzzleAppState>,")
    private serviceToSkip: ServiceToSkipService, //private store: Store<PuzzleAppState>,
    @DecoratorTypesService.skipLineParameter({"puzzleAlgorithmType": "['JIGSAW', 'ANTI-JIGSAW', 'JIGSAW2']"}, "private shufflePuzzlesService: ShufflePuzzlesService,")
    private serviceToSkip2: ServiceToSkipService, //private shufflePuzzlesService: ShufflePuzzlesService,
    @DecoratorTypesService.skipLineParameter({"puzzleAlgorithmType": "['ANTI-JIGSAW']"}, "private drawBordersService2: DrawBordersService2,")
    private serviceToSkip3: ServiceToSkipService, //private drawBordersService2: DrawBordersService2,
    @DecoratorTypesService.skipLineParameter({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "private drawBordersService: DrawBordersService,")
    private serviceToSkip4: ServiceToSkipService, //private drawBordersService: DrawBordersService,

    private manageGraphicsService: ManageGraphicsService,
    private setPuzzleAreaOnBoardService: SetPuzzleAreaOnBoardService,
    private imageSizeManagerService: ImageSizeManagerService,
    private sanitizer: DomSanitizer,
    private manipulationHandlerService: ManipulationHandlerService,
    private resizeHandlerService: ResizeHandlerService,
    private zoomManagerService: ZoomManagerService,
    private disableControlsService: DisableControlsService,
    private gameConfiguration: GameConfigurationService
    ) {
      // @ts-ignore
      @DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'ANTI-JIGSAW', 'JIGSAW2']"}, "let algorithms: any = [];") var newA;
      //let algorithms: any = [];
      // @ts-ignore
      @DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW']"}, 'algorithms.push({"name": "Old jigsaw","instance": new PuzzleGeneratorQuadroService(this.drawBordersService, this.store, this.shufflePuzzlesService)});') var newA;
      //algorithms.push({"name": "Old jigsaw","instance": new PuzzleGeneratorQuadroService(this.drawBordersService, this.store, this.shufflePuzzlesService)});
      // @ts-ignore
      @DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['ANTI-JIGSAW']"}, 'algorithms.push({"name": "Anti jigsaw", "instance": new PuzzleGeneratorQuadroService2(this.drawBordersService2, this.store, this.shufflePuzzlesService)});') var newA;
      //algorithms.push({"name": "Anti jigsaw", "instance": new PuzzleGeneratorQuadroService2(this.drawBordersService2, this.store, this.shufflePuzzlesService)});
      // @ts-ignore
      @DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW2']"}, 'algorithms.push({"name": "Old jigsaw 2", "instance": new PuzzleGeneratorQuadroService(this.drawBordersService, this.store, this.shufflePuzzlesService)});') var newA;
      //algorithms.push({"name": "Old jigsaw 2", "instance": new PuzzleGeneratorQuadroService(this.drawBordersService, this.store, this.shufflePuzzlesService)});
      // @ts-ignore
      @DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'ANTI-JIGSAW', 'JIGSAW2']"}, 'this.gameConfiguration.setAlgorithms(algorithms);') var newA;
      //this.gameConfiguration.setAlgorithms(algorithms);
    }

  getAvailableAlgorithms(): AlgorithmMap[] {
    return this.gameConfiguration.getAlgorithms();
  }

  public startNewGame(): void {
    const algorithmsConfig = this.gameConfiguration.getAlgorithms()[Number(this.configurationFormGroup.controls.algorithm.value)].instance;
    if (algorithmsConfig !== null) {
      new PuzzleManagerService(algorithmsConfig,
      this.manageGraphicsService, this.setPuzzleAreaOnBoardService,
      this.imageSizeManagerService, this.sanitizer,
      this.manipulationHandlerService, this.resizeHandlerService,
      this.zoomManagerService, this.disableControlsService).startGame();
    }
  }
}
