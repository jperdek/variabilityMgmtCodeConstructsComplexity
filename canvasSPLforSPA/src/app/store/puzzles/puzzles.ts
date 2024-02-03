import { DecoratorTypesService } from "src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service";

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface Puzzle {
    id: string;
    positionIndex: number;
    puzzleImageSrc: string;
    width: number; height: number;
    rotateAngle: number;
    positionLeftOnImage: number;  positionTopOnImage: number;
    boardCanvasWidth: number; boardCanvasHeight: number;
    imageCanvasWidth: number; imageCanvasHeight: number;
}
