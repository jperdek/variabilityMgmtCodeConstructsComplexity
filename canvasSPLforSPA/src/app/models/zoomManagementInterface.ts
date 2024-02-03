import { DecoratorTypesService } from "../featureManagement/decoratorsVariationPointManagement/decorator-types.service";

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface ZoomManagementInterface {
    setCenterXAndY(x: number, y: number): void;
}
