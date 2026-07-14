export interface TaskCard {

  id: number;

  taskCode: string;

  title: string;

  priority: string;

  story: string;

  remainingHours: number;

  assignee: string;

  status: string;

}

export interface BoardColumn {

  status: string;

  tasks: TaskCard[];

}