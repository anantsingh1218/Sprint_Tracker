import { SearchService } from './../search/search.service';
import { Component, OnInit, signal, ChangeDetectorRef } from '@angular/core';
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
  stories: any[] = [];
  isStoryOpen = false;

  selectedStory: any = null;
  newComment = '';

  //Attachments Signals
  selectedFiles = signal<File[]>([]);
  attachments = signal<Attachment[]>([]);
  isAttachmentView = false;
  attachmentUploadStatus = signal('');

  // dependency data
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
      next: (data: any[]) => {
        this.stories = data;
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

    // Requests the features through the proper authenticated pipeline
    this.St1.getFeaturesDropdown().subscribe({
      next: (res) => this.features.set(res),
      error: (err) => console.error('Failed fetching features:', err),
    });

    // Requests the sprints through the proper authenticated pipeline
    this.St1.getSprintsDropdown().subscribe({
      next: (res) => this.sprints.set(res),
      error: (err) => console.error('Failed fetching sprints:', err),
    });
  }

  openStory(story: any) {
    // Spread Operator.
    // this line below actually opens the story
    // it takes a snapshot of all the properties inside the story object and duplicates them into a brand new, separate object.
    //Why do this? In JavaScript, if you pass an object directly (like this.selectedStory = story;), both variables point to the exact same place in memory. If the user starts editing the story title in a popup form, the title would instantly change in the background list too, even before they click "Save"! By copying it, the user can edit selectedStory all they want without breaking the original data
    this.selectedStory = { ...story };
    this.selectedStory.comments ??= [];
    this.isStoryOpen = true;

    if (this.selectedStory.id) {
      this.loadAttachments();
    }
  }

  openCreateStory() {
    this.selectedStory = {
      id: null,
      title: '',
      body: '',
      storyStatus: 'TODO',
      priority: 'Medium',
      storyPoints: 0,
      featureCode: null,
      sprintCode: null,
      userCode: null,
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
      comments: storyRes.comments,
    };
  }

  saveStory() {
    const payload = {
      title: this.selectedStory.title,
      body: this.selectedStory.body,
      featureCode: this.selectedStory.featureCode,
      sprintCode: this.selectedStory.sprintCode,
      userCode: this.selectedStory.userCode,
      status: this.selectedStory.storyStatus || 'TODO',
      priority: this.selectedStory.priority || 'Medium',
      storyPoints: this.selectedStory.storyPoints || 0,
      comments: this.newComment.trim() || null,
    };
    if (this.selectedStory.id) {
      this.St1.updateStory(this.selectedStory.id, payload).subscribe({
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
    if (!this.newComment.trim() || !this.selectedStory.id) return;

    this.St1.updateStory(this.selectedStory.id, { comments: this.newComment }).subscribe({
      next: () => {
        this.selectedStory.comments.push({
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

  // Add this method inside the StoryList class in story-list.ts
  getUserName(userCode: string | null): string {
    // Finds the user in the dynamic signal array matching the id
    return this.users().find((u) => u.id === userCode)?.name || 'Unassigned';
  }

  loadAttachments() {
    this.apiService
      .getAttachments(WorkItemType.Story, this.apiService.toApiWorkItemId('S' + String(this.selectedStory.id)))
      .subscribe({
        next: (data) => {
          if(data?.fileToBeFetched)
            this.attachments.set(data.fileToBeFetched);
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
    if (!this.selectedFiles().length || !this.selectedStory.id) return;
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
