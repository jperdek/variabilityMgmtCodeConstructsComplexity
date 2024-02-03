import { Injectable} from '@angular/core';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[IMPORT=Store]") var newA;
import { Store } from '@ngrx/store';
import { AlgorithmMap } from '../models/algorithmMap';
import { PuzzleAppState } from '../store';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[IMPORT=DrawBordersService]") var newA;
import { DrawBordersService } from './puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/draw-borders.service';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[IMPORT=PuzzleGeneratorQuadroService]") var newA;
import { PuzzleGeneratorQuadroService } from './puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/puzzle-generator-quadro.service';
// @ts-ignore
@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[IMPORT=ShufflePuzzlesService]") var newA;
import { ShufflePuzzlesService } from './utils/shuffle-puzzles.service';
import { DecoratorTypesService } from '../featureManagement/decoratorsVariationPointManagement/decorator-types.service';


@Injectable({
  providedIn: 'root'
})
@DecoratorTypesService.wholeBlockFile({})
export class GameConfigurationService {

  availableAlgorithms: AlgorithmMap[] = [];

  constructor(
    @DecoratorTypesService.skipLineParameter({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[ARG=drawBordersService]")
    private drawBordersService: DrawBordersService,
    @DecoratorTypesService.skipLineParameter({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[ARG=store]")
    private store: Store<PuzzleAppState>,
    @DecoratorTypesService.skipLineParameter({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[ARG=shufflePuzzlesService]")
    private shufflePuzzlesService: ShufflePuzzlesService
    ) {
      // @ts-ignore
      @DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[NOT=let puzzleQuatro = null]")
      let puzzleQuatro = {"name": "jigsaw", "instance": new PuzzleGeneratorQuadroService(this.drawBordersService, this.store, this.shufflePuzzlesService)};

      if (puzzleQuatro != null) this.availableAlgorithms.push(puzzleQuatro);
      // @ts-ignore
      @DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[NOT=let puzzleQuatro2 = null;]")
      let puzzleQuatro2 = {"name": "jigsaw2", "instance": new PuzzleGeneratorQuadroService(this.drawBordersService, this.store, this.shufflePuzzlesService)};

      if (puzzleQuatro2 != null) this.availableAlgorithms.push(puzzleQuatro2);
      return this.applyToMe();
    }

  applyToMe(): GameConfigurationService {
    return this;
  }

  getAlgorithms(): AlgorithmMap[] {
    return this.availableAlgorithms;
  }

  setAlgorithms(algorithms: AlgorithmMap[]): void {
    this.availableAlgorithms = algorithms;
  }
}
