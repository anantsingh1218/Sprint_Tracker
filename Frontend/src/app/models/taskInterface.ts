export type TaskStatus = 'Todo' | 'InProgress' | 'Done';

export interface Task {
  id: string;
  sprintId: string;
  title: string;
  status: TaskStatus;
}