import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class DecoratorTypesService {
  static wholeInitialization(arg0: {}) {
  }

  static wholeBlockFile(arg0: any): (target: any) => void  {
    return (target: any) => {
    }
  }

  static wholeBlockMethod(arg0: any): (target: any, propertyKey: string | symbol | undefined, descriptor: any) => void {
    return (target: any, propertyKey: string | symbol | undefined, descriptor: any) => {
    }
  }

  static skipLineParameter(arg0: {}, commentedLine: string | null = null): (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => void {
    return (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => {
    }
  }

  static skipLineVariableDeclaration(arg0: {}, commentedLine: string | null = null): (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => void {
    return (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => {
    }
  }

  static skipLineClassVariableDeclaration(arg0: {}, commentedLine: string | null = null): (target: any, key: string)=> void {
    return (target: any, key: string) => {
    }
  }

  static skipLineFile(arg0: {}, commentedLine: string | null = null): (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => void {
    return (target: any, propertyKey: string | symbol | undefined, parameterIndex: number) => {
    }
  }

  constructor() { }
}
