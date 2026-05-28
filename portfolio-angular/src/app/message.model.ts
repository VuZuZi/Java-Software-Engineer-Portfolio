export interface ContactMessage {
  id: number;
  senderName: string;
  senderEmail: string;
  subject: string;
  content: string;
  read: boolean;
  createdAt: string;
}

export type ContactMessagePayload = Omit<ContactMessage, 'id' | 'read' | 'createdAt'>;
