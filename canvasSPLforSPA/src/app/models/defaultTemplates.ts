import { DecoratorTypesService } from "../featureManagement/decoratorsVariationPointManagement/decorator-types.service";

export interface TemplateCategory {
    name: string;
    images: TemplateImage[];
}

@DecoratorTypesService.wholeBlockFile({})
class DecoratorFileCopy {}

export interface TemplateImage {
    title: string;
    quality: string;
    src: string;
}
