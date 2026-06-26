import { WorkItemType } from "./workItem"

export interface Attachment{
    filename: string,
    createdBy: string,
    createdAt: Date,
    updatedBy: string,
    updatedAt: Date,
    entityType: WorkItemType,
    entityId: number
}