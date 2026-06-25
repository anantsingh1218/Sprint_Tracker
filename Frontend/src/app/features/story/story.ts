import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  signal,
  SimpleChanges,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IComment, IStory } from '../../models/storyInterface';
import { ApiService } from '../../core/apiService/api-service';
import { Attachment } from '../../models/attachmentInterface';

@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './story.html',
  styleUrl: './story.css',
})
export class Story implements OnChanges {
  @Input() story!: IStory;
  @Output() save = new EventEmitter<IStory>();
  @Output() close = new EventEmitter<void>();
  selectedFiles: File[] = [];
  attachments: Attachment[] = [];
  isAttachmentView = false;
  attachmentUploadStatus = signal('');

  constructor(private apiService: ApiService) {}

  newComment = '';

  users = [
    { id: 1, name: 'John Doe' },
    { id: 2, name: 'Sarah Lee' },
    { id: 3, name: 'Mike Johnson' },
  ];

  features = [
    { id: 1, name: 'Login Module' },
    { id: 2, name: 'Sprint Module' },
  ];

  sprints = [
    { id: 1, name: 'Sprint 1' },
    { id: 2, name: 'Sprint 2' },
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
      createdAt: new Date().toISOString(),
    };

    this.story.comments.push(comment);
    this.newComment = '';
  }

  getUserName(userId: number | null): string {
    return this.users.find((u) => u.id === userId)?.name || 'Unassigned';
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  loadAttachments() {
    this.apiService.getAttachments('story', this.story.id).subscribe({
      next: (data) => {
        this.attachments = data.fileToBeFetched;
      },
      error: () => {
        this.attachments = [];
      },
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['story'] && this.story?.id) {
      this.loadAttachments();
    }
  }

  onFileSelected(event: any) {
    const files: FileList = event.target.files;

    if (!files) return;

    this.selectedFiles = Array.from(files);
  }

  uploadFiles() {
    if (!this.selectedFiles.length) return;

    this.apiService.uploadAttachments('story', this.story.id, this.selectedFiles).subscribe({
      next: (res: Attachment[]) => {
        console.log('attachments:', this.attachments);
        console.log('isArray:', Array.isArray(this.attachments));
        this.attachments = res;
        this.selectedFiles = [];
        this.attachmentUploadStatus.set(res + 'Files uploaded Successfully');
      },
    });
  }

  deleteAttachment(filename: string) {
    this.apiService.deleteRequest(`/attachments/delete/story/${filename}`).subscribe(() => {
      this.attachments = this.attachments.filter((a) => a.filename !== filename);
    });
  }
}
