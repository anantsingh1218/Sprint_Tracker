import { Component, OnInit , ChangeDetectorRef} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Bug } from '../bug/bug';
import { IBug } from '../../models/bugInterface';
import { Priority, WorkStatus } from '../../models/workItem';
import { BugService } from '../bug/bug.service';
import { SprintService } from '../sprint/sprint.service';
import { StoryService } from '../story-list/story.service';

@Component({
  selector: 'app-story-list',
  standalone: true,
  imports: [CommonModule, FormsModule, Bug],
  templateUrl: './bug-list.html',
  styleUrl: './bug-list.css',
})
export class BugList implements OnInit {
  isBugOpen = false;
  selectedBug!: IBug;
  bugs: IBug[] = [];

  filterSprintCode: string = '';
  filterStoryCode: string = '';

  sprints: any[] = [];
  stories: any[] = [];
  users: any[] = [];


  showDeletePopup=false;
  bugToDelete: IBug|null =null;

  constructor(
    private bugService: BugService,
    private sprintService: SprintService,
    private storyService: StoryService,
    private cdr:ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.fetchBugs();
    this.fetchDropdowns();
  }

  fetchDropdowns() {
    this.sprintService.getSprints().subscribe({
      next: (res) => this.sprints = res,
      error: (err) => console.error(err)
    });
    this.storyService.getStories().subscribe({
      next: (res) => this.stories = res,
      error: (err) => console.error(err)
    });
    this.storyService.getUsersDropdown().subscribe({
      next: (res) => this.users = res,
      error: (err) => console.error(err)
    });
  }

  fetchBugs() {
    this.bugService.getAllBugs().subscribe({
      next: (res: IBug[]) => {
        this.bugs = res;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching bugs:', err)
    });
  }

  getSprintName(code: string | null): string {
    if (!code) return '';
    const sprint = this.sprints.find(s => 'SP' + s.id === code);
    return sprint ? sprint.sprintName : code;
  }

  getStoryName(code: string | null): string {
    if (!code) return '';
    const story = this.stories.find(s => 'S' + s.id === code);
    return story ? story.title : code;
  }

  get filteredBugs() {
    return this.bugs.filter(b => {
      let matchSprint = true;
      let matchStory = true;
      if (this.filterSprintCode.trim()) {
        matchSprint = b.sprintCode === this.filterSprintCode.trim();
      }
      if (this.filterStoryCode.trim()) {
        matchStory = b.storyCode === this.filterStoryCode.trim();
      }
      return matchSprint && matchStory;
    });
  }

  get openBugs() { return this.filteredBugs.filter(b => b.bugstatus as any === 'OPEN'); }
  get inProgressBugs() { return this.filteredBugs.filter(b => b.bugstatus as any === 'IN_PROGRESS'); }
  get closedBugs() { return this.filteredBugs.filter(b => b.bugstatus as any === 'CLOSED'); }

  openBug(bug: IBug) {
    this.selectedBug = { ...bug };
    this.isBugOpen = true;
  }

  closeBug() {
    this.isBugOpen = false;
  }

  openCreateBug() {
    this.selectedBug = {
      id: 0,
      bugCode: '',
      title: '',
      description: '',
      bugstatus: WorkStatus.OPEN,
      priority: Priority.LOW,
      originalestimatehours: 0,
      remainingestimatehours: 0,
      storyCode: this.filterStoryCode.trim() || null,
      sprintCode: this.filterSprintCode.trim() || null,
      assignedUserCode: null,
      reopencount: 0
    };
    this.isBugOpen = true;
  }

  saveBug(updated: IBug) {
    if (updated.id && updated.id > 0) {
       this.bugService.updateBug(updated.id, updated).subscribe(() => this.fetchBugs());
    } else {
       this.bugService.createBug(updated).subscribe(() => this.fetchBugs());
    }
    this.closeBug();
  }


  deleteBug(bug: IBug, event: Event): void {

    event.stopPropagation();

    this.bugToDelete = bug;
    this.showDeletePopup = true;

  }

  // ==========================
  // YES button
  // ==========================
  confirmDelete(): void {

    if (!this.bugToDelete) {
      return;
    }

    this.bugService.deleteBug(this.bugToDelete.id).subscribe({

      next: () => {

        this.fetchBugs();

        this.showDeletePopup = false;
        this.bugToDelete = null;

      },

      error: err => {

        console.error('Error deleting bug:', err);

        this.showDeletePopup = false;
        this.bugToDelete = null;

      }

    });

  }

  // ==========================
  // NO button
  // ==========================
  cancelDelete(): void {

    this.showDeletePopup = false;
    this.bugToDelete = null;

  }

}
