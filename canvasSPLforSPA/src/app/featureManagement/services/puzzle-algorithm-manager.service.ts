import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { GameConfigurationService } from 'src/app/services/game-configuration.service';
import { DrawBordersService } from 'src/app/services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/draw-borders.service';
import { PuzzleGeneratorQuadroService } from 'src/app/services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/puzzle-generator-quadro.service';
import { DrawBordersService2 } from 'src/app/services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm2/draw-borders2.service';
import { PuzzleGeneratorQuadroService2 } from 'src/app/services/puzzleGenerator/puzzleRenderingAlgorithm/algorithm2/puzzle-generator-quadro2.service';
import { ShufflePuzzlesService } from 'src/app/services/utils/shuffle-puzzles.service';
import { PuzzleAppState } from 'src/app/store';
import { aop, hookName, createHook  } from 'to-aop';



@Injectable({
  providedIn: 'root'
})
export class PuzzleAlgorithmManagerService {

  puzzleAlgorithmHook: any;
  serviceContext: GameConfigurationService | null = null;

  constructor(
    private store: Store<PuzzleAppState>,
    private shufflePuzzlesService: ShufflePuzzlesService,
    private drawBordersService2: DrawBordersService2,
    private drawBordersService: DrawBordersService) { }


  // store needs to be initialzied apart this class with all dependecies
  public initialize(config: any): void {
    console.log('Initializing algorithm variants!');
    const newGameConfigurationService = new GameConfigurationService(this.drawBordersService, this.store, this.shufflePuzzlesService);

    this.puzzleAlgorithmHook = createHook(hookName.aroundMethod, 'applyToMe',  (args: any) => {
      console.log('AOP: Selecting new Puzzle algorithms!!!');
      this.serviceContext = args.context;
      if (config["include"]) {
        const algorithms = [];
        //CYCLE!!!
        //const newGameConfigurationService = new GameConfigurationService(this.drawBordersService, this.store, this.shufflePuzzlesService);


        if (config["includeOptions"].indexOf("ANTI-JIGSAW") > -1) {
          algorithms.push({
            "name": "Anti jigsaw",
            "instance": new PuzzleGeneratorQuadroService2(this.drawBordersService2, this.store, this.shufflePuzzlesService)
          });
        }

        if (config["includeOptions"].indexOf("JIGSAW") > -1) {
          algorithms.push({
            "name": "Old jigsaw",
            "instance": new PuzzleGeneratorQuadroService(this.drawBordersService, this.store, this.shufflePuzzlesService)
          });
        }

        if (config["includeOptions"].indexOf("JIGSAW2") > -1) {
          algorithms.push({
            "name": "Old jigsaw 2",
            "instance": new PuzzleGeneratorQuadroService(this.drawBordersService, this.store, this.shufflePuzzlesService)
          });
        }
        newGameConfigurationService.setAlgorithms(algorithms);
        return newGameConfigurationService;
      }
      return args.context;
    });
    aop(GameConfigurationService , this.puzzleAlgorithmHook, { constructor: true });
  }
}
