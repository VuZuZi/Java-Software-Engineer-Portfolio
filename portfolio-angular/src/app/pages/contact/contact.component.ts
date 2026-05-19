import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderAuthComponent } from '../../shared/header-auth.component';
import { ApiConversation, ApiConversationMessage, ApiService } from '../../shared/api.service';
import { AuthService } from '../../core/auth.service';
import { catchError, finalize, of } from 'rxjs';

interface Message {
  id?: number;
  content: string;
  fromMe: boolean;
  name: string;
  time: string;
}

interface Conversation {
  id: string;
  subject: string;
  sender: string;
  senderEmail?: string;
  lastMessage: string;
  lastAt: string;
  unreadCount: number;
  messages: Message[];
}

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, HeaderAuthComponent],
  templateUrl: './contact.component.html'
})
export class ContactComponent implements OnInit, OnDestroy {
  conversations: Conversation[] = [];
  selectedConversation: Conversation | null = null;
  replyContent: string = '';
  toast: string = '';
  isLoading: boolean = false;
  isTyping: boolean = false;
  userEmail: string = '';
  private typingTimeout: any;

  constructor(
    private apiService: ApiService,
    private authService: AuthService
  ) {
    this.loadUserEmail();
  }

  ngOnInit(): void {
    this.loadConversations();
  }

  ngOnDestroy(): void {
    if (this.typingTimeout) {
      clearTimeout(this.typingTimeout);
    }
  }

  private loadUserEmail(): void {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        this.userEmail = user.email || '';
      } catch (e) {
        this.userEmail = '';
      }
    }
  }

  loadConversations(): void {
    this.isLoading = true;

    this.apiService.conversations()
      .pipe(
        catchError(() => of([] as ApiConversation[])),
        finalize(() => {
          this.isLoading = false;
        })
      )
      .subscribe((conversations) => {
        this.conversations = conversations.map((conversation) => this.mapConversationFromApi(conversation));
        if (!this.selectedConversation && this.conversations.length) {
          this.selectConversation(this.conversations[0]);
        }
      });
  }

  private mapConversationFromApi(conversation: ApiConversation): Conversation {
    return {
      id: conversation.id || '',
      subject: conversation.subject || 'Không có tiêu đề',
      sender: conversation.sender || 'Người dùng',
      senderEmail: undefined,
      lastMessage: conversation.lastMessage || '',
      lastAt: conversation.lastAt || '',
      unreadCount: conversation.unreadCount || 0,
      messages: (conversation.messages || []).map((message) => ({
        id: message.id,
        content: message.content || '',
        fromMe: Boolean(message.fromMe),
        name: message.name || '',
        time: message.time || '',
      })),
    };
  }

  selectConversation(conversation: Conversation): void {
    this.isLoading = true;
    this.apiService.conversationById(conversation.id)
      .pipe(
        catchError(() => of(null as ApiConversation | null)),
        finalize(() => {
          this.isLoading = false;
        })
      )
      .subscribe((detail) => {
        if (!detail) {
          return;
        }

        const selected = this.mapConversationFromApi(detail);
        this.selectedConversation = selected;

        const index = this.conversations.findIndex((c) => c.id === conversation.id);
        if (index !== -1) {
          this.conversations[index].unreadCount = 0;
        }
      });
  }

  sendReply(): void {
    if (!this.replyContent.trim() || !this.selectedConversation) return;

    const content = this.replyContent.trim();
    const subject = this.selectedConversation.subject;

    const newMessage: Message = {
      content,
      fromMe: true,
      name: 'You',
      time: this.getCurrentTime()
    };

    this.selectedConversation.messages.push(newMessage);
    this.selectedConversation.lastMessage = content;
    this.selectedConversation.lastAt = this.getCurrentTime();
    this.replyContent = '';
    this.scrollToBottom();

    this.apiService
      .sendMessage({ subject, content, conversationId: this.selectedConversation.id })
      .pipe(
        catchError(() => of({ success: false, message: 'Gửi tin nhắn thất bại.' }))
      )
      .subscribe((response) => {
        if (!response?.success) {
          this.showToast(response?.message || 'Gửi tin nhắn thất bại.');
          return;
        }

        this.showToast(response.message || 'Tin nhắn đã gửi.');
      });
  }

  getLastMessage(conversation: Conversation): string {
    return conversation.lastMessage || '';
  }

  formatTime(time: string): string {
    if (!time) return '';
    return time;
  }

  getCurrentTime(): string {
    const now = new Date();
    return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`;
  }

  showToast(message: string): void {
    this.toast = message;
    setTimeout(() => {
      this.toast = '';
    }, 3000);
  }

  markAsRead(event: Event): void {
    event.preventDefault();
    if (!this.selectedConversation) {
      return;
    }

    this.apiService.markConversationAsRead(this.selectedConversation.id)
      .pipe(catchError(() => of({ success: false })))
      .subscribe((response) => {
        if (response.success) {
          this.selectedConversation!.unreadCount = 0;
          const index = this.conversations.findIndex((c) => c.id === this.selectedConversation!.id);
          if (index !== -1) {
            this.conversations[index].unreadCount = 0;
          }
          this.showToast('Đã đánh dấu đã đọc');
        } else {
          this.showToast('Không thể đánh dấu đã đọc.');
        }
      });
  }

  deleteConversation(event: Event): void {
    event.preventDefault();
    if (this.selectedConversation && confirm('Bạn có chắc muốn xóa cuộc trò chuyện này?')) {
      const index = this.conversations.findIndex(c => c.id === this.selectedConversation?.id);
      if (index !== -1) {
        this.conversations.splice(index, 1);
        this.selectedConversation = null;
        this.showToast('Đã xóa cuộc trò chuyện');
      }
    }
  }

  onTyping(): void {
    if (this.typingTimeout) {
      clearTimeout(this.typingTimeout);
    }
    this.isTyping = true;
    this.typingTimeout = setTimeout(() => {
      this.isTyping = false;
    }, 1000);
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const messageStream = document.querySelector('.message-stream');
      if (messageStream) {
        messageStream.scrollTop = messageStream.scrollHeight;
      }
    }, 100);
  }
}