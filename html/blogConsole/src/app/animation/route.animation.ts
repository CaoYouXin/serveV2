import {animate, AnimationEntryMetadata, state, style, transition, trigger} from '@angular/core';

// Component transition animations
export const slideInUpAnimation: AnimationEntryMetadata =
  trigger('routeAnimation', [
    state('*',
      style({
        opacity: 1,
        transform: 'translateX(0)'
      })
    ),
    transition(':enter', [
      style({
        opacity: 0,
        zIndex: 1,
        transform: 'translateX(100%)'
      }),
      animate('0.5s ease-in')
    ]),
    transition(':leave', [
      animate('0.5s ease-out', style({
        opacity: 0,
        zIndex: -1,
        transform: 'translateY(-100%)'
      }))
    ])
  ]);
