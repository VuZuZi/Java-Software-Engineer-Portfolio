import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { conversations, profile } from '../../shared/portfolio-data';

type Message = {
  fromMe: boolean;
  name: string;
  content: string;
  time: string;
};

@Component({
  selector: 'app-contact',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './contact.component.html',
})
export class ContactComponent {
  readonly profile = profile;
  readonly conversations = conversations;
  selected = conversations[0];

  newSubject = '';
  newContent = '';
  replyContent = '';
  toast = '';

  selectConversation(conversation: (typeof conversations)[number]): void {
    this.selected = conversation;
    this.replyContent = '';
  }

  sendNewMessage(): void {
    const subject = this.newSubject.trim();
    const content = this.newContent.trim();
    if (!subject || !content) {
      this.showToast('Vui lòng nhập đầy đủ tiêu đề và nội dung.');
      return;
    }

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
    this.newSubject = '';
    this.newContent = '';
    this.showToast('Tin nhắn đã được thêm vào giao diện Angular.');
  }

  sendReply(): void {
    const content = this.replyContent.trim();
    if (!content) {
      this.showToast('Vui lòng nhập nội dung trả lời.');
      return;
    }

    this.selected.messages.push({
      fromMe: true,
      name: 'Bạn',
      content,
      time: new Date().toLocaleString('vi-VN'),
    });
    this.selected.lastAt = new Date().toLocaleString('vi-VN');
    this.replyContent = '';
    this.showToast('Đã gửi phản hồi trong UI.');
  }

  private showToast(message: string): void {
    this.toast = message;
    window.setTimeout(() => {
      this.toast = '';
    }, 2200);
  }
}
