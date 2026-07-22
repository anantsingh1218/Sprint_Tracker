export interface DashboardResponse {

 role:string;

 dashboard:{

   sprintName:string;

   progress:number;

   totalTasks:number;

   completedTasks:number;

   inProgressTasks:number;

 };

}