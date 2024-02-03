import { DecoratorTypesService } from "../featureManagement/decoratorsVariationPointManagement/decorator-types.service";

export interface AlgorithmMap {
  name: string;
  instance: any;
}

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface PuzzleAlgorithm {
  divideToPuzzle(
    sourceCanvas: HTMLCanvasElement,
    targetCanvas: fabric.Canvas,
    photoCanvasWidth: number,
    photoCanvasHeight: number,
    boardCanvasWidth: number,
    boardCanvasHeight: number,
    radius: number,
    randomAngle: boolean): void
}
