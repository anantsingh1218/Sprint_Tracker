import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IComment, IStory } from '../../models/storyInterface';



@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './story.html',
  styleUrl: './story.css'
})
export class Story {

  @Input() story!: IStory;
  @Output() save = new EventEmitter<IStory>();
  @Output() close = new EventEmitter<void>();

  newComment = '';

  users = [
    { id: 1, name: 'John Doe' },
    { id: 2, name: 'Sarah Lee' },
    { id: 3, name: 'Mike Johnson' }
  ];

  features = [
    { id: 1, name: 'Login Module' },
    { id: 2, name: 'Sprint Module' }
  ];

  sprints = [
    { id: 1, name: 'Sprint 1' },
    { id: 2, name: 'Sprint 2' }
  ];

  saveStory() {
    this.save.emit(this.story);
  }

  closeOverlay() {
    this.close.emit();
  }

  addComment() {
    if (!this.newComment.trim()) return;

    const comment: IComment = {
      userId: 1,
      text: this.newComment,
      createdAt: new Date().toISOString()
    };

    this.story.comments.push(comment);
    this.newComment = '';
  }

  getUserName(userId: number | null): string {
    return this.users.find(u => u.id === userId)?.name || 'Unassigned';
  }
}