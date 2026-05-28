export interface Project {
  id: number;
  title: string;
  description: string;
  imageUrl?: string | null;
  sourceUrl?: string | null;
  liveUrl?: string | null;
  technologies?: string | null;
}

export type ProjectPayload = Omit<Project, 'id'>;
