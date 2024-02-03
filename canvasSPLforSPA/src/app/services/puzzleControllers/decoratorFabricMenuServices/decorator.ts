import { DecoratorTypesService } from "src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service";

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface Decorator {
  setDecorated(decorated: boolean): void;
  getDecorated(): boolean;
}
