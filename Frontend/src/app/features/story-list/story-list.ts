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
    // 1. Safely normalize comments to always be an array of objects { text: string, ... }
    let normalizedComments: any[] = [];
    if (storyRes.comments) {
      if (Array.isArray(storyRes.comments)) {
        normalizedComments = storyRes.comments.map(c => 
          typeof c === 'string' ? { text: c, userCode: 'System' } : c
        );
      } else if (typeof storyRes.comments === 'string') {
        try {
          // If the string is a JSON array string
          const parsed = JSON.parse(storyRes.comments);
          normalizedComments = Array.isArray(parsed) 
            ? parsed.map(c => typeof c === 'string' ? { text: c } : c)
            : [{ text: storyRes.comments }];
        } catch {
          // Plain fallback string
          normalizedComments = [{ text: storyRes.comments }];
        }
      }
    }

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
      comments: normalizedComments,
    };
  }

  saveStory() {
    if (!this.selectedStory) return;

    const { comments, ...destructedPayload } = this.selectedStory;
    let commentText = '';

    if (Array.isArray(comments)) {
      const lastComment = comments.at(-1);
      commentText = typeof lastComment === 'object' ? (lastComment?.text ?? '') : (lastComment ?? '');
    } else if (typeof comments === 'string') {
      commentText = comments;
    }

    // 2. Build the correct payload
    const payloadToSend = {
      ...destructedPayload,
      // Strip out the client-side prefix 'S' from the payload ID if creating/updating
      id: this.selectedStory.id ? Number(this.selectedStory.id.substring(1)) : null,
      comments: commentText,
    };

    console.log('Sending payload:', payloadToSend);

    if (this.selectedStory.id) {
      this.St1.updateStory(Number(this.selectedStory.id.substring(1)), payloadToSend).subscribe({
        next: () => {
          this.loadStories();
          this.closeStory();
        },
        error: (err: any) => console.error('Update failed', err),
      });
    } else {
      this.St1.createStory(payloadToSend).subscribe({
        next: () => {
          this.loadStories();
          this.closeStory();
        },
        error: (err: any) => console.error('Creation failed', err),
      });
    }
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
      this.users().find((u) => {
        // Check if the list element has a raw userCode property matching "U2"
        if (u.userCode === userCode) return true;

        // Check if it matches your calculated UI prefix format: e.g. "U" + 2 === "U2"
        if (u.id && 'U' + u.id === userCode) return true;

        return false;
      })?.username || 'Unassigned'
    ); // Changed .name to .username based on your select loop
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