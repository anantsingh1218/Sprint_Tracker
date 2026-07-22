import { Component, Input, Output, EventEmitter, OnInit, signal,ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/apiService/api-service';
import { Attachment } from '../../models/attachmentInterface';
import { WorkItemType } from '../../models/workItem';
import { HttpErrorResponse } from '@angular/common/http';
import { IBug } from '../../models/bugInterface';
import { IComment } from '../../models/storyInterface';

@Component({
  selector: 'app-bug',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bug.html',
  styleUrl: './bug.css'
})
export class Bug implements OnInit {

  @Input() bug: IBug | null = null;
  @Input() sprints: any[] = [];
  @Input() stories: any[] = [];
  @Input() users: any[] = [];
  @Output() save = new EventEmitter<any>();
  @Output() close = new EventEmitter<void>();

  isAttachmentView = false;
  selectedFiles = signal<File[]>([]);
  attachments = signal<Attachment[]>([]);
  attachmentUploadStatus = signal('');
  newComment = '';

  constructor(private apiService: ApiService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    if (this.bug && this.bug.id > 0) {
      if (!this.bug.comments) this.bug.comments = [];
      this.loadAttachments();
    }
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  loadAttachments() {
    if (!this.bug?.bugCode) return;
    try {
      this.apiService.getAttachments(WorkItemType.Bug, this.apiService.toApiWorkItemId(this.bug.bugCode)).subscribe({
        next: (data) => {
          if(data?.fileToBeFetched)
            this.attachments.set(data.fileToBeFetched);
        },
        error: (err: HttpErrorResponse) => {
          this.attachments.set([]);
          if (err.status === 500) {
            this.attachmentUploadStatus.set('Resource not found');
          }
        },
      });
    } catch(e) {
      console.warn("Invalid ID format for attachments");
    }
  }

  onFileSelected(event: any) {
    const files: FileList = event.target.files;
    if (!files) return;
    this.selectedFiles.set(Array.from(files));
  }

  uploadFiles() {
    if (!this.selectedFiles().length || !this.bug?.bugCode) return;

    try {
      this.apiService.uploadAttachments(WorkItemType.Bug, this.apiService.toApiWorkItemId(this.bug.bugCode), this.selectedFiles()).subscribe({
        next: (res: Attachment[]) => {
          this.selectedFiles.set([]);
          this.attachmentUploadStatus.set('Files uploaded Successfully');
          this.attachments.set(res);
        },
      });
    } catch(e) {}
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

  saveBug() {
    const bugToSave = { ...this.bug };

    // The backend BugDto expects a single String for the new comment being added.
    // If the user typed a new comment, we send it here so the backend saves it.
    if (this.newComment.trim()) {
      const newComment : IComment = {
        userCode: bugToSave.assignedUserCode ? bugToSave.assignedUserCode : '',
        text: this.newComment.trim(),
        createdAt: Date.now().toString()
      };
      bugToSave.comments?.push(newComment);
    } else {
      bugToSave.comments = bugToSave.comments;
    }

    this.save.emit(bugToSave);
  }

  closeOverlay() {
    this.close.emit();
  }
}
