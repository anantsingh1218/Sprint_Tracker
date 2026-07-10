import { IComment } from './storyInterface';
import { Priority, WorkStatus } from './workItem';

export interface IBug {
  id: number;
  bugCode: string;
  title: string;
  description: string;
  storyCode: string | null;
  sprintCode: string | null;
  assignedUserCode: string | null;
  bugstatus: WorkStatus;
  priority: Priority;
  originalestimatehours: number;
  remainingestimatehours: number;
  reopencount: number;

  comments?: IComment[];
}
