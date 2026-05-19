import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export type ApiUser = {
  name?: string;
  email?: string;
  picture?: string;
  role?: string;
};

export type ApiAuthResponse = {
  authenticated: boolean;
  admin?: boolean;
  user?: ApiUser;
};

export type ApiProject = {
  id?: string;
  name?: string;
  description?: string;
  technologies?: string;
  imageUrl?: string;
  logoUrl?: string;
  status?: string;
  requiredSkills?: ApiSkill[];
};

export type ApiSkill = {
  id?: number;
  name: string;
  category?: string;
  icon?: string;
  yearsExperience?: number;
  proficiencyPercent?: number;
};

export type ApiMessage = {
  id?: number;
  name: string;
  email: string;
  receiverEmail: string;
  subject: string;
  content: string;
  sentAt?: string;
  status?: string;
};

export type DashboardResponse = {
  user: ApiUser;
  stats: {
    projectsCount: number;
    skillsCount: number;
    avgMatch: number;
  };
  projects: ApiProject[];
};

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private readonly http: HttpClient) {}

  me(): Observable<ApiAuthResponse> {
    return this.http.get<ApiAuthResponse>('/api/auth/me', { withCredentials: true });
  }

  dashboard(): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>('/api/dashboard', { withCredentials: true });
  }

  projects(): Observable<ApiProject[]> {
    return this.http.get<ApiProject[]>('/api/projects', { withCredentials: true });
  }

  skills(): Observable<ApiSkill[]> {
    return this.http.get<ApiSkill[]>('/api/skills', { withCredentials: true });
  }

  messages(): Observable<ApiMessage[]> {
    return this.http.get<ApiMessage[]>('/api/messages', { withCredentials: true });
  }

  sendMessage(payload: { subject: string; content: string; receiverEmail?: string }): Observable<{ success: boolean; message: string; data?: ApiMessage }> {
    return this.http.post<{ success: boolean; message: string; data?: ApiMessage }>('/api/messages', payload, {
      withCredentials: true,
    });
  }
}
