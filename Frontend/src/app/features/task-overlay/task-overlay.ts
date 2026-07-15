import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/apiService/api-service';
import { Attachment } from '../../models/attachmentInterface';
import { Priority, WorkItemType, WorkStatus } from '../../models/workItem';
import { HttpErrorResponse } from '@angular/common/http';
import { TaskService } from '../tasks/task.service';
import { ITask } from '../../models/taskInterface';
import { IComment } from '../../models/storyInterface';
import { FetchAttachmentsResponse } from '../../models/fetchAttachmnetResponseInterface';

@Component({
  selector: 'app-task-overlay',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-overlay.html',
  styleUrl: './task-overlay.css',
})
export class TaskOverlay implements OnInit {
  @Input() task: ITask | null = null;
  @Input() sprints: any[] = [];
  @Input() users: any[] = [];
  @Input() stories: any[] = [];

  @Output() save = new EventEmitter<any>();
  @Output() close = new EventEmitter<void>();

  isAttachmentView = false;
  selectedFiles = signal<File[]>([]);
  attachments = signal<Attachment[]>([]);
  attachmentUploadStatus = signal('');
  newComment = '';

  constructor(
    private apiService: ApiService,
    private taskService: TaskService,
  ) {}

  ngOnInit() {
    if (!this.task) {
      this.task = {
        id: null as unknown as string,
        title: '',
        description: '',
        status: WorkStatus.OPEN,
        priority: Priority.LOW,
        estimatedHours: 0,
        remainingHours: 0,
        storyCode: '',
        sprintCode: '',
        userCode: '',
        comments: [],
      };
    } else {
      if (!this.task.comments) this.task.comments = [];
      if (this.task.id) {
        this.loadAttachments();
      }
    }
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  loadAttachments() {
    if (!this.task?.id) return;
    try {
      this.apiService
        .getAttachments(WorkItemType.Task, this.apiService.toApiWorkItemId(this.task.id))
        .subscribe({
          next: (data: FetchAttachmentsResponse) => {
            if (data?.fileToBeFetched) {
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
    } catch (e) {
      console.warn('Invalid ID format for attachments');
    }
  }

  onFileSelected(event: any) {
    const files: FileList = event.target.files;
    if (!files) return;
    this.selectedFiles.set(Array.from(files));
  }

  uploadFiles() {
    if (!this.selectedFiles().length || !this.task?.id) return;

    try {
      this.apiService
        .uploadAttachments(
          WorkItemType.Task,
          this.apiService.toApiWorkItemId(this.task?.id),
          this.selectedFiles(),
        )
        .subscribe({
          next: (res: Attachment[]) => {
            this.selectedFiles.set([]);
            this.attachmentUploadStatus.set('Files uploaded Successfully');
            this.attachments.set(res);
          },
        });
    } catch (e) {}
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

  saveTask() {
    if (!this.task) return;

    if (this.newComment?.trim()) {
    this.task.comments = this.task.comments || [];
    this.task.comments.push({ userCode:this.task.userCode != null ? this.task.userCode : '' , text: this.newComment.trim(), createdAt: Date.now().toString() });
  }

    let { comments, ...destructedPayload } = this.task;
    const payloadToSend = { ...destructedPayload, comments: comments?.at(-1)?.text };
    console.log('To Save Payload = ' + JSON.stringify(payloadToSend));
    this.save.emit(payloadToSend);
    this.newComment = '';
  }

  closeOverlay() {
    this.close.emit();
  }
}
