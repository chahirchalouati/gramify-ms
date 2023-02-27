import {Directive, ElementRef, EventEmitter, NgZone, OnDestroy, OnInit, Output,} from '@angular/core';

@Directive({
  selector: '[trackVisibility]',
})
export class TrackVisibilityDirective implements OnInit, OnDestroy {
  observer!: IntersectionObserver;

  @Output() visible = new EventEmitter<boolean>();

  constructor(private el: ElementRef<HTMLElement>, private ngZone: NgZone) {
  }

  ngOnInit(): void {
    this.ngZone.runOutsideAngular(() => {
      this.observer = new IntersectionObserver((entries) => {
        entries.forEach((e) => {
          this.visible.emit(e.isIntersecting);
        });
      });
      this.observer.observe(this.el.nativeElement);
    });
  }

  ngOnDestroy(): void {
    this.observer.disconnect();
  }
}
