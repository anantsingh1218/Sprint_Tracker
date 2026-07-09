import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/apiService/api-service';
import { Attachment } from '../../models/attachmentInterface';
import { WorkItemType } from '../../models/workItem';
import { HttpErrorResponse } from '@angular/common/http';
import { TaskService } from '../tasks/task.service';

@Component({
  selector: 'app-task-overlay',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-overlay.html',
  styleUrl: './task-overlay.css'
})
export class TaskOverlay implements OnInit {
  @Input() task: any = null;
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

  constructor(private apiService: ApiService, private taskService: TaskService) {}

  ngOnInit() {
    if (!this.task) {
      this.task = {
        title: '',
        body: '',
        taskstatus: 'OPEN',
        priority: 'MEDIUM',
        originalestimatehours: 0,
        remainingestimatehours: 0,
        storyCode: '',
        sprintCode: '',
        userCode: '',
        commentsList: []
      };
    } else {
      if (!this.task.commentsList) this.task.commentsList = [];
      if (this.task.taskCode) {
        this.loadAttachments();
      }
    }
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  loadAttachments() {
    if (!this.task.taskCode) return;
    try {
      this.apiService.getAttachments(WorkItemType.Task, this.apiService.toApiWorkItemId(this.task.taskCode)).subscribe({
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
    if (!this.selectedFiles().length || !this.task.taskCode) return;

    try {
      this.apiService.uploadAttachments(WorkItemType.Task, this.apiService.toApiWorkItemId(this.task.taskCode), this.selectedFiles()).subscribe({
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

  addComment() {
    if (!this.newComment.trim()) return;

    this.task.commentsList.push({
      userCode: '',
      text: this.newComment,
      createdAt: new Date().toISOString()
    });
    this.newComment = '';
  }

  saveTask() {
    if (this.task.taskCode) {
      this.taskService.updateTask(this.task).subscribe({
        next: (res) => this.save.emit(res),
        error: (err) => console.error('Failed to update task', err)
      });
    } else {
      this.taskService.addTask(this.task).subscribe({
        next: (res) => this.save.emit(res),
        error: (err) => console.error('Failed to create task', err)
      });
    }
  }

  closeOverlay() {
    this.close.emit();
  }
}
