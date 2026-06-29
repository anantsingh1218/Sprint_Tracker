export type SprintStatus = 'Planned' | 'Active' | 'Completed';

export interface Sprint {
  id: string;
  name: string;
  description?: string;
  startDate: string;
  endDate: string;
  duration: number;
  status: SprintStatus;
}