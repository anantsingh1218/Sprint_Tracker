
export interface ProductSummary {

  productId: number;

  productName: string;

  sprintName: string;

  progress: number;

  totalFeatures: number;

  totalStories: number;

  completedStories: number;

  totalTasks: number;

  completedTasks: number;

  pendingTasks: number;

  blockedTasks: number;

}

export interface SprintProgress {

    totalFeatures:number;

    totalStories:number;

    totalTasks:number;

    completedTasks:number;

    pendingTasks:number;

    blockedTasks:number;

    completion:number;

}

export interface PMDashboard {

  totalProducts: number;

  activeSprints: number;

  totalFeatures: number;

  totalStories: number;

  completedStories: number;

  totalTasks: number;

  completedTasks: number;

  blockedTasks: number;

  products: ProductSummary[];

}


export interface DashboardResponse {

  role: string;

  dashboard: PMDashboard;

}

export interface ProductDropdown {

    productId:number;

    productName:string;

}

export interface Velocity {

    sprintName: string;

    completedStories: number;

    totalStories: number;

    velocity: number;

}


export interface Burndown {

    sprintName: string;

    labels: string[];

    actual: number[];

    ideal: number[];

}
export interface TeamCapacity{

}

export interface ChartItem {

    label: string;

    value: number;

}

export interface TeamCapacity {

    totalMembers: number;

    availableMembers: number;

    busyMembers: number;

    overloadedMembers: number;

}

export interface ReleaseReadiness {

    productName: string;

    sprintName: string;

    completion: number;

    openBugs: number;

    blockedTasks: number;

    releaseReady: boolean;

}