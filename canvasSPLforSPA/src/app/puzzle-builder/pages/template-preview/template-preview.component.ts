import { Component } from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';
import { PuzzleManagerService } from 'src/app/services/puzzleGenerator/puzzle-manager.service';


@Component({
  selector: 'app-template-preview',
  templateUrl: './template-preview.component.html',
  styleUrls: ['./template-preview.component.scss']
})
@DecoratorTypesService.wholeBlockFile({})
export class TemplatePreviewComponent {

  constructor(private puzzleManagerService: PuzzleManagerService) { }

  public getTemplatePreviewImage(): SafeResourceUrl {
    const templatePreviewImage = this.puzzleManagerService.getTemplatePreviewImage();
    if (templatePreviewImage !== undefined) {
      return templatePreviewImage;
    }
    console.log('Error: no template preview image for puzzles!');
    return 'assets/test1.jpg';
  }
}