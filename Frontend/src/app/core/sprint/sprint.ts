import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SprintService {

  private sprints: any[] = [];
  selectedSprint:any=[];

  private nextId = 3;

  // GET all sprints
  getSprints() {
    return this.sprints;
  }

  // ADD sprint
  addSprint(sprint: any) {
    const newSprint = {
      id: String(this.nextId++),
      ...sprint,
      status: sprint.status || 'Planned'
    };

    this.sprints.push(newSprint);
  }

  // FIND sprint name
  getSprintName(id: string) {
    return this.sprints.find(s => s.id === id)?.name || 'No Sprint';
  }
}
