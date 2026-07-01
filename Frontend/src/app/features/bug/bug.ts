import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-bug',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bug.html',
  styleUrl: './bug.css'
})
export class Bug {

  @Input() bug: any;
  @Output() save = new EventEmitter<any>();
  @Output() close = new EventEmitter<void>();

  saveBug() {
    this.save.emit(this.bug);
  }

  closeOverlay() {
    this.close.emit();
  }
}
