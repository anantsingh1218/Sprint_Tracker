// import {
//   Component,
//   EventEmitter,
//   Inject,
//   Input,
//   OnChanges,
//   OnInit,
//   Optional,
//   Output,
//   signal,
//   SimpleChanges,
// } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule } from '@angular/forms';
// import { IComment, IStory } from '../../models/storyInterface';
// import { ApiService } from '../../core/apiService/api-service';
// import { Attachment } from '../../models/attachmentInterface';
// import { HttpErrorResponse } from '@angular/common/http';
// import { WorkItemType } from '../../models/workItem';

// import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';


// @Component({
//   selector: 'app-story',
//   standalone: true,
//   imports: [CommonModule, FormsModule],
//   templateUrl: './story.html',
//   styleUrl: './story.css',
// })
// export class Story implements OnInit, OnChanges {
//   @Input() story!: IStory;
//   @Output() save = new EventEmitter<IStory>();
//   @Output() close = new EventEmitter<void>();
//   selectedFiles = signal<File[]>([]);
//   attachments = signal<Attachment[]>([]);
//   isAttachmentView = false;
//   attachmentUploadStatus = signal('');

//   constructor(private apiService: ApiService,
//     @Optional()
//     private dialogRef: MatDialogRef<Story>,
//     @Optional()
//     @Inject(MAT_DIALOG_DATA)
//     private dialogData: { story?: IStory } | null,
//   ) {}

//   newComment = '';

//   users = [
//     { id: 1, name: 'John Doe' },
//     { id: 2, name: 'Sarah Lee' },
//     { id: 3, name: 'Mike Johnson' },
//   ];

//   features = [
//     { id: 1, name: 'Login Module' },
//     { id: 2, name: 'Sprint Module' },
//   ];

//   sprints = [
//     { id: 1, name: 'Sprint 1' },
//     { id: 2, name: 'Sprint 2' },
//   ];

//   saveStory() {
//     if (this.dialogRef) {
//       this.dialogRef.close(this.story);
//       return;
//     }

//     this.save.emit(this.story);
//   }

//   closeOverlay() {
//     if (this.dialogRef) {
//       this.dialogRef.close();
//       return;
//     }

//     this.close.emit();
//   }

//   addComment() {
//     if (!this.newComment.trim()) return;

//     const comment: IComment = {
//       userId: 1,
//       text: this.newComment,
//       createdAt: new Date().toISOString(),
//     };
//     this.story.comments ??= [];
//     this.story.comments.push(comment);
//     this.newComment = '';
//   }

//   getUserName(userId: number | null): string {
//     return this.users.find((u) => u.id === userId)?.name || 'Unassigned';
//   }

//   toggleAttachmentsView() {
//     this.isAttachmentView = !this.isAttachmentView;
//   }

//   loadAttachments() {
//     this.apiService.getAttachments(WorkItemType.Story, this.apiService.toApiWorkItemId(this.story.id)).subscribe({
//       next: (data) => {
//         this.attachments.set(data.fileToBeFetched);
//       },
//       error: (err: HttpErrorResponse) => {
//         this.attachments.set([]);
//         if (err.status === 500) {
//           this.attachmentUploadStatus.set('Resource not found');
//         }
//       },
//     });
//   }

//   ngOnInit(): void {
//     if (this.dialogData?.story) {
//       this.story = this.dialogData.story;
//     }

//     this.story.comments ??= [];

//     if (this.story.id) {
//       this.loadAttachments();
//     }
//   }

//   ngOnChanges(changes: SimpleChanges) {
//     if (changes['story'] && this.story?.id) {
//       this.story.comments ??= [];
//       this.loadAttachments();
//     }
//   }

//   onFileSelected(event: any) {
//     const files: FileList = event.target.files;

//     if (!files) return;

//     this.selectedFiles.set(Array.from(files));
//   }

//   uploadFiles() {
//     if (!this.selectedFiles().length) return;

//     this.apiService.uploadAttachments(WorkItemType.Story, this.apiService.toApiWorkItemId(this.story.id), this.selectedFiles()).subscribe({
//       next: (res: Attachment[]) => {
//         this.selectedFiles.set([]);
//         this.attachmentUploadStatus.set('Files uploaded Successfully');
//         this.attachments.set(res);
//       },
//     });
//   }

//   deleteAttachment(filename: string) {
//     this.apiService
//       .deleteRequest('/attachment/delete', {
//         body: { filename: filename },
//       })
//       .subscribe(() => {
//         this.attachments.update((list) => list.filter((a) => a.filename !== filename));
//       });
//   }
// }
