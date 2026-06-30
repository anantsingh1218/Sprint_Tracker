import {
  Component,
  EventEmitter,
  Inject,
  Input,
  OnChanges,
  OnInit,
  Optional,
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
import { WorkItemType } from '../../models/workItem';

import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { IBug } from '../../models/bugInterface';

@Component({
  selector: 'app-bug',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bug.html',
  styleUrl: './bug.css',
})
export class Bug implements OnInit, OnChanges {
  @Input() bug!: IBug;
  @Output() save = new EventEmitter<IBug>();
  @Output() close = new EventEmitter<void>();
  selectedFiles = signal<File[]>([]);
  attachments = signal<Attachment[]>([]);
  isAttachmentView = false;
  attachmentUploadStatus = signal('');

  constructor(
    private apiService: ApiService,
    @Optional()
    private dialogRef: MatDialogRef<Bug>,
    @Optional()
    @Inject(MAT_DIALOG_DATA)
    private dialogData: { bug?: IBug } | null,
  ) {}

  newComment = '';

  users = [
    { id: 1, name: 'John Doe' },
    { id: 2, name: 'Sarah Lee' },
    { id: 3, name: 'Mike Johnson' },
  ];

  stories = [
    { id: 1, name: 'Login Module' },
    { id: 2, name: 'Sprint Module' },
  ];

  sprints = [
    { id: 1, name: 'Sprint 1' },
    { id: 2, name: 'Sprint 2' },
  ];

  saveBug() {
    if (this.dialogRef) {
      this.dialogRef.close(this.bug);
      return;
    }

    this.save.emit(this.bug);
  }

  closeOverlay() {
    if (this.dialogRef) {
      this.dialogRef.close();
      return;
    }

    this.close.emit();
  }

  addComment() {
    if (!this.newComment.trim()) return;

    const comment: IComment = {
      userId: 1,
      text: this.newComment,
      createdAt: new Date().toISOString(),
    };
    this.bug.comments ??= [];
    this.bug.comments.push(comment);
    this.newComment = '';
  }

  getUserName(userId: number | null): string {
    return this.users.find((u) => u.id === userId)?.name || 'Unassigned';
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  loadAttachments() {
    this.apiService.getAttachments(WorkItemType.Bug, this.apiService.toApiWorkItemId(this.bug.id)).subscribe({
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

  ngOnInit(): void {
    // Checks updated dialogData type safely
    if (this.dialogData?.bug) {
      this.bug = this.dialogData.bug;
    }

    // Safe Check: Prevents runtime crashing if 'bug' is missing entirely upon modal opening
    if (this.bug) {
      this.bug.comments ??= [];
      if (this.bug.id) {
        this.loadAttachments();
      }
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['bug'] && this.bug?.id) {
      this.bug.comments ??= [];
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

    this.apiService.uploadAttachments(WorkItemType.Bug, this.apiService.toApiWorkItemId(this.bug.id), this.selectedFiles()).subscribe({
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