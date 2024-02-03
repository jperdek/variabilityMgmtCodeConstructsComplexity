import { Component, EventEmitter, Output } from '@angular/core';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';
import { ZoomManagementInterface } from 'src/app/models/zoomManagementInterface';
import { PuzzleManagerService } from 'src/app/services/puzzleGenerator/puzzle-manager.service';


@Component({
  selector: 'app-set-zoom-position',
  templateUrl: './set-zoom-position.component.html',
  styleUrls: ['./set-zoom-position.component.scss']
})
@DecoratorTypesService.wholeBlockFile({"zoomCoordinates": "true"})
export class SetZoomPositionComponent implements ZoomManagementInterface {

  centerX = 25;
  centerY = 25;

  @Output()
  centerXEmitter: EventEmitter<number> = new EventEmitter();

  @Output()
  centerYEmitter: EventEmitter<number> = new EventEmitter();
  constructor(private puzzleManagerService: PuzzleManagerService) {
    const zoomManagerService = this.puzzleManagerService.getZoomManagerService();
    zoomManagerService.initForComponent(this);
  }

  setCenterXAndY(x: number, y: number): void {
    this.centerX = x;
    this.centerY = y;
  }
}
