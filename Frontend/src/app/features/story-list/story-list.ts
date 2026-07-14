import { SearchService } from './../search/search.service';
import { Component, OnInit, signal, ChangeDetectorRef, OnChanges, SimpleChange } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IStory } from '../../models/storyInterface';
import { Priority, WorkStatus } from '../../models/workItem';
import { HttpErrorResponse } from '@angular/common/http';
import { StoryService } from './story.service';
import { ApiService } from '../../core/apiService/api-service';
import { Attachment } from '../../models/attachmentInterface';
import { WorkItemType } from '../../models/workItem';
import { IStoryResponse } from '../../models/storyResponseInterface';

@Component({
  selector: 'app-story-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './story-list.html',
  styleUrl: './story-list.css',
})
export class StoryList implements OnInit {
  stories: IStory[] = [];
  isStoryOpen = false;

  selectedStory: IStory | null = null;
  newComment = '';

  // Attachments Signals
  selectedFiles = signal<File[]>([]);
  attachments = signal<Attachment[]>([]);
  isAttachmentView = false;
  attachmentUploadStatus = signal('');

  // Dependency data signals
  users = signal<any[]>([]);
  features = signal<any[]>([]);
  sprints = signal<any[]>([]);

  constructor(
    private St1: StoryService,
    private apiService: ApiService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadStories();
    this.loadDepedencyData();
  }

  loadStories() {
    this.St1.getStories().subscribe({
      next: (data: IStoryResponse[]) => {
        this.stories = [];
        data.map((ele) => {
          console.log("Original = " + JSON.stringify(ele));
          const story = this.mapResponseToInterface(ele);
          this.stories.push(story);
        });
        console.log(this.stories);
        this.cdr.markForCheck();
      },
      error: (err: any) => console.error('Failed to retrieve stories:', err),
    });
  }

  loadDepedencyData() {
    this.St1.getUsersDropdown().subscribe({
      next: (res) => this.users.set(res),
      error: (err) => console.error('Failed fetching users:', err),
    });

    this.St1.getFeaturesDropdown().subscribe({
      next: (res) => this.features.set(res),
      error: (err) => console.error('Failed fetching features:', err),
    });

    this.St1.getSprintsDropdown().subscribe({
      next: (res) => this.sprints.set(res),
      error: (err) => console.error('Failed fetching sprints:', err),
    });
  }

  openStory(story: any) {
    // Creating a copy of the selected item to prevent dirty direct editing side effects
    this.selectedStory = { ...story };
    if (this.selectedStory) {
      this.selectedStory.comments = this.selectedStory.comments || [];
    }
    this.isStoryOpen = true;

    if (this.selectedStory?.id) {
      this.loadAttachments();
    }
  }

  openCreateStory() {
    this.selectedStory = {
      id: null as unknown as string,
      title: '',
      body: '',
      featureCode: null,
      sprintCode: null,
      userCode: null,
      status: WorkStatus.OPEN,
      priority: Priority.LOW,
      estimatedStoryPoints: 0,
      remainingStoryPoint: 0,
      comments: [],
    };
    this.attachments.set([]);
    this.isStoryOpen = true;
  }

  closeStory() {
    this.isStoryOpen = false;
    this.selectedStory = null;
    this.newComment = '';
  }

  mapResponseToInterface(storyRes: IStoryResponse): IStory {
    return {
      id: 'S' + storyRes.id,
      title: storyRes.title,
      body: storyRes.body,
      status: storyRes.storyStatus,
      priority: storyRes.priority,
      estimatedStoryPoints: storyRes.storyPoints,
      remainingStoryPoint: storyRes.storyPoints,
      featureCode: storyRes.featureCode,
      sprintCode: storyRes.sprintCode,
      userCode: storyRes.userCode,
      comments: storyRes.comments || [],
    };
  }

  saveStory() {
    // 1. Guard check: Stop execution if selectedStory is null
    if (!this.selectedStory) return;

    const {comments , ...destructedPayload} = this.selectedStory;
    const payload = {...destructedPayload, comments: comments.at(-1)?.text};

    console.log(payload);

    if (this.selectedStory.id) {
      this.St1.updateStory(Number(this.selectedStory.id.substring(1)), payload).subscribe({
        next: () => {
          this.loadStories();
          this.closeStory();
        },
        error: (err: any) => console.error('Update failed', err),
      });
    } else {
      this.St1.createStory(payload).subscribe({
        next: () => {
          this.loadStories();
          this.closeStory();
        },
        error: (err: any) => console.error('Creation failed', err),
      });
    }
  }

  addComment() {
    // 2. Guard check: Ensure we have both a comment and an active valid story ID
    if (!this.newComment.trim() || !this.selectedStory || !this.selectedStory.id) return;

    const storyToUpdate = this.selectedStory; // local reference helper to satisfy compiler
    
    this.St1.updateStory(Number(storyToUpdate.id.substring(1)), { comments: this.newComment }).subscribe({
      next: () => {
        storyToUpdate.comments = storyToUpdate.comments || [];
        storyToUpdate.comments.push({
          userCode: storyToUpdate.userCode ? storyToUpdate.userCode : (null as unknown as string),
          text: this.newComment,
          createdAt: new Date().toISOString(),
        });
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
    return this.users().find((u) => u.id === userCode)?.name || 'Unassigned';
  }

  loadAttachments() {
    // 3. Guard check: Only proceed if selectedStory and its ID exist
    if (!this.selectedStory || !this.selectedStory.id) return;

    this.apiService
      .getAttachments(WorkItemType.Story, this.apiService.toApiWorkItemId(this.selectedStory.id))
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
    // 4. Guard check: Ensure story exists, files exist, and the ID is present
    if (!this.selectedStory || !this.selectedStory.id || !this.selectedFiles().length) return;

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