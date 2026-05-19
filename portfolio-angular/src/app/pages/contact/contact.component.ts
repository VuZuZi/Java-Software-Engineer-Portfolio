import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { catchError, of } from 'rxjs';
import { ApiMessage, ApiService } from '../../shared/api.service';
import { HeaderAuthComponent } from '../../shared/header-auth.component';
import { profile } from '../../shared/portfolio-data';

type Message = {
  fromMe: boolean;
  name: string;
  content: string;
  time: string;
};

type Conversation = {
  subject: string;
  sender: string;
  email: string;
  lastAt: string;
  messages: Message[];
};

const defaultAdminConversation: Conversation = {
  subject: 'Admin',
  sender: 'Admin',
  email: 'admin@example.com',
  lastAt: new Date().toLocaleString('vi-VN'),
  messages: [
    {
      fromMe: false,
      name: 'Admin',
      content: 'Xin chào! Đây là cuộc trò chuyện tạo sẵn với Admin.',
      time: new Date().toLocaleString('vi-VN'),
    },
  ],
};

@Component({
  selector: 'app-contact',
  imports: [CommonModule, FormsModule, HeaderAuthComponent],
  templateUrl: './contact.component.html',
})
export class ContactComponent implements OnInit {
  readonly profile = profile;
  conversations: Conversation[] = this.ensureAdminConversation([]);
  selected: Conversation = defaultAdminConversation;
  private selectedKey = this.conversationKey(this.selected);

  newSubject = '';
  newContent = '';
  replyContent = '';
  toast = '';

  constructor(private readonly api: ApiService) { }

  ngOnInit(): void {
    this.loadMessages();
  }

  selectConversation(conversation: Conversation): void {
    this.selected = conversation;
    this.selectedKey = this.conversationKey(conversation);
    this.replyContent = '';
  }

  sendNewMessage(): void {
    const subject = this.newSubject.trim();
    const content = this.newContent.trim();
    if (!subject || !content) {
      this.showToast('Vui lòng nhập đầy đủ tiêu đề và nội dung.');
      return;
    }

    this.api.sendMessage({ subject, content }).pipe(catchError(() => of(null))).subscribe((response) => {
      if (!response?.success) {
        this.showToast(response?.message || 'Không thể gửi tin nhắn. Hãy đăng nhập trước.');
        return;
      }

      this.loadMessages();
      this.newSubject = '';
      this.newContent = '';
      this.showToast(response.message || 'Tin nhắn đã được gửi.');
    });
  }

  sendReply(): void {
    const content = this.replyContent.trim();
    if (!content) {
      this.showToast('Vui lòng nhập nội dung trả lời.');
      return;
    }

    this.api
      .sendMessage({
        subject: this.selected.subject,
        content,
        receiverEmail: this.selected.email,
      })
      .pipe(catchError(() => of(null)))
      .subscribe((response) => {
        if (!response?.success) {
          this.showToast(response?.message || 'Không thể gửi phản hồi. Hãy đăng nhập trước.');
          return;
        }

        this.replyContent = '';
        this.selectedKey = this.conversationKey(this.selected);
        this.loadMessages();
        this.showToast(response.message || 'Đã gửi phản hồi.');
      });
  }

  private addLocalMessage(subject: string, content: string): void {
    const conversation = {
      subject,
      sender: 'Bạn',
      email: profile.email,
      lastAt: new Date().toLocaleString('vi-VN'),
      messages: [
        {
          fromMe: true,
          name: 'Bạn',
          content,
          time: new Date().toLocaleString('vi-VN'),
        },
      ] satisfies Message[],
    };

    this.conversations.unshift(conversation);
    this.selected = conversation;
  }

  private loadMessages(): void {
    this.api.messages().pipe(catchError(() => of([]))).subscribe((messages) => {
      if (!messages.length) {
        this.conversations = this.ensureAdminConversation([]);
        this.selected = this.conversations[0];
        this.selectedKey = this.conversationKey(this.selected);
        return;
      }

      this.conversations = this.ensureAdminConversation(this.groupMessages(messages));
      this.selected = this.conversations.find((conversation) => this.conversationKey(conversation) === this.selectedKey) || this.conversations[0];
      this.selectedKey = this.conversationKey(this.selected);
    });
  }

  private ensureAdminConversation(conversations: Conversation[]): Conversation[] {
    const filteredConversations = conversations.filter(
      (conversation) => conversation.subject !== 'Cơ hội hợp tác' && conversation.subject !== 'Review portfolio',
    );

    const hasAdmin = filteredConversations.some(
      (conversation) => conversation.subject === defaultAdminConversation.subject && conversation.email === defaultAdminConversation.email,
    );

    if (hasAdmin) {
      return [
        defaultAdminConversation,
        ...filteredConversations.filter(
          (conversation) => !(conversation.subject === defaultAdminConversation.subject && conversation.email === defaultAdminConversation.email),
        ),
      ];
    }

    return [defaultAdminConversation, ...filteredConversations];
  }

  private conversationKey(conversation: Conversation): string {
    return `${conversation.subject}|${conversation.email}`;
  }

  private groupMessages(messages: ApiMessage[]): Conversation[] {
    const groups = new Map<string, Conversation>();
    const currentUserEmail = profile.email;

    for (const message of messages) {
      const otherParty = message.email === currentUserEmail ? message.receiverEmail : message.email;
      const key = `${message.subject}|${otherParty}`;
      const existing = groups.get(key);
      const chatMessage: Message = {
        fromMe: message.email === currentUserEmail,
        name: message.name,
        content: message.content,
        time: message.sentAt ? new Date(message.sentAt).toLocaleString('vi-VN') : '',
      };

      if (!existing) {
        groups.set(key, {
          subject: message.subject,
          sender: message.name,
          email: otherParty,
          lastAt: chatMessage.time,
          messages: [chatMessage],
        });
      } else {
        existing.messages.push(chatMessage);
        existing.lastAt = chatMessage.time;
      }
    }

    return Array.from(groups.values()).map((conversation) => ({
      ...conversation,
      messages: conversation.messages.sort((a: Message, b: Message) => a.time.localeCompare(b.time)),
    }));
  }

  private showToast(message: string): void {
    this.toast = message;
    window.setTimeout(() => {
      this.toast = '';
    }, 2200);
  }
}
