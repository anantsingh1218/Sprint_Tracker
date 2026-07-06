export interface TeamDashboardData {

    assignedTasks: number;

    completedTasks: number;

    pendingTasks: number;

}

export interface TeamDashboardResponse {

    role: string;

    dashboard: TeamDashboardData;

}