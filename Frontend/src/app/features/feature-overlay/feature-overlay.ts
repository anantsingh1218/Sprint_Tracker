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
import { FeatureService } from '../feature-list/feature.service';

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
  users: any[] = [];
  products: GetAllEntity[] = [];
  sprints: GetAllEntity[] = [];

  constructor(
    private apiService: ApiService,
    private featureService: FeatureService,
    private cdr: ChangeDetectorRef,
    @Optional()
    private dialogRef: MatDialogRef<FeatureOverlay>,
    @Optional()
    @Inject(MAT_DIALOG_DATA)
    private dialogData: { feature?: IFeature } | null,
  ) {}

  saveFeature() {
    const payload: any = {
      featureTitle: this.feature.title,
      description: this.feature.description,
      productCategory: this.feature.productName,
      sprintName: this.feature.sprintName,
      assignedTo: this.feature.assignedTo,
      featureStatus: this.feature.featureStatus,
      featurePriority: this.feature.priority,
      estimatedStoryPoints: this.feature.estimatedStoryPoints,
      remainingStoryPoints: this.feature.remainingStoryPoints,
      comments: this.newComment.trim() || undefined
    };

    if (this.feature.featureCode) {
      this.featureService.updateFeature({ ...payload, featureCode: this.feature.featureCode }).subscribe({
        next: (res) => {
          if (this.dialogRef) {
            this.dialogRef.close(res);
          } else {
            this.save.emit(res);
          }
        },
        error: (err) => console.error('Failed to update feature', err)
      });
    } else {
      this.featureService.addFeature(payload).subscribe({
        next: (res) => {
          if (this.dialogRef) {
            this.dialogRef.close(res);
          } else {
            this.save.emit(res);
          }
        },
        error: (err) => console.error('Failed to create feature', err)
      });
    }
  }

  closeOverlay() {
    if (this.dialogRef) {
      this.dialogRef.close();
      return;
    }
    this.close.emit();
  }

  getUserName(userCode: any): string {
    return this.users.find((u) => u.id === userCode || u.username === userCode)?.username || 'Unassigned';
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  loadAttachments() {
    this.apiService
      .getAttachments(WorkItemType.Feature, this.apiService.toApiWorkItemId(this.feature.id || this.feature.featureCode || ''))
      .subscribe({
        next: (data) => {
          if(data && data.fileToBeFetched){
            this.attachments.set(data.fileToBeFetched);
          }
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

    this.feature.commentsList ??= [];
    this.loadDropdownData();
    if (this.feature.id || this.feature.featureCode) {
      this.loadAttachments();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['feature'] && (this.feature?.id || this.feature?.featureCode)) {
      this.feature.commentsList ??= [];
      this.loadAttachments();
    }
  }

  loadDropdownData() {
    this.apiService.getRequest<any[]>('/users/all').subscribe({
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
        this.apiService.toApiWorkItemId(this.feature.id || this.feature.featureCode || ''),
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
