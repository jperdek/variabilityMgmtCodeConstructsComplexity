import { Injectable, Type } from '@angular/core';
import 'reflect-metadata';


declare let Reflect: any;

@Injectable({
  providedIn: 'root'
})
export class AnnotationExtractorService {

  //private Reflect: any;
  constructor() { }

  getAnnotation(typeOrFunc: Type<any>): any|null {
    // Prefer the direct API.
    if ((<any>typeOrFunc).annotations) {
      let annotations = (<any>typeOrFunc).annotations;
      if (typeof annotations === 'function' && annotations.annotations) {
        annotations = annotations.annotations;
      }
      return annotations[0];
    }

    // API of tsickle for lowering decorators to properties on the class.
    if ((<any>typeOrFunc).decorators) {
      return this.convertTsickleDecoratorIntoMetadata((<any>typeOrFunc).decorators)[0];
    }

    // API for metadata created by invoking the decorators.
    if (Reflect && Reflect.getOwnMetadata) {
      return Reflect.getOwnMetadata('annotations', typeOrFunc)[0];
    }
    return null;
  }

  convertTsickleDecoratorIntoMetadata(decoratorInvocations: any[]): any[] {
    if (!decoratorInvocations) {
      return [];
    }
    return decoratorInvocations.map(decoratorInvocation => {
      const decoratorType = decoratorInvocation.type;
      const annotationCls = decoratorType.annotationCls;
      const annotationArgs = decoratorInvocation.args ? decoratorInvocation.args : [];
      return new annotationCls(...annotationArgs);
    });
  }


  getAnnotation2(typeOrFunc: Type<any>): any[]|null {
    // Prefer the direct API.
    console.log(Object.getOwnPropertyNames(typeOrFunc));
    console.log((typeOrFunc as any)["ɵcmp"]);
    console.log((typeOrFunc as any)["ɵfac"]);
    if ((<any>typeOrFunc)['__annotations__']) {
        return (<any>typeOrFunc)['__annotations__'][0];
    }

    // API of tsickle for lowering decorators to properties on the class.
    if ((<any>typeOrFunc).decorators) {
      return this.convertTsickleDecoratorIntoMetadata((<any>typeOrFunc).decorators)[0];
    }

    // API for metadata created by invoking the decorators.
    if (Reflect && Reflect.getOwnMetadata) {
      return Reflect.getOwnMetadata('annotations', typeOrFunc)[0];
    }
    return null;
  }

}
