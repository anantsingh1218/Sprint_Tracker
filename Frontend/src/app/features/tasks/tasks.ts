import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  OnInit,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { TaskService } from './task.service';
import { signal } from '@angular/core';

import { CdkDragDrop, DragDropModule } from '@angular/cdk/drag-drop';

import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { TaskOverlay } from '../task-overlay/task-overlay';
import { ITask } from '../../models/taskInterface';
import { ITasksResponse } from '../../models/taskResponseInterface';
import { Priority, WorkStatus } from '../../models/workItem';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DragDropModule,
    MatAutocompleteModule,
    MatInputModule,
    TaskOverlay,
  ],
  templateUrl: './tasks.html',
  styleUrl: './tasks.css',
})
export class Tasks implements OnChanges, OnInit {
  tasksList: ITask[] = [];
  @Input() task: ITask | null = null;
  @Output() save = new EventEmitter<any>();
  @Output() close = new EventEmitter<void>();

  showTaskModal = false;
  showEditModal = false;
  selectedSprintId = '';

  selectedTask: any = null;
  // Dependency Data Signals
  sprints = signal<any[]>([]);
  users = signal<any[]>([]);
  stories = signal<any[]>([]);

  newTask = {
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

  editTask: ITask | null = null;
  selectedTaskForOverlay: ITask | null = null;

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute,
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['task'] && this.task) {
      if (this.task.title === '') {
        // It's a brand new task draft being created from the Backlog row
        this.newTask = {
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
        this.showTaskModal = true;
      } else {
        // It's an existing task being opened for modification
        this.editTask = {
          ...this.task,
          estimatedHours: this.task.estimatedHours,
          remainingHours: this.task.remainingHours,
        };
        this.showEditModal = true;
      }
    }
  }

  ngOnInit(): void {
    const taskCode = this.route.snapshot.paramMap.get('id');

    if (taskCode) {
      this.taskService.getTaskById(taskCode).subscribe((res) => {
        this.selectedTask = res;
      });
    }

    this.loadTasks();
    this.loadDependencyData();
  }

  mapResponseToInterface(taskRes: ITasksResponse): ITask {
    return {
      id: taskRes.taskCode,
      title: taskRes.title,
      description: taskRes.body,
      status: taskRes.taskstatus,
      priority: taskRes.priority,
      estimatedHours: taskRes.originalestimatehours,
      remainingHours: taskRes.remainingestimatehours,
      storyCode: taskRes.storyCode,
      sprintCode: taskRes.sprintCode,
      userCode: taskRes.userCode,
      comments: taskRes.commentsList,
    };
  }

  loadTasks() {
    this.taskService.getTasks().subscribe({
      next: (res) => {
        this.tasksList = res.map(task => this.mapResponseToInterface(task));
      },
      error: (err) => console.error('Error loading tasks', err),
    });
  }

  loadDependencyData() {
    this.taskService.getSprintsDropdown().subscribe({
      next: (res) => this.sprints.set(res),
      error: (err) => console.error('Error loading sprints', err),
    });
    this.taskService.getUsersDropdown().subscribe({
      next: (res) => this.users.set(res),
      error: (err) => console.error('Error loading users', err),
    });
    this.taskService.getStoriesDropdown().subscribe({
      next: (res) => this.stories.set(res),
      error: (err) => console.error('Error loading stories', err),
    });
  }

  get allTasks() {
    return this.tasksList;
  }

  get filteredTasks() {
    if (!this.selectedSprintId) return this.allTasks;
    return this.allTasks.filter((t) => {
    // Convert both to strings and trim any whitespace to prevent false negatives
    const taskSprint = String(t.sprintCode || '').trim();
    const selectedSprint = String(this.selectedSprintId).trim();
    
    return taskSprint === selectedSprint;
  });
  }

  get todoTasks() {
    return this.filteredTasks.filter((t) => t.status === WorkStatus.OPEN);
  }

  get inProgressTasks() {
    return this.filteredTasks.filter((t) => t.status === WorkStatus.IN_PROGRESS);
  }

  get doneTasks() {
    return this.filteredTasks.filter((t) => t.status === WorkStatus.DONE || t.status === WorkStatus.CLOSED);
  }

  openTaskForm() {
    this.selectedTaskForOverlay = {
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
    }; // null means new task
    this.showTaskModal = true;
  }

  closeTaskForm() {
    this.showTaskModal = false;
    this.close.emit();
  }

  openEditTask(task: any) {
    this.selectedTaskForOverlay = { ...task };
    this.showEditModal = true;
  }

  closeEditTask() {
    this.showEditModal = false;
    this.editTask = null;
    this.close.emit();
  }

  createTask() {
    if (!this.newTask.title) return;

    const original = Number(this.newTask.estimatedHours);
    const remaining = Number(this.newTask.remainingHours);

    if (original < 0 || remaining < 0) {
      alert('Estimates cannot be negative');
      return;
    }

    if (remaining > original) {
      alert('Remaining cannot be greater than original');
      return;
    }

    this.taskService
      .addTask({
        ...this.newTask,
        sprintCode: this.selectedSprintId,
      })
      .subscribe({
        next: () => {
          this.loadTasks();
          this.closeTaskForm();
        },
        error: (err) => console.error('Failed to create task', err),
      });

    this.newTask = {
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
  }

  saveTaskChanges() {
    const original = Number(this.editTask?.estimatedHours);
    const remaining = Number(this.editTask?.remainingHours);

    if (original < 0 || remaining < 0) {
      alert('Estimates cannot be negative');
      return;
    }

    if (remaining > original) {
      alert('Remaining cannot be greater than original');
      return;
    }

    this.taskService.updateTask(this.editTask).subscribe({
      next: () => {
        this.loadTasks();
        this.closeEditTask();
      },
      error: (err) => console.error('Failed to update task', err),
    });
  }

  drop(event: CdkDragDrop<any[]>, newStatus: string) {
    const task = event.previousContainer.data[event.previousIndex];

    this.taskService.updateTaskStatus(task, newStatus).subscribe(() => {
      this.loadTasks();
    });
  }

  onTaskSaved(savedTask: ITask) {
    // Optionally refresh tasks list from API here
    // For now we just close the modal
    console.log(savedTask);
    let {comments, ...destructedPayload} = savedTask;
    const payloadToSend = {...destructedPayload, comments: comments?.at(-1)?.text};
    console.log("To Save Payload = " + JSON.stringify(payloadToSend));
    if (savedTask.id){
      this.taskService.updateTask(payloadToSend).subscribe({
        next: (data : ITasksResponse) => {
          this.tasksList.push(this.mapResponseToInterface(data));
          this.loadTasks();
        },
        error: (err) => console.error('Failed to update task', err),
      });
    }
    else{
      this.taskService.addTask(payloadToSend).subscribe({
        next: (data : ITasksResponse) => {
          this.tasksList.push(this.mapResponseToInterface(data));
          this.loadTasks();
        },
        error: (err) => console.error('Failed to update task', err),
      });
    }
    this.closeTaskOverlay();
  }

  closeTaskOverlay() {
    this.showTaskModal = false;
    this.showEditModal = false;
    this.selectedTaskForOverlay = null;
  }

  trackById(index: number, item: any) {
    return item.id;
  }
}
