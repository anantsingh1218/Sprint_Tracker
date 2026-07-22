import { trigger, transition, style, animate } from '@angular/animations';

export const fadeSlide = trigger('fadeSlide', [

  // ENTER (like Azure DevOps card appearing)
  transition(':enter', [
    style({
      opacity: 0,
      transform: 'translateY(10px) scale(0.98)'
    }),
    animate('180ms ease-out', style({
      opacity: 1,
      transform: 'translateY(0) scale(1)'
    }))
  ]),

  // LEAVE (like moving between columns / deleting)
  transition(':leave', [
    animate('150ms ease-in', style({
      opacity: 0,
      transform: 'translateY(10px) scale(0.98)'
    }))
  ])
]);