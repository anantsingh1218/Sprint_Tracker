// import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import {
//   DragDropModule,
//   CdkDragDrop,
//   transferArrayItem
// } from '@angular/cdk/drag-drop';
// import { MatCardModule } from '@angular/material/card';

// import { DashboardService } from '../../services/dashboardService';
// import { BoardColumn } from './models/board.model';

// @Component({
//   selector: 'app-board',
//   standalone: true,
//   imports: [
//     CommonModule,
//     DragDropModule,
//     MatCardModule
//   ],
//   templateUrl: './board.html',
//   styleUrls: ['./board.css']
// })
// export class Board implements OnInit {

//   columns: BoardColumn[] = [];
  

//   sprintId = 1;

//   constructor(
//     private dashboardService: DashboardService,
//     private cdr: ChangeDetectorRef
//   ) {}

//   ngOnInit(): void {

//     this.loadBoard();

//   }

//   loadBoard(): void {

//     this.dashboardService
//       .getBoard(this.sprintId)
//       .subscribe({

//         next: (res: BoardColumn[]) => {

//           console.log("Board Response:", res);

//           this.columns = res;

//         },

//         error: (err) => {

//           console.error(err);

//         }

//       });
//       // this.cdr.detectChanges();

//   }

//   drop(
//     event: CdkDragDrop<any[]>,
//     column: BoardColumn
// ): void {

//     if (
//         event.previousContainer === event.container
//     ) {
//         return;
//     }

//     const movedTask =
//         event.previousContainer.data[
//             event.previousIndex
//         ];

//     this.dashboardService
//         .moveTask({

//             taskCode: movedTask.taskCode,

//             status: column.status

//         })

//         .subscribe({

//             next: () => {

//                 this.loadBoard();

//             },

//             error: err => {

//                 console.error(err);

//             }

//         });

// }

// get dropLists(): string[] {

//     return this.columns.map(

//         c => c.status

//     );

// }

// }
import { SprintDropdown } from './models/sprint-dropdown.model';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  DragDropModule,
  CdkDragDrop
} from '@angular/cdk/drag-drop';
import { MatCardModule } from '@angular/material/card';
import { BehaviorSubject, Observable } from 'rxjs';

import { DashboardService } from '../../services/dashboardService';
import { BoardColumn } from './models/board.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-board',
  standalone: true,
  imports: [
    CommonModule,
    DragDropModule,
    MatCardModule, FormsModule
  ],
  templateUrl: './board.html',
  styleUrls: ['./board.css']
})
export class Board implements OnInit {

  private columnsSubject = new BehaviorSubject<BoardColumn[]>([]);

  columns$: Observable<BoardColumn[]> =
    this.columnsSubject.asObservable();

  sprints: SprintDropdown[] = [];

  selectedSprintId = 1;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.loadBoard();

    this.loadSprints();

  }

  loadBoard(): void {

    this.dashboardService
      .getBoard(this.selectedSprintId)
      .subscribe({

        next: (res: BoardColumn[]) => {

          console.log('Board Response:', res);

          this.columnsSubject.next(res);

          this.cdr.detectChanges();

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  drop(
    event: CdkDragDrop<any[]>,
    column: BoardColumn
  ): void {

    if (event.previousContainer === event.container) {
      return;
    }

    const movedTask =
      event.previousContainer.data[event.previousIndex];

    console.log('Moving Task:', movedTask);

    this.dashboardService
      .moveTask({

        taskCode: movedTask.taskCode,

        status: column.status

      })

      .subscribe({

        next: () => {

          this.loadBoard();

        },

        error: err => {

          console.error(err);

        }

      });

  }

  get dropLists(): string[] {

    return this.columnsSubject
      .getValue()
      .map(column => column.status);

  }

  loadSprints(): void {

    this.dashboardService
        .getSprints()
        .subscribe({

            next: res => {

                this.sprints = res;

                if(res.length){

                    this.selectedSprintId = res[0].id;

                    this.loadBoard();

                }

            }

        });

}

}