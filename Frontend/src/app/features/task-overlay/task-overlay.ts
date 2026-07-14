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
  styleUrl: './task-overlay.css'
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

  constructor(private apiService: ApiService, private taskService: TaskService) {}

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
      this.apiService.getAttachments(WorkItemType.Task, this.apiService.toApiWorkItemId(this.task.id)).subscribe({
        next: (data : FetchAttachmentsResponse) => {
          if(data?.fileToBeFetched){
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
    if (!this.selectedFiles().length || !this.task?.id) return;

    try {
      this.apiService.uploadAttachments(WorkItemType.Task, this.apiService.toApiWorkItemId(this.task?.id), this.selectedFiles()).subscribe({
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



  saveTask() {
    const taskToSave = { ...this.task };
    if (this.newComment.trim()) {
      const newComment : IComment = {
        userCode: taskToSave.userCode ? taskToSave.userCode : '',
        text: this.newComment.trim(),
        createdAt: Date.now().toString()
      }
      taskToSave.comments?.push(newComment);
    } else {
      taskToSave.comments = taskToSave.comments;
    }

    let {comments, ...destructuredTask} = taskToSave;
    const payload = {...taskToSave, comments: comments?.at(-1)?.text}
    console.log("SENDING TASK TO BACKEND:", JSON.stringify(payload));
    if (this.task?.id) {
      this.taskService.updateTask(payload).subscribe({
        next: (res) => this.save.emit(res),
        error: (err) => console.error('Failed to update task', err)
      });
    } else {
      this.taskService.addTask(payload).subscribe({
        next: (res) => this.save.emit(res),
        error: (err) => console.error('Failed to create task', err)
      });
    }
  }

  closeOverlay() {
    this.close.emit();
  }
}
