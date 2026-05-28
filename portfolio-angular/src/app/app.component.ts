import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, Subscription } from 'rxjs';
import { AuthPayload, RegisterPayload, UserProfile } from './auth.model';
import { AuthService } from './auth.service';
import { ContactMessage, ContactMessagePayload } from './message.model';
import { MessageService } from './message.service';
import { Project, ProjectPayload } from './project.model';
import { ProjectService } from './project.service';

type ProjectForm = ProjectPayload & { id?: number };
type AuthMode = 'login' | 'register';

const emptyProjectForm: ProjectForm = {
  title: '',
  description: '',
  imageUrl: '',
  sourceUrl: '',
  liveUrl: '',
  technologies: ''
};

const emptyAuthForm: RegisterPayload = {
  name: '',
  email: '',
  password: ''
};

const emptyMessageForm: ContactMessagePayload = {
  senderName: '',
  senderEmail: '',
  subject: '',
  content: ''
};

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  projects: Project[] = [];
  messages: ContactMessage[] = [];
  currentUser: UserProfile | null = null;

  projectForm: ProjectForm = { ...emptyProjectForm };
  authForm: RegisterPayload = { ...emptyAuthForm };
  messageForm: ContactMessagePayload = { ...emptyMessageForm };

  authMode: AuthMode = 'login';
  isLoadingProjects = false;
  isSavingProject = false;
  isSubmittingAuth = false;
  isSubmittingMessage = false;
  isLoadingMessages = false;

  projectError = '';
  authError = '';
  messageError = '';
  messageSuccess = '';

  private readonly subscriptions = new Subscription();

  constructor(
    private readonly authService: AuthService,
    private readonly messageService: MessageService,
    private readonly projectService: ProjectService
  ) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.authService.currentUser$.subscribe((user) => {
        this.currentUser = user;
        if (user) {
          this.loadMessages();
        } else {
          this.messages = [];
        }
      })
    );

    this.loadProjects();
    if (this.authService.consumeOAuthTokenFromUrl() || this.authService.token) {
      this.loadCurrentUser();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get isEditingProject(): boolean {
    return this.projectForm.id !== undefined;
  }

  setAuthMode(mode: AuthMode): void {
    this.authMode = mode;
    this.authError = '';
  }

  submitAuth(): void {
    this.isSubmittingAuth = true;
    this.authError = '';

    const request = this.authMode === 'register'
      ? this.authService.register(this.toRegisterPayload())
      : this.authService.login(this.toAuthPayload());

    request
      .pipe(finalize(() => this.isSubmittingAuth = false))
      .subscribe({
        next: () => this.authForm = { ...emptyAuthForm },
        error: (error) => this.authError = this.toErrorMessage(error, 'Khong the dang nhap. Vui long thu lai.')
      });
  }

  logout(): void {
    this.authService.logout();
    this.projectForm = { ...emptyProjectForm };
    this.messages = [];
  }

  loadProjects(): void {
    this.isLoadingProjects = true;
    this.projectError = '';

    this.projectService.getProjects()
      .pipe(finalize(() => this.isLoadingProjects = false))
      .subscribe({
        next: (projects) => this.projects = projects,
        error: (error) => this.projectError = this.toErrorMessage(
          error,
          'Khong the tai danh sach project. Kiem tra backend o cong 8080.'
        )
      });
  }

  saveProject(): void {
    if (!this.currentUser) {
      this.projectError = 'Ban can dang nhap de quan ly project.';
      return;
    }

    const payload = this.toProjectPayload();
    if (!payload.title || !payload.description) {
      this.projectError = 'Title va description la bat buoc.';
      return;
    }

    this.isSavingProject = true;
    this.projectError = '';
    const request = this.isEditingProject
      ? this.projectService.updateProject(this.projectForm.id as number, payload)
      : this.projectService.createProject(payload);

    request
      .pipe(finalize(() => this.isSavingProject = false))
      .subscribe({
        next: () => {
          this.resetProjectForm();
          this.loadProjects();
        },
        error: (error) => this.projectError = this.toErrorMessage(
          error,
          'Khong the luu project. Vui long kiem tra du lieu hoac quyen dang nhap.'
        )
      });
  }

  editProject(project: Project): void {
    this.projectForm = {
      id: project.id,
      title: project.title,
      description: project.description,
      imageUrl: project.imageUrl ?? '',
      sourceUrl: project.sourceUrl ?? '',
      liveUrl: project.liveUrl ?? '',
      technologies: project.technologies ?? ''
    };
  }

  deleteProject(project: Project): void {
    if (!this.currentUser || !confirm(`Xoa project "${project.title}"?`)) {
      return;
    }

    this.projectError = '';
    this.projectService.deleteProject(project.id)
      .subscribe({
        next: () => this.projects = this.projects.filter((item) => item.id !== project.id),
        error: (error) => this.projectError = this.toErrorMessage(error, 'Khong the xoa project nay.')
      });
  }

  resetProjectForm(): void {
    this.projectForm = { ...emptyProjectForm };
  }

  submitMessage(): void {
    const payload = this.toMessagePayload();
    if (!payload.senderName || !payload.senderEmail || !payload.subject || !payload.content) {
      this.messageError = 'Vui long dien day du thong tin lien he.';
      return;
    }

    this.isSubmittingMessage = true;
    this.messageError = '';
    this.messageSuccess = '';

    this.messageService.createMessage(payload)
      .pipe(finalize(() => this.isSubmittingMessage = false))
      .subscribe({
        next: () => {
          this.messageForm = { ...emptyMessageForm };
          this.messageSuccess = 'Tin nhan da duoc gui.';
          if (this.currentUser) {
            this.loadMessages();
          }
        },
        error: (error) => this.messageError = this.toErrorMessage(error, 'Khong the gui tin nhan luc nay.')
      });
  }

  loadMessages(): void {
    if (!this.currentUser) {
      return;
    }

    this.isLoadingMessages = true;
    this.messageError = '';

    this.messageService.getMessages()
      .pipe(finalize(() => this.isLoadingMessages = false))
      .subscribe({
        next: (messages) => this.messages = messages,
        error: (error) => this.messageError = this.toErrorMessage(error, 'Khong the tai inbox.')
      });
  }

  markMessageAsRead(message: ContactMessage): void {
    this.messageService.markAsRead(message.id)
      .subscribe({
        next: (updatedMessage) => {
          this.messages = this.messages.map((item) => item.id === updatedMessage.id ? updatedMessage : item);
        },
        error: (error) => this.messageError = this.toErrorMessage(error, 'Khong the cap nhat tin nhan.')
      });
  }

  deleteMessage(message: ContactMessage): void {
    if (!confirm(`Xoa tin nhan tu "${message.senderName}"?`)) {
      return;
    }

    this.messageService.deleteMessage(message.id)
      .subscribe({
        next: () => this.messages = this.messages.filter((item) => item.id !== message.id),
        error: (error) => this.messageError = this.toErrorMessage(error, 'Khong the xoa tin nhan.')
      });
  }

  private loadCurrentUser(): void {
    this.authService.loadProfile()
      .subscribe({
        error: () => this.authService.logout()
      });
  }

  private toAuthPayload(): AuthPayload {
    return {
      email: this.authForm.email.trim().toLowerCase(),
      password: this.authForm.password
    };
  }

  private toRegisterPayload(): RegisterPayload {
    return {
      name: this.authForm.name.trim(),
      ...this.toAuthPayload()
    };
  }

  private toProjectPayload(): ProjectPayload {
    return {
      title: this.projectForm.title.trim(),
      description: this.projectForm.description.trim(),
      imageUrl: this.cleanOptional(this.projectForm.imageUrl),
      sourceUrl: this.cleanOptional(this.projectForm.sourceUrl),
      liveUrl: this.cleanOptional(this.projectForm.liveUrl),
      technologies: this.cleanOptional(this.projectForm.technologies)
    };
  }

  private toMessagePayload(): ContactMessagePayload {
    return {
      senderName: this.messageForm.senderName.trim(),
      senderEmail: this.messageForm.senderEmail.trim().toLowerCase(),
      subject: this.messageForm.subject.trim(),
      content: this.messageForm.content.trim()
    };
  }

  private cleanOptional(value: string | null | undefined): string | null {
    const cleanValue = value?.trim();
    return cleanValue ? cleanValue : null;
  }

  private toErrorMessage(error: unknown, fallback: string): string {
    if (!(error instanceof HttpErrorResponse)) {
      return fallback;
    }

    const response = error.error as { message?: string; details?: Record<string, string> } | null;
    if (response?.details) {
      const firstDetail = Object.values(response.details)[0];
      if (firstDetail) {
        return firstDetail;
      }
    }

    return response?.message || fallback;
  }
}
