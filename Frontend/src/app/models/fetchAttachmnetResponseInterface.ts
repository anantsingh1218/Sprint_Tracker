import { Attachment } from "./attachmentInterface";

export interface FetchAttachmentsResponse {
  fileToBeFetched: Attachment[];
  numberOfFilesFetched: number;
  entityType: string;
  entityId: number;
}