import { fabric } from 'fabric';
import { Puzzle } from '../store/puzzles/puzzles';
import { DecoratorTypesService } from '../featureManagement/decoratorsVariationPointManagement/decorator-types.service';

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface ExtendedPuzzle extends fabric.Image {
    puzzleData: Puzzle;

    dragPointer: fabric.Point | undefined;

    // for responsiveness
    previousCanvasWidth: number | undefined;
    previousCanvasHeight: number | undefined;
}
