import { DecoratorTypesService } from "../featureManagement/decoratorsVariationPointManagement/decorator-types.service";

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface Point {
    x: number;
    y: number;
}
