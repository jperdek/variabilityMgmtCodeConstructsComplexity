import { DecoratorTypesService } from '../featureManagement/decoratorsVariationPointManagement/decorator-types.service';
import { Point } from './point';

export enum Connection {
    hole,
    fill,
    none
}

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface Polygon {
    points: Point[];
    connections: Connection[];
    innerCircles: ImageData[];
}
