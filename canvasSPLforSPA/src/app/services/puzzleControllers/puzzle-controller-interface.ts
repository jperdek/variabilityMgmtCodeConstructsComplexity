import { Injectable } from "@angular/core";
import { ManageGraphicsService } from "../puzzleGenerator/manage-puzzle.service";
import { DecoratorTypesService } from "src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service";


@Injectable({
  providedIn: 'root'
})
@DecoratorTypesService.wholeBlockFile({})
export abstract class PuzzleController {
  abstract registerControllers(ManageGraphicsService: ManageGraphicsService): void;
}
