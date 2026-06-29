import { IComment } from "./storyInterface";

export interface IFeature {
  id: number;
  title: string;
  description: string;

  productId: number | null;
  sprintId: number | null;
  userId: number | null;

  status: 'New' | 'Active' | 'Resolved' | 'Closed';
  priority: 'Low' | 'Medium' | 'High';

  estimatedStoryPoints: number;
  remainingStoryPoint: number;

  comments: IComment[];
}