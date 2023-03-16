import {ComponentFactoryResolver, Directive, HostListener, Input, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[appDynamicComponent]'
})
export class DynamicComponentDirective {

  constructor(private viewContainerRef: ViewContainerRef, private factory: ComponentFactoryResolver) {
  }

  @HostListener('document:click')
  public onClick() {
  }
}

