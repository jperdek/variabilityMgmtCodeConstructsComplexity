import { DecoratorTypesService } from "../featureManagement/decoratorsVariationPointManagement/decorator-types.service";

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface RoutingModel {
  name: string;
  path: string;
  bottomSheetComponent: any,
  componentPathInModule: string,
  componentRef: any
}
