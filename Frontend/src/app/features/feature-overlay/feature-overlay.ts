import {
  Component,
  ChangeDetectorRef,
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
import { IFeature } from '../../models/featureInterface';
import { WorkItemType } from '../../models/workItem';

import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { GetAllEntity } from '../../models/getAllEntityInterface';

@Component({
  selector: 'app-feature',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feature-overlay.html',
  styleUrl: './feature-overlay.css',
})
export class FeatureOverlay implements OnInit, OnChanges {
  @Input() feature!: IFeature;
  @Output() save = new EventEmitter<IFeature>();
  @Output() close = new EventEmitter<void>();
  selectedFiles = signal<File[]>([]);
  attachments = signal<Attachment[]>([]);
  isAttachmentView = false;
  attachmentUploadStatus = signal('');
  newComment = '';
  users: GetAllEntity[] = [];
  products: GetAllEntity[] = [];
  sprints: GetAllEntity[] = [];

  constructor(
    private apiService: ApiService,
    private cdr: ChangeDetectorRef,
    @Optional()
    private dialogRef: MatDialogRef<FeatureOverlay>,
    @Optional()
    @Inject(MAT_DIALOG_DATA)
    private dialogData: { feature?: IFeature } | null,
  ) {}

  saveFeature() {
    if (this.dialogRef) {
      this.dialogRef.close(this.feature);
      return;
    }

    this.save.emit(this.feature);
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

    this.feature.comments.push(comment);
    this.newComment = '';
  }

  getUserName(userId: any): string {
    return this.users.find((u) => u.id === userId || u.name === userId)?.name || 'Unassigned';
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  loadAttachments() {
    this.apiService
      .getAttachments(WorkItemType.Feature, this.apiService.toApiWorkItemId(this.feature.id))
      .subscribe({
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
    if (this.dialogData?.feature) {
      this.feature = this.dialogData.feature;
    }

    this.feature.comments ??= [];
    this.loadDropdownData();
    if (this.feature.id) {
      this.loadAttachments();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['feature'] && this.feature?.id) {
      this.feature.comments ??= [];
      this.loadAttachments();
    }
  }

  loadDropdownData() {
    this.apiService.getRequest<GetAllEntity[]>('/user/getAllUsers').subscribe({
      next: (data) => {
        this.users = data;
        this.cdr.detectChanges(); // Force UI Update
      },
      error: (err) => console.error('Failed to load users', err),
    });

    this.apiService.getRequest<GetAllEntity[]>('/product/getAllProducts').subscribe({
      next: (data) => {
        this.products = data;
        this.cdr.detectChanges(); // Force UI Update
      },
      error: (err) => console.error('Failed to load products', err),
    });

    this.apiService.getRequest<GetAllEntity[]>('/sprint/getAllSprints').subscribe({
      next: (data) => {
        this.sprints = data;
        this.cdr.detectChanges(); // Force UI Update
      },
      error: (err) => console.error('Failed to load sprints', err),
    });
  }

  onFileSelected(event: any) {
    const files: FileList = event.target.files;

    if (!files) return;

    this.selectedFiles.set(Array.from(files));
  }

  uploadFiles() {
    if (!this.selectedFiles().length) return;

    this.apiService
      .uploadAttachments(
        WorkItemType.Feature,
        this.apiService.toApiWorkItemId(this.feature.id),
        this.selectedFiles(),
      )
      .subscribe({
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
