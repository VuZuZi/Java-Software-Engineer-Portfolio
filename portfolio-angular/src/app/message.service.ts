import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ContactMessage, ContactMessagePayload } from './message.model';

@Injectable({ providedIn: 'root' })
export class MessageService {
  private readonly apiUrl = 'http://localhost:8080/api/messages';

  constructor(private readonly http: HttpClient) {}

  getMessages(): Observable<ContactMessage[]> {
    return this.http.get<ContactMessage[]>(this.apiUrl);
  }

  createMessage(payload: ContactMessagePayload): Observable<ContactMessage> {
    return this.http.post<ContactMessage>(this.apiUrl, payload);
  }

  markAsRead(id: number): Observable<ContactMessage> {
    return this.http.patch<ContactMessage>(`${this.apiUrl}/${id}/read`, {});
  }

  deleteMessage(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
