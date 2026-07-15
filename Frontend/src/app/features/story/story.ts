import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  signal,
  ChangeDetectorRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiService } from '../../core/apiService/api-service';
import { Attachment } from '../../models/attachmentInterface';
import { WorkItemType } from '../../models/workItem';
import { StoryService } from '../story-list/story.service';
import { IStoryResponse } from '../../models/storyResponseInterface';
import { IStory } from '../../models/storyInterface';

@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './story.html',
  styleUrl: './story.css',
})
export class Story implements OnInit {
  @Input() isOpen = false;
  @Input() selectedStory: IStory | null = null;
  @Input() usersList: any[] = [];
  @Input() featuresList: any[] = [];
  @Input() sprintsList: any[] = [];

  @Output() close = new EventEmitter<void>();
  @Output() saved = new EventEmitter<IStory>();

  newComment = '';
  isAttachmentView = false;

  // Attachments Signals
  selectedFiles = signal<File[]>([]);
  attachments = signal<Attachment[]>([]);
  attachmentUploadStatus = signal('');

  constructor(
    private St1: StoryService,
    private apiService: ApiService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    if (this.selectedStory?.id) {
      this.loadAttachments();
    }
  }

  closeModal() {
    this.close.emit();
  }

  saveStory() {
    if (!this.selectedStory) {
      console.error('Cannot save: selectedStory is null');
      return;
    }
    this.saved.emit(this.selectedStory);
  }

  addComment() {
    if (!this.newComment.trim() || !this.selectedStory?.id) return;

    this.St1.updateStory(Number(this.selectedStory.id.substring(1)), {
      comments: this.newComment,
    }).subscribe({
      next: (response: any) => {
        if (!this.selectedStory!.comments) {
          this.selectedStory!.comments = [];
        }

        // 2. Create the comment object to match what your template expects
        // If your backend returns the newly created comment object, use that instead!
        const newCmt = {
          text: this.newComment,
          userCode: 'U1',
          createdAt: new Date().toISOString(), // Used for your track expression
        };

        // 3. Update the array immutably so Angular change detection fires flawlessly
        this.selectedStory!.comments = [...this.selectedStory!.comments, newCmt];

        // 4. Clear the input and force change detection just in case
        this.newComment = '';
        this.cdr.markForCheck();
      },
      error: (err: any) => console.error('Failed to add comment', err),
    });
  }

  toggleAttachmentsView() {
    this.isAttachmentView = !this.isAttachmentView;
  }

  getUserName(userCode: string | null): string {
    if (!userCode) return 'Unassigned';

    return (
      this.usersList.find((u) => {
        // Check if the list element has a raw userCode property matching "U2"
        if (u.userCode === userCode) return true;

        // Check if it matches your calculated UI prefix format: e.g. "U" + 2 === "U2"
        if (u.id && 'U' + u.id === userCode) return true;

        return false;
      })?.username || 'Unassigned'
    ); // Changed .name to .username based on your select loop
  }

  loadAttachments() {
    this.apiService
      .getAttachments(WorkItemType.Story, Number(this.selectedStory?.id.substring(1)))
      .subscribe({
        next: (data) => {
          if (data?.fileToBeFetched) this.attachments.set(data.fileToBeFetched);
        },
        error: (err: HttpErrorResponse) => {
          this.attachments.set([]);
          if (err.status === 500) this.attachmentUploadStatus.set('Resource not found');
        },
      });
  }

  onFileSelected(event: any) {
    const files: FileList = event.target.files;
    if (files) this.selectedFiles.set(Array.from(files));
  }

  uploadFiles() {
    if (!this.selectedFiles().length || !this.selectedStory?.id) return;
    this.apiService
      .uploadAttachments(
        WorkItemType.Story,
        this.apiService.toApiWorkItemId(this.selectedStory.id),
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
    this.apiService.deleteRequest('/attachment/delete', { body: { filename } }).subscribe(() => {
      this.attachments.update((list) => list.filter((a) => a.filename !== filename));
    });
  }
}
