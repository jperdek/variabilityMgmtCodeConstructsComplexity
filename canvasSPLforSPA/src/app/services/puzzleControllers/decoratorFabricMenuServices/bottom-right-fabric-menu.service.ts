import { Injectable } from '@angular/core';
import { fabric } from 'fabric';
import { DecoratorTypesService } from 'src/app/featureManagement/decoratorsVariationPointManagement/decorator-types.service';


@Injectable({
  providedIn: 'root'
})
@DecoratorTypesService.wholeBlockFile({})
export class BottomRightFabricMenuService {
  private static _decorated: boolean = true;

  getDecorated(): boolean {
    return BottomRightFabricMenuService._decorated;
  }

  setDecorated(decorated: boolean): void {
    BottomRightFabricMenuService._decorated = decorated;
  }

  static createDecorator(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
    BottomRightFabricMenuService._decorated = false;
    target.setDecorated= function(decorated: boolean)  {
        this.decorated = decorated;
      }
    target.isDecorated = function()  { return this.decorated; }
  }

  static setDecorator(shouldApply: boolean) {
    return function (target: any, propertyKey: string, descriptor: PropertyDescriptor) {
        descriptor.value = function (...args: any[]) {
          target.decorated = shouldApply;
        }
      }
  }

  static applyDecoratorPick(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
    const originalValue = descriptor.value;
    descriptor.value = function (...args: any[]) {
      //some setting can be overwritten if are applied before function call
      const service  = this as typeof target.type;
      if (service.bringToFrontService !== null) {
        const result = originalValue.apply(this, args);

        const service  = this as typeof target.type;
        fabric.Object.prototype.controls.bottomRight = new fabric.Control(Object.assign(fabric.Object.prototype.controls.bottomRight,
          { render: service.drawControllerIcon(BottomRightFabricMenuService.getIcon(), service.cornerSize),
        }));

        return result;
      }
    }
  }

  static applyDecorator(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
    descriptor.value = function (...args: any[]) {
      //some setting can be overwritten if are applied before function call
      const service  = this as typeof target.type;
      if (service.bringToFrontService !== null) {
        fabric.Object.prototype.controls.bottomRight = new fabric.Control({
          x: 0.5,
          y: 0.5,
          offsetX: 16,
          offsetY: 16,
          cursorStyle: 'pointer',
          mouseUpHandler: service.bringToFrontService.bringToFront(),
          render: service.drawControllerIcon(BottomRightFabricMenuService.getIcon(), service.cornerSize),
        });
      }
    }
  }

  public static getIcon(): HTMLImageElement {
    const icon = 'data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KPHN2ZyB2ZXJzaW9uPSIxLjIiIHdpZHRoPSIxMzUuNzNtbSIgaGVpZ2h0PSIxMzUuNzNtbSIgdmlld0JveD0iMCAwIDEzNTczIDEzNTczIiBwcmVzZXJ2ZUFzcGVjdFJhdGlvPSJ4TWlkWU1pZCIgZmlsbC1ydWxlPSJldmVub2RkIiBzdHJva2Utd2lkdGg9IjI4LjIyMiIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczpvb289Imh0dHA6Ly94bWwub3Blbm9mZmljZS5vcmcvc3ZnL2V4cG9ydCIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiIHhtbG5zOnByZXNlbnRhdGlvbj0iaHR0cDovL3N1bi5jb20veG1sbnMvc3Rhcm9mZmljZS9wcmVzZW50YXRpb24iIHhtbG5zOnNtaWw9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvU01JTDIwLyIgeG1sbnM6YW5pbT0idXJuOm9hc2lzOm5hbWVzOnRjOm9wZW5kb2N1bWVudDp4bWxuczphbmltYXRpb246MS4wIiB4bWw6c3BhY2U9InByZXNlcnZlIj4KIDxkZWZzIGNsYXNzPSJDbGlwUGF0aEdyb3VwIj4KICA8Y2xpcFBhdGggaWQ9InByZXNlbnRhdGlvbl9jbGlwX3BhdGgiIGNsaXBQYXRoVW5pdHM9InVzZXJTcGFjZU9uVXNlIj4KICAgPHJlY3QgeD0iMCIgeT0iMCIgd2lkdGg9IjEzNTczIiBoZWlnaHQ9IjEzNTczIi8+CiAgPC9jbGlwUGF0aD4KICA8Y2xpcFBhdGggaWQ9InByZXNlbnRhdGlvbl9jbGlwX3BhdGhfc2hyaW5rIiBjbGlwUGF0aFVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+CiAgIDxyZWN0IHg9IjEzIiB5PSIxMyIgd2lkdGg9IjEzNTQ2IiBoZWlnaHQ9IjEzNTQ2Ii8+CiAgPC9jbGlwUGF0aD4KIDwvZGVmcz4KIDxkZWZzIGNsYXNzPSJUZXh0U2hhcGVJbmRleCI+CiAgPGcgb29vOnNsaWRlPSJpZDEiIG9vbzppZC1saXN0PSJpZDMgaWQ0IGlkNSBpZDYgaWQ3IGlkOCIvPgogPC9kZWZzPgogPGRlZnMgY2xhc3M9IkVtYmVkZGVkQnVsbGV0Q2hhcnMiPgogIDxnIGlkPSJidWxsZXQtY2hhci10ZW1wbGF0ZS01NzM1NiIgdHJhbnNmb3JtPSJzY2FsZSgwLjAwMDQ4ODI4MTI1LC0wLjAwMDQ4ODI4MTI1KSI+CiAgIDxwYXRoIGQ9Ik0gNTgwLDExNDEgTCAxMTYzLDU3MSA1ODAsMCAtNCw1NzEgNTgwLDExNDEgWiIvPgogIDwvZz4KICA8ZyBpZD0iYnVsbGV0LWNoYXItdGVtcGxhdGUtNTczNTQiIHRyYW5zZm9ybT0ic2NhbGUoMC4wMDA0ODgyODEyNSwtMC4wMDA0ODgyODEyNSkiPgogICA8cGF0aCBkPSJNIDgsMTEyOCBMIDExMzcsMTEyOCAxMTM3LDAgOCwwIDgsMTEyOCBaIi8+CiAgPC9nPgogIDxnIGlkPSJidWxsZXQtY2hhci10ZW1wbGF0ZS0xMDE0NiIgdHJhbnNmb3JtPSJzY2FsZSgwLjAwMDQ4ODI4MTI1LC0wLjAwMDQ4ODI4MTI1KSI+CiAgIDxwYXRoIGQ9Ik0gMTc0LDAgTCA2MDIsNzM5IDE3NCwxNDgxIDE0NTYsNzM5IDE3NCwwIFogTSAxMzU4LDczOSBMIDMwOSwxMzQ2IDY1OSw3MzkgMTM1OCw3MzkgWiIvPgogIDwvZz4KICA8ZyBpZD0iYnVsbGV0LWNoYXItdGVtcGxhdGUtMTAxMzIiIHRyYW5zZm9ybT0ic2NhbGUoMC4wMDA0ODgyODEyNSwtMC4wMDA0ODgyODEyNSkiPgogICA8cGF0aCBkPSJNIDIwMTUsNzM5IEwgMTI3NiwwIDcxNywwIDEyNjAsNTQzIDE3NCw1NDMgMTc0LDkzNiAxMjYwLDkzNiA3MTcsMTQ4MSAxMjc0LDE0ODEgMjAxNSw3MzkgWiIvPgogIDwvZz4KICA8ZyBpZD0iYnVsbGV0LWNoYXItdGVtcGxhdGUtMTAwMDciIHRyYW5zZm9ybT0ic2NhbGUoMC4wMDA0ODgyODEyNSwtMC4wMDA0ODgyODEyNSkiPgogICA8cGF0aCBkPSJNIDAsLTIgQyAtNywxNCAtMTYsMjcgLTI1LDM3IEwgMzU2LDU2NyBDIDI2Miw4MjMgMjE1LDk1MiAyMTUsOTU0IDIxNSw5NzkgMjI4LDk5MiAyNTUsOTkyIDI2NCw5OTIgMjc2LDk5MCAyODksOTg3IDMxMCw5OTEgMzMxLDk5OSAzNTQsMTAxMiBMIDM4MSw5OTkgNDkyLDc0OCA3NzIsMTA0OSA4MzYsMTAyNCA4NjAsMTA0OSBDIDg4MSwxMDM5IDkwMSwxMDI1IDkyMiwxMDA2IDg4Niw5MzcgODM1LDg2MyA3NzAsNzg0IDc2OSw3ODMgNzEwLDcxNiA1OTQsNTg0IEwgNzc0LDIyMyBDIDc3NCwxOTYgNzUzLDE2OCA3MTEsMTM5IEwgNzI3LDExOSBDIDcxNyw5MCA2OTksNzYgNjcyLDc2IDY0MSw3NiA1NzAsMTc4IDQ1NywzODEgTCAxNjQsLTc2IEMgMTQyLC0xMTAgMTExLC0xMjcgNzIsLTEyNyAzMCwtMTI3IDksLTExMCA4LC03NiAxLC02NyAtMiwtNTIgLTIsLTMyIC0yLC0yMyAtMSwtMTMgMCwtMiBaIi8+CiAgPC9nPgogIDxnIGlkPSJidWxsZXQtY2hhci10ZW1wbGF0ZS0xMDAwNCIgdHJhbnNmb3JtPSJzY2FsZSgwLjAwMDQ4ODI4MTI1LC0wLjAwMDQ4ODI4MTI1KSI+CiAgIDxwYXRoIGQ9Ik0gMjg1LC0zMyBDIDE4MiwtMzMgMTExLDMwIDc0LDE1NiA1MiwyMjggNDEsMzMzIDQxLDQ3MSA0MSw1NDkgNTUsNjE2IDgyLDY3MiAxMTYsNzQzIDE2OSw3NzggMjQwLDc3OCAyOTMsNzc4IDMyOCw3NDcgMzQ2LDY4NCBMIDM2OSw1MDggQyAzNzcsNDQ0IDM5Nyw0MTEgNDI4LDQxMCBMIDExNjMsMTExNiBDIDExNzQsMTEyNyAxMTk2LDExMzMgMTIyOSwxMTMzIDEyNzEsMTEzMyAxMjkyLDExMTggMTI5MiwxMDg3IEwgMTI5Miw5NjUgQyAxMjkyLDkyOSAxMjgyLDkwMSAxMjYyLDg4MSBMIDQ0Miw0NyBDIDM5MCwtNiAzMzgsLTMzIDI4NSwtMzMgWiIvPgogIDwvZz4KICA8ZyBpZD0iYnVsbGV0LWNoYXItdGVtcGxhdGUtOTY3OSIgdHJhbnNmb3JtPSJzY2FsZSgwLjAwMDQ4ODI4MTI1LC0wLjAwMDQ4ODI4MTI1KSI+CiAgIDxwYXRoIGQ9Ik0gODEzLDAgQyA2MzIsMCA0ODksNTQgMzgzLDE2MSAyNzYsMjY4IDIyMyw0MTEgMjIzLDU5MiAyMjMsNzczIDI3Niw5MTYgMzgzLDEwMjMgNDg5LDExMzAgNjMyLDExODQgODEzLDExODQgOTkyLDExODQgMTEzNiwxMTMwIDEyNDUsMTAyMyAxMzUzLDkxNiAxNDA3LDc3MiAxNDA3LDU5MiAxNDA3LDQxMiAxMzUzLDI2OCAxMjQ1LDE2MSAxMTM2LDU0IDk5MiwwIDgxMywwIFoiLz4KICA8L2c+CiAgPGcgaWQ9ImJ1bGxldC1jaGFyLXRlbXBsYXRlLTgyMjYiIHRyYW5zZm9ybT0ic2NhbGUoMC4wMDA0ODgyODEyNSwtMC4wMDA0ODgyODEyNSkiPgogICA8cGF0aCBkPSJNIDM0Niw0NTcgQyAyNzMsNDU3IDIwOSw0ODMgMTU1LDUzNSAxMDEsNTg2IDc0LDY0OSA3NCw3MjMgNzQsNzk2IDEwMSw4NTkgMTU1LDkxMSAyMDksOTYzIDI3Myw5ODkgMzQ2LDk4OSA0MTksOTg5IDQ4MCw5NjMgNTMxLDkxMCA1ODIsODU5IDYwOCw3OTYgNjA4LDcyMyA2MDgsNjQ4IDU4Myw1ODYgNTMyLDUzNSA0ODIsNDgzIDQyMCw0NTcgMzQ2LDQ1NyBaIi8+CiAgPC9nPgogIDxnIGlkPSJidWxsZXQtY2hhci10ZW1wbGF0ZS04MjExIiB0cmFuc2Zvcm09InNjYWxlKDAuMDAwNDg4MjgxMjUsLTAuMDAwNDg4MjgxMjUpIj4KICAgPHBhdGggZD0iTSAtNCw0NTkgTCAxMTM1LDQ1OSAxMTM1LDYwNiAtNCw2MDYgLTQsNDU5IFoiLz4KICA8L2c+CiAgPGcgaWQ9ImJ1bGxldC1jaGFyLXRlbXBsYXRlLTYxNTQ4IiB0cmFuc2Zvcm09InNjYWxlKDAuMDAwNDg4MjgxMjUsLTAuMDAwNDg4MjgxMjUpIj4KICAgPHBhdGggZD0iTSAxNzMsNzQwIEMgMTczLDkwMyAyMzEsMTA0MyAzNDYsMTE1OSA0NjIsMTI3NCA2MDEsMTMzMiA3NjUsMTMzMiA5MjgsMTMzMiAxMDY3LDEyNzQgMTE4MywxMTU5IDEyOTksMTA0MyAxMzU3LDkwMyAxMzU3LDc0MCAxMzU3LDU3NyAxMjk5LDQzNyAxMTgzLDMyMiAxMDY3LDIwNiA5MjgsMTQ4IDc2NSwxNDggNjAxLDE0OCA0NjIsMjA2IDM0NiwzMjIgMjMxLDQzNyAxNzMsNTc3IDE3Myw3NDAgWiIvPgogIDwvZz4KIDwvZGVmcz4KIDxnPgogIDxnIGlkPSJpZDIiIGNsYXNzPSJNYXN0ZXJfU2xpZGUiPgogICA8ZyBpZD0iYmctaWQyIiBjbGFzcz0iQmFja2dyb3VuZCIvPgogICA8ZyBpZD0iYm8taWQyIiBjbGFzcz0iQmFja2dyb3VuZE9iamVjdHMiLz4KICA8L2c+CiA8L2c+CiA8ZyBjbGFzcz0iU2xpZGVHcm91cCI+CiAgPGc+CiAgIDxnIGlkPSJjb250YWluZXItaWQxIj4KICAgIDxnIGlkPSJpZDEiIGNsYXNzPSJTbGlkZSIgY2xpcC1wYXRoPSJ1cmwoI3ByZXNlbnRhdGlvbl9jbGlwX3BhdGgpIj4KICAgICA8ZyBjbGFzcz0iUGFnZSI+CiAgICAgIDxnIGNsYXNzPSJHcm91cCI+CiAgICAgICA8ZyBjbGFzcz0iR3JvdXAiPgogICAgICAgIDxnIGNsYXNzPSJjb20uc3VuLnN0YXIuZHJhd2luZy5Qb2x5UG9seWdvblNoYXBlIj4KICAgICAgICAgPGcgaWQ9ImlkMyI+CiAgICAgICAgICA8cmVjdCBjbGFzcz0iQm91bmRpbmdCb3giIHN0cm9rZT0ibm9uZSIgZmlsbD0ibm9uZSIgeD0iNzUwMiIgeT0iNzUwMiIgd2lkdGg9IjYwMzciIGhlaWdodD0iNjAzNyIvPgogICAgICAgICAgPHBhdGggZmlsbD0icmdiKDQ1LDgyLDEyNCkiIHN0cm9rZT0ibm9uZSIgZD0iTSA3NTE2LDEzMzY5IEwgNzUwMiw3Njg2IDc1MDMsNzY2OSA3NTA1LDc2NTIgNzUxMCw3NjM2IDc1MTUsNzYyMCA3NTIyLDc2MDUgNzUzMSw3NTkxIDc1NDEsNzU3OCA3NTUyLDc1NjYgNzU2NCw3NTU1IDc1NzcsNzU0NSA3NTkxLDc1MzcgNzYwNiw3NTMwIDc2MjEsNzUyNCA3NjM3LDc1MTkgNzY1NCw3NTE3IDc2NzEsNzUxNiAxMzM1NCw3NTAyIDEzMzcxLDc1MDMgMTMzODgsNzUwNSAxMzQwNCw3NTEwIDEzNDIwLDc1MTUgMTM0MzUsNzUyMiAxMzQ0OSw3NTMxIDEzNDYyLDc1NDEgMTM0NzQsNzU1MiAxMzQ4NSw3NTY0IDEzNDk1LDc1NzcgMTM1MDMsNzU5MSAxMzUxMCw3NjA2IDEzNTE2LDc2MjEgMTM1MjEsNzYzNyAxMzUyMyw3NjU0IDEzNTI0LDc2NzEgMTM1MzgsMTMzNTQgMTM1MzcsMTMzNzEgMTM1MzUsMTMzODggMTM1MzAsMTM0MDQgMTM1MjUsMTM0MjAgMTM1MTgsMTM0MzUgMTM1MDksMTM0NDkgMTM0OTksMTM0NjIgMTM0ODgsMTM0NzQgMTM0NzYsMTM0ODUgMTM0NjMsMTM0OTUgMTM0NDksMTM1MDMgMTM0MzQsMTM1MTAgMTM0MTksMTM1MTYgMTM0MDMsMTM1MjEgMTMzODYsMTM1MjMgMTMzNjksMTM1MjQgODgzOCwxMzUzNSA4ODIwLDEzNTM0IDg4MDMsMTM1MzIgODc4NywxMzUyOCA4NzcxLDEzNTIyIDg3NTYsMTM1MTUgODc0MiwxMzUwNiA4NzI5LDEzNDk3IDg3MTcsMTM0ODYgODcwNiwxMzQ3NCA4Njk2LDEzNDYxIDg2ODcsMTM0NDcgODY4MCwxMzQzMiA4Njc0LDEzNDE2IDg2NzAsMTM0MDAgODY2OCwxMzM4MyA4NjY3LDEzMzY2IDg2NjcsMTMzNDggODY3MCwxMzMzMSA4Njc0LDEzMzE1IDg2ODAsMTMyOTkgODY4NywxMzI4NSA4Njk2LDEzMjcwIDg3MDUsMTMyNTcgODcxNiwxMzI0NSA4NzI4LDEzMjM0IDg3NDIsMTMyMjQgODc1NiwxMzIxNiA4NzcwLDEzMjA4IDg3ODYsMTMyMDMgODgwMiwxMzE5OCA4ODE5LDEzMTk2IDg4MzcsMTMxOTUgMTMxOTcsMTMxODQgMTMxODQsNzg0MyA3ODQzLDc4NTYgNzg1NiwxMzM2OCA3ODU2LDEzMzg1IDc4NTMsMTM0MDIgNzg0OSwxMzQxOCA3ODQzLDEzNDM0IDc4MzYsMTM0NDkgNzgyNywxMzQ2MyA3ODE4LDEzNDc2IDc4MDcsMTM0ODggNzc5NCwxMzQ5OSA3NzgxLDEzNTA5IDc3NjcsMTM1MTcgNzc1MiwxMzUyNCA3NzM3LDEzNTMwIDc3MjAsMTM1MzQgNzcwNCwxMzUzNyA3Njg2LDEzNTM4IDc2NjksMTM1MzcgNzY1MiwxMzUzNSA3NjM2LDEzNTMwIDc2MjAsMTM1MjUgNzYwNSwxMzUxOCA3NTkxLDEzNTA5IDc1NzgsMTM0OTkgNzU2NiwxMzQ4OCA3NTU1LDEzNDc2IDc1NDUsMTM0NjMgNzUzNywxMzQ0OSA3NTI5LDEzNDM0IDc1MjQsMTM0MTkgNzUxOSwxMzQwMyA3NTE3LDEzMzg2IDc1MTYsMTMzNjkgWiIvPgogICAgICAgICA8L2c+CiAgICAgICAgPC9nPgogICAgICAgIDxnIGNsYXNzPSJjb20uc3VuLnN0YXIuZHJhd2luZy5Qb2x5UG9seWdvblNoYXBlIj4KICAgICAgICAgPGcgaWQ9ImlkNCI+CiAgICAgICAgICA8cmVjdCBjbGFzcz0iQm91bmRpbmdCb3giIHN0cm9rZT0ibm9uZSIgZmlsbD0ibm9uZSIgeD0iODMzMiIgeT0iODMzMiIgd2lkdGg9IjQzNzciIGhlaWdodD0iNDM3NyIvPgogICAgICAgICAgPHBhdGggZmlsbD0icmdiKDQ1LDgyLDEyNCkiIHN0cm9rZT0ibm9uZSIgZD0iTSA4MzQyLDEyNTM3IEwgODMzMiw4NTEzIDgzMzMsODQ5NiA4MzM2LDg0NzkgODM0MCw4NDYyIDgzNDYsODQ0NyA4MzUzLDg0MzIgODM2MSw4NDE4IDgzNzEsODQwNSA4MzgyLDgzOTIgODM5NCw4MzgxIDg0MDcsODM3MSA4NDIxLDgzNjMgODQzNiw4MzU2IDg0NTIsODM1MCA4NDY4LDgzNDYgODQ4NSw4MzQzIDg1MDMsODM0MiAxMTcxMiw4MzM0IDExNzI5LDgzMzUgMTE3NDYsODMzOCAxMTc2Miw4MzQyIDExNzc4LDgzNDggMTE3OTMsODM1NSAxMTgwNyw4MzYzIDExODIwLDgzNzMgMTE4MzIsODM4NCAxMTg0Myw4Mzk2IDExODUzLDg0MDkgMTE4NjEsODQyMyAxMTg2OSw4NDM4IDExODc0LDg0NTQgMTE4NzksODQ3MCAxMTg4MSw4NDg3IDExODgyLDg1MDUgMTE4ODEsODUyMiAxMTg3OSw4NTM5IDExODc1LDg1NTUgMTE4NjksODU3MSAxMTg2Miw4NTg2IDExODUzLDg2MDAgMTE4NDQsODYxMyAxMTgzMyw4NjI1IDExODIxLDg2MzYgMTE4MDgsODY0NiAxMTc5NCw4NjU1IDExNzc5LDg2NjIgMTE3NjMsODY2OCAxMTc0Nyw4NjcyIDExNzMwLDg2NzUgMTE3MTMsODY3NiA4Njc0LDg2ODMgODY4MywxMjM2NiAxMjM2NiwxMjM1NyAxMjM1Nyw4NTAzIDEyMzU3LDg0ODYgMTIzNjAsODQ2OSAxMjM2NCw4NDUzIDEyMzcwLDg0MzcgMTIzNzcsODQyMiAxMjM4NSw4NDA4IDEyMzk1LDgzOTUgMTI0MDYsODM4MyAxMjQxOCw4MzcyIDEyNDMxLDgzNjIgMTI0NDUsODM1MyAxMjQ2MCw4MzQ2IDEyNDc2LDgzNDAgMTI0OTIsODMzNiAxMjUwOSw4MzMzIDEyNTI3LDgzMzIgMTI1NDQsODMzMyAxMjU2MSw4MzM2IDEyNTc4LDgzNDAgMTI1OTMsODM0NiAxMjYwOCw4MzUzIDEyNjIyLDgzNjEgMTI2MzUsODM3MSAxMjY0OCw4MzgyIDEyNjU5LDgzOTQgMTI2NjksODQwNyAxMjY3Nyw4NDIxIDEyNjg0LDg0MzYgMTI2OTAsODQ1MiAxMjY5NCw4NDY4IDEyNjk3LDg0ODUgMTI2OTgsODUwMyAxMjcwOCwxMjUyNyAxMjcwNywxMjU0NCAxMjcwNCwxMjU2MSAxMjcwMCwxMjU3OCAxMjY5NCwxMjU5MyAxMjY4NywxMjYwOCAxMjY3OSwxMjYyMiAxMjY2OSwxMjYzNSAxMjY1OCwxMjY0OCAxMjY0NiwxMjY1OSAxMjYzMywxMjY2OSAxMjYxOSwxMjY3NyAxMjYwNCwxMjY4NCAxMjU4OCwxMjY5MCAxMjU3MiwxMjY5NCAxMjU1NSwxMjY5NyAxMjUzNywxMjY5OCA4NTEzLDEyNzA4IDg0OTYsMTI3MDcgODQ3OSwxMjcwNCA4NDYyLDEyNzAwIDg0NDcsMTI2OTQgODQzMiwxMjY4NyA4NDE4LDEyNjc5IDg0MDUsMTI2NjkgODM5MiwxMjY1OCA4MzgxLDEyNjQ2IDgzNzEsMTI2MzMgODM2MywxMjYxOSA4MzU2LDEyNjA0IDgzNTAsMTI1ODggODM0NiwxMjU3MiA4MzQzLDEyNTU1IDgzNDIsMTI1MzcgWiIvPgogICAgICAgICA8L2c+CiAgICAgICAgPC9nPgogICAgICAgPC9nPgogICAgICAgPGcgY2xhc3M9Ikdyb3VwIj4KICAgICAgICA8ZyBjbGFzcz0iY29tLnN1bi5zdGFyLmRyYXdpbmcuUG9seVBvbHlnb25TaGFwZSI+CiAgICAgICAgIDxnIGlkPSJpZDUiPgogICAgICAgICAgPHJlY3QgY2xhc3M9IkJvdW5kaW5nQm94IiBzdHJva2U9Im5vbmUiIGZpbGw9Im5vbmUiIHg9Ijk4IiB5PSIxMjgiIHdpZHRoPSI2MDI3IiBoZWlnaHQ9IjYwMjciLz4KICAgICAgICAgIDxwYXRoIGZpbGw9InJnYig0NSw4MiwxMjQpIiBzdHJva2U9Im5vbmUiIGQ9Ik0gNjEyNCwzMDIgTCA2MTIwLDU5ODQgNjExOSw2MDAyIDYxMTcsNjAxOCA2MTEyLDYwMzUgNjEwNyw2MDUwIDYwOTksNjA2NSA2MDkxLDYwNzkgNjA4MSw2MDkyIDYwNzAsNjEwNCA2MDU4LDYxMTUgNjA0NSw2MTI1IDYwMzEsNjEzMyA2MDE2LDYxNDEgNjAwMCw2MTQ2IDU5ODQsNjE1MSA1OTY3LDYxNTMgNTk1MCw2MTU0IDI2OCw2MTUwIDI1MCw2MTQ5IDIzNCw2MTQ3IDIxNyw2MTQyIDIwMiw2MTM3IDE4Nyw2MTI5IDE3Myw2MTIxIDE2MCw2MTExIDE0OCw2MTAwIDEzNyw2MDg4IDEyNyw2MDc1IDExOSw2MDYxIDExMSw2MDQ2IDEwNiw2MDMwIDEwMSw2MDE0IDk5LDU5OTcgOTgsNTk4MCAxMDIsMjk4IDEwMywyODAgMTA1LDI2NCAxMTAsMjQ3IDExNSwyMzIgMTIzLDIxNyAxMzEsMjAzIDE0MSwxOTAgMTUyLDE3OCAxNjQsMTY3IDE3NywxNTcgMTkxLDE0OSAyMDYsMTQxIDIyMiwxMzYgMjM4LDEzMSAyNTUsMTI5IDI3MiwxMjggNDgwMywxMzEgNDgyMCwxMzIgNDgzNywxMzUgNDg1NCwxMzkgNDg2OSwxNDUgNDg4NCwxNTIgNDg5OCwxNjAgNDkxMSwxNzAgNDkyMywxODEgNDkzNCwxOTMgNDk0NCwyMDYgNDk1MywyMjAgNDk2MCwyMzUgNDk2NiwyNTEgNDk3MCwyNjcgNDk3MiwyODQgNDk3MywzMDEgNDk3MiwzMTggNDk3MCwzMzUgNDk2NiwzNTIgNDk2MCwzNjcgNDk1MywzODIgNDk0NCwzOTYgNDkzNCw0MDkgNDkyMyw0MjIgNDkxMSw0MzMgNDg5OCw0NDIgNDg4NCw0NTEgNDg2OSw0NTggNDg1Myw0NjQgNDgzNyw0NjggNDgyMCw0NzEgNDgwMyw0NzIgNDQyLDQ2OSA0MzksNTgxMCA1NzgwLDU4MTMgNTc4NCwzMDIgNTc4NCwyODQgNTc4NywyNjggNTc5MSwyNTEgNTc5NywyMzYgNTgwNCwyMjEgNTgxMywyMDcgNTgyMywxOTQgNTgzNCwxODIgNTg0NiwxNzEgNTg1OSwxNjEgNTg3MywxNTIgNTg4OCwxNDUgNTkwNCwxNDAgNTkyMCwxMzUgNTkzNywxMzMgNTk1NCwxMzIgNTk3MiwxMzMgNTk4OSwxMzUgNjAwNSwxNDAgNjAyMCwxNDUgNjAzNSwxNTMgNjA0OSwxNjEgNjA2MiwxNzEgNjA3NCwxODIgNjA4NSwxOTQgNjA5NSwyMDcgNjEwNCwyMjEgNjExMSwyMzYgNjExNiwyNTIgNjEyMSwyNjggNjEyMywyODUgNjEyNCwzMDIgWiIvPgogICAgICAgICA8L2c+CiAgICAgICAgPC9nPgogICAgICAgIDxnIGNsYXNzPSJjb20uc3VuLnN0YXIuZHJhd2luZy5Qb2x5UG9seWdvblNoYXBlIj4KICAgICAgICAgPGcgaWQ9ImlkNiI+CiAgICAgICAgICA8cmVjdCBjbGFzcz0iQm91bmRpbmdCb3giIHN0cm9rZT0ibm9uZSIgZmlsbD0ibm9uZSIgeD0iOTI2IiB5PSI5NTYiIHdpZHRoPSI0MzcxIiBoZWlnaHQ9IjQzNzEiLz4KICAgICAgICAgIDxwYXRoIGZpbGw9InJnYig0NSw4MiwxMjQpIiBzdHJva2U9Im5vbmUiIGQ9Ik0gNTI5NSwxMTMwIEwgNTI5Miw1MTU1IDUyOTEsNTE3MiA1Mjg5LDUxODkgNTI4NSw1MjA1IDUyNzksNTIyMSA1MjcyLDUyMzYgNTI2Myw1MjUwIDUyNTMsNTI2MyA1MjQyLDUyNzUgNTIzMCw1Mjg2IDUyMTcsNTI5NiA1MjAzLDUzMDUgNTE4OCw1MzEyIDUxNzIsNTMxOCA1MTU2LDUzMjIgNTEzOSw1MzI0IDUxMjIsNTMyNSAxOTEyLDUzMjMgMTg5NSw1MzIyIDE4NzgsNTMxOSAxODYyLDUzMTUgMTg0Niw1MzEwIDE4MzEsNTMwMiAxODE3LDUyOTQgMTgwNCw1Mjg0IDE3OTIsNTI3MyAxNzgxLDUyNjEgMTc3MSw1MjQ4IDE3NjMsNTIzNCAxNzU2LDUyMTkgMTc1MCw1MjAzIDE3NDYsNTE4NyAxNzQzLDUxNzAgMTc0Miw1MTUyIDE3NDMsNTEzNSAxNzQ2LDUxMTggMTc1MCw1MTAxIDE3NTYsNTA4NiAxNzYzLDUwNzEgMTc3Miw1MDU3IDE3ODEsNTA0NCAxNzkyLDUwMzEgMTgwNCw1MDIwIDE4MTgsNTAxMSAxODMyLDUwMDIgMTg0Niw0OTk1IDE4NjIsNDk4OSAxODc4LDQ5ODUgMTg5NSw0OTgyIDE5MTIsNDk4MiA0OTUxLDQ5ODQgNDk1NCwxMzAxIDEyNzEsMTI5OCAxMjY4LDUxNTIgMTI2Nyw1MTY5IDEyNjUsNTE4NiAxMjYwLDUyMDMgMTI1NSw1MjE4IDEyNDcsNTIzMyAxMjM5LDUyNDcgMTIyOSw1MjYwIDEyMTgsNTI3MyAxMjA2LDUyODQgMTE5Myw1MjkzIDExNzksNTMwMiAxMTY0LDUzMDkgMTE0OCw1MzE1IDExMzIsNTMxOSAxMTE1LDUzMjIgMTA5Nyw1MzIyIDEwODAsNTMyMSAxMDYzLDUzMTkgMTA0Nyw1MzE1IDEwMzEsNTMwOSAxMDE2LDUzMDIgMTAwMiw1MjkzIDk4OSw1MjgzIDk3Nyw1MjcyIDk2Niw1MjYwIDk1Niw1MjQ3IDk0Nyw1MjMzIDk0MCw1MjE4IDkzNCw1MjAyIDkzMCw1MTg2IDkyOCw1MTY5IDkyNyw1MTUyIDkzMCwxMTI3IDkzMSwxMTEwIDkzMywxMDkzIDkzNywxMDc3IDk0MywxMDYxIDk1MCwxMDQ2IDk1OSwxMDMyIDk2OSwxMDE5IDk4MCwxMDA3IDk5Miw5OTYgMTAwNSw5ODYgMTAxOSw5NzcgMTAzNCw5NzAgMTA1MCw5NjQgMTA2Niw5NjAgMTA4Myw5NTggMTEwMCw5NTcgNTEyNSw5NjAgNTE0Miw5NjEgNTE1OSw5NjMgNTE3NSw5NjcgNTE5MSw5NzMgNTIwNiw5ODAgNTIyMCw5ODkgNTIzMyw5OTkgNTI0NSwxMDEwIDUyNTYsMTAyMiA1MjY2LDEwMzUgNTI3NSwxMDQ5IDUyODIsMTA2NCA1Mjg4LDEwODAgNTI5MiwxMDk2IDUyOTQsMTExMyA1Mjk1LDExMzAgWiIvPgogICAgICAgICA8L2c+CiAgICAgICAgPC9nPgogICAgICAgPC9nPgogICAgICA8L2c+CiAgICAgIDxnIGNsYXNzPSJjb20uc3VuLnN0YXIuZHJhd2luZy5Qb2x5UG9seWdvblNoYXBlIj4KICAgICAgIDxnIGlkPSJpZDciPgogICAgICAgIDxyZWN0IGNsYXNzPSJCb3VuZGluZ0JveCIgc3Ryb2tlPSJub25lIiBmaWxsPSJub25lIiB4PSIzMzAxIiB5PSIzNTUxIiB3aWR0aD0iNjc1MyIgaGVpZ2h0PSI2NTAzIi8+CiAgICAgICAgPHBhdGggZmlsbD0icmdiKDIwNywyNDAsMTU4KSIgc3Ryb2tlPSJub25lIiBkPSJNIDMzMDIsMTAwNTIgTCAxMDA1MiwxMDA1MiAxMDA1MiwzNTUyIDMzMDIsMzU1MiAzMzAyLDEwMDUyIFoiLz4KICAgICAgIDwvZz4KICAgICAgPC9nPgogICAgICA8ZyBjbGFzcz0iR3JhcGhpYyI+CiAgICAgICA8ZyBpZD0iaWQ4Ij4KICAgICAgICA8cmVjdCBjbGFzcz0iQm91bmRpbmdCb3giIHN0cm9rZT0ibm9uZSIgZmlsbD0ibm9uZSIgeD0iMzI1MiIgeT0iMzQwMiIgd2lkdGg9IjY4MDEiIGhlaWdodD0iNjc1MSIvPgogICAgICAgIDxwYXRoIGZpbGw9InJnYigxNzksNjQsNzQpIiBzdHJva2U9Im5vbmUiIGQ9Ik0gOTg1MywxMDE0NSBMIDM0NTEsMTAxNDUgQyAzMzQ1LDEwMTQ1IDMyNTksMTAwNjAgMzI1OSw5OTU1IEwgMzI1OSwzNTk5IEMgMzI1OSwzNDk0IDMzNDUsMzQwOSAzNDUxLDM0MDkgTCA5ODUzLDM0MDkgQyA5OTU5LDM0MDkgMTAwNDUsMzQ5NCAxMDA0NSwzNTk5IEwgMTAwNDUsODY2NyBDIDEwMDQ1LDg3NzMgOTk1OSw4ODU4IDk4NTMsODg1OCA5NzQ3LDg4NTggOTY2MSw4NzczIDk2NjEsODY2NyBMIDk2NjEsMzc5MCAzNjQzLDM3OTAgMzY0Myw5NzY0IDk4NTMsOTc2NCBDIDk5NTksOTc2NCAxMDA0NSw5ODQ5IDEwMDQ1LDk5NTUgMTAwNDUsMTAwNjAgOTk1OSwxMDE0NSA5ODUzLDEwMTQ1IEwgOTg1MywxMDE0NSBaIi8+CiAgICAgICAgPHBhdGggZmlsbD0icmdiKDE3OSw2NCw3NCkiIHN0cm9rZT0ibm9uZSIgZD0iTSA4OTIwLDkyMTkgTCA0Mzg0LDkyMTkgQyA0Mjc4LDkyMTkgNDE5Miw5MTMzIDQxOTIsOTAyOCBMIDQxOTIsNTQzOCBDIDQxOTIsNTMzMyA0Mjc4LDUyNDcgNDM4NCw1MjQ3IDQ0OTEsNTI0NyA0NTc3LDUzMzMgNDU3Nyw1NDM4IEwgNDU3Nyw4ODM3IDg3MjcsODgzNyA4NzI3LDQ3MTcgNDM4NCw0NzE3IEMgNDI3OCw0NzE3IDQxOTIsNDYzMiA0MTkyLDQ1MjYgNDE5Miw0NDIxIDQyNzgsNDMzNSA0Mzg0LDQzMzUgTCA4OTIwLDQzMzUgQyA5MDI2LDQzMzUgOTExMiw0NDIxIDkxMTIsNDUyNiBMIDkxMTIsOTAyOCBDIDkxMTIsOTEzMyA5MDI2LDkyMTkgODkyMCw5MjE5IEwgODkyMCw5MjE5IFoiLz4KICAgICAgICA8cGF0aCBmaWxsPSJyZ2IoMjQ0LDE3OCwxNzYpIiBzdHJva2U9Im5vbmUiIGQ9Ik0gNTM0Niw4MDc0IEwgNzk1OCw4MDc0IDc5NTgsNTQ4MCA1MzQ2LDU0ODAgNTM0Niw4MDc0IDUzNDYsODA3NCBaIi8+CiAgICAgICAgPHBhdGggZmlsbD0icmdiKDE3OSw2NCw3NCkiIHN0cm9rZT0ibm9uZSIgZD0iTSA3OTU4LDgyNjUgTCA1MzQ2LDgyNjUgQyA1MjM5LDgyNjUgNTE1Myw4MTc5IDUxNTMsODA3NCBMIDUxNTMsNTQ4MCBDIDUxNTMsNTM3NSA1MjM5LDUyODkgNTM0Niw1Mjg5IEwgNzk1OCw1Mjg5IEMgODA2NSw1Mjg5IDgxNTEsNTM3NSA4MTUxLDU0ODAgTCA4MTUxLDgwNzQgQyA4MTUxLDgxNzkgODA2NSw4MjY1IDc5NTgsODI2NSBMIDc5NTgsODI2NSBaIE0gNTUzOCw3ODgzIEwgNzc2Niw3ODgzIDc3NjYsNTY3MSA1NTM4LDU2NzEgNTUzOCw3ODgzIDU1MzgsNzg4MyBaIi8+CiAgICAgICA8L2c+CiAgICAgIDwvZz4KICAgICA8L2c+CiAgICA8L2c+CiAgIDwvZz4KICA8L2c+CiA8L2c+Cjwvc3ZnPg==';
    const img = new Image();
    img.src = icon;
    return img;
  }
}
