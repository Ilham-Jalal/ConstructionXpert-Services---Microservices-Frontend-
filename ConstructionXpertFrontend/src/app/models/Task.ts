import {Priority} from "../enums/Priority";
import {Status} from "../enums/Status";

export class Task {
  id?: number;
  title: string;
  type: string;
  startDate: Date;
  endDate: Date;
  description: string;
  priority: Priority;
  status: Status;
  projectId: number;
  rIds: number[];

  constructor(
    title: string,
    type: string,
    startDate: Date,
    endDate: Date,
    description: string,
    priority: Priority,
    status: Status,
    projectId: number,
    rIds: number[],
    id?: number
  ) {
    this.title = title;
    this.type = type;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
    this.priority = priority;
    this.status = status;
    this.projectId = projectId;
    this.rIds = rIds;
    if (id) this.id = id;
  }




}
