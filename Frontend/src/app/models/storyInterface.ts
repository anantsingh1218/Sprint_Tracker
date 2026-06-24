export interface IStory {
  id: number;
  title: string;
  body: string;

  featureId: number | null;
  sprintId: number | null;
  userId: number | null;

  status: 'New' | 'Active' | 'Resolved' | 'Closed';
  priority: 'Low' | 'Medium' | 'High';

  estimatedStoryPoints: number;
  remainingStoryPoint: number;

  comments: IComment[];
}

export interface IComment {

  userId: number;
  text: string;
  createdAt: string;
}