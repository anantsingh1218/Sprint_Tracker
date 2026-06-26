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
import { IComment } from '../../models/storyInterface';
import { ApiService } from '../../core/apiService/api-service';
import { Attachment } from '../../models/attachmentInterface';
import { HttpErrorResponse } from '@angular/common/http';
import { IFeature } from '../../models/featureInterface';

@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feature-overlay.html',
  styleUrl: './feature-overlay.css',
})
export class FeatureOverlay implements OnChanges {
  @Input() feature!: IFeature;
  @Output() save = new EventEmitter<IFeature>();
  @Output() close = new EventEmitter<void>();
  selectedFiles = signal<File[]>([]);
  attachments = signal<Attachment[]>([]);
  isAttachmentView = false;
  attachmentUploadStatus = signal('');

  constructor(private apiService: ApiService) {}

  newComment = '';

  users = [
    { id: 1, name: 'John Doe' },
    { id: 2, name: 'Sarah Lee' },
    { id: 3, name: 'Mike Johnson' },
  ];

  products = [
    { id: 1, name: 'FSM' },
    { id: 2, name: 'Starwatch' },
  ];

  sprints = [
    { id: 1, name: 'Sprint 1' },
    { id: 2, name: 'Sprint 2' },
  ];

  saveFeature() {
    this.save.emit(this.feature);
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

    this.feature.comments.push(comment);
    this.newComment = '';
  }

  getUserName(userId: number | null): string {
    return this.users.find((u) => u.id === userId)?.name || 'Unassigned';
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  loadAttachments() {
    this.apiService.getAttachments('feature', this.feature.id).subscribe({
      next: (data) => {
        this.attachments.set(data.fileToBeFetched);
      },
      error: (err: HttpErrorResponse) => {
        this.attachments.set([]);
        if (err.status === 500) {
          this.attachmentUploadStatus.set('Resource not found');
        }
      },
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['story'] && this.feature?.id) {
      this.loadAttachments();
    }
  }

  onFileSelected(event: any) {
    const files: FileList = event.target.files;

    if (!files) return;

    this.selectedFiles.set(Array.from(files));
  }

  uploadFiles() {
    if (!this.selectedFiles().length) return;

    this.apiService.uploadAttachments('feature', this.feature.id, this.selectedFiles()).subscribe({
      next: (res: Attachment[]) => {
        this.selectedFiles.set([]);
        this.attachmentUploadStatus.set('Files uploaded Successfully');
        this.attachments.set(res);
      },
    });
  }

  deleteAttachment(filename: string) {
    this.apiService
      .deleteRequest('/attachment/delete', {
        body: { filename: filename },
      })
      .subscribe(() => {
        this.attachments.update((list) => list.filter((a) => a.filename !== filename));
      });
  }
}
