import { CommonModule } from '@angular/common';
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { catchError, of } from 'rxjs';
import { ApiService } from '../../shared/api.service';
import { HeaderAuthComponent } from '../../shared/header-auth.component';
import { LanguageService } from '../../core/language.service';
import { AuthService } from '../../core/auth.service';
import { profile, projects, services, skills } from '../../shared/portfolio-data';

type Language = 'vi' | 'en';

@Component({
  selector: 'app-landing',
  imports: [CommonModule, FormsModule, RouterLink, HeaderAuthComponent],
  templateUrl: './landing.component.html',
})
export class LandingComponent implements OnInit, OnDestroy {
  readonly profile = profile;
  readonly services = services;
  projects = projects;
  skills = skills.map((skill) => skill.name);
  readonly sections = ['home', 'services', 'projects', 'skills', 'contact'];

  activeSection = 'home';
  isScrolled = false;
  language: Language = 'vi';
  typedText = '';

  newSubject = '';
  newContent = '';
  toast = '';

  private typedWords: Record<Language, string[]> = {
    vi: ['Đoàn Đình Vũ', 'Java Developer', 'Spring Boot Developer', 'Problem Solver'],
    en: ['Doan Dinh Vu', 'Java Developer', 'Spring Boot Developer', 'Problem Solver'],
  };
  private wordIndex = 0;
  private charIndex = 0;
  private deleting = false;
  private timer?: number;

  constructor(
    private readonly api: ApiService,
    private readonly languageService: LanguageService,
    public readonly authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.language = this.languageService.current;
    this.languageService.language$.subscribe((language) => {
      this.language = language;
      this.wordIndex = 0;
      this.charIndex = 0;
      this.deleting = false;
    });

    this.loadBackendData();
    this.startTyping();
  }

  ngOnDestroy(): void {
    if (this.timer) {
      window.clearTimeout(this.timer);
    }
  }

  @HostListener('window:scroll')
  onScroll(): void {
    this.isScrolled = window.scrollY > 50;
    const scrollPosition = window.scrollY + 160;
    for (const sectionId of this.sections) {
      const section = document.getElementById(sectionId);
      if (section && scrollPosition >= section.offsetTop) {
        this.activeSection = sectionId;
      }
    }
  }

  toggleLanguage(): void {
    this.language = this.language === 'vi' ? 'en' : 'vi';
    localStorage.setItem('portfolioLanguage', this.language);
    this.wordIndex = 0;
    this.charIndex = 0;
    this.deleting = false;
  }

  scrollTo(sectionId: string): void {
    document.getElementById(sectionId)?.scrollIntoView({ behavior: 'smooth', block: 'start' });
    this.activeSection = sectionId;
  }

  t(vi: string, en: string): string {
    return this.language === 'vi' ? vi : en;
  }

  private startTyping(): void {
    const words = this.typedWords[this.language];
    const word = words[this.wordIndex % words.length];

    this.typedText = word.slice(0, this.charIndex);

    if (!this.deleting && this.charIndex < word.length) {
      this.charIndex += 1;
    } else if (this.deleting && this.charIndex > 0) {
      this.charIndex -= 1;
    } else if (!this.deleting) {
      this.deleting = true;
      this.timer = window.setTimeout(() => this.startTyping(), 1200);
      return;
    } else {
      this.deleting = false;
      this.wordIndex += 1;
    }

    this.timer = window.setTimeout(() => this.startTyping(), this.deleting ? 35 : 65);
  }

  private loadBackendData(): void {
    this.api.projects().pipe(catchError(() => of([]))).subscribe((apiProjects) => {
      if (!apiProjects.length) {
        return;
      }

      this.projects = apiProjects.slice(0, 3).map((project, index) => ({
        name: project.name || projects[index]?.name || 'Project',
        description: project.description || project.technologies || projects[index]?.description || '',
        image: project.imageUrl || project.logoUrl || projects[index % projects.length].image,
        status: project.status || 'OPEN',
        skills: project.requiredSkills?.map((skill) => skill.name) || [],
      }));
    });

    this.api.skills().pipe(catchError(() => of([]))).subscribe((apiSkills) => {
      if (apiSkills.length) {
        this.skills = apiSkills.map((skill) => skill.name);
      }
    });
  }


  sendNewMessage(): void {
    const subject = this.newSubject.trim();
    const content = this.newContent.trim();

    if (!subject || !content) {
      this.showToast(this.t('Vui lòng nhập tiêu đề và nội dung.', 'Please enter a subject and message.'));
      return;
    }

    this.api.sendMessage({ subject, content }).pipe(catchError(() => of({ success: false, message: this.t('Gửi tin nhắn thất bại.', 'Message send failed.') }))).subscribe((response) => {
      if (!response?.success) {
        this.showToast(response?.message || this.t('Không thể gửi tin nhắn.', 'Unable to send message.'));
        return;
      }

      this.newSubject = '';
      this.newContent = '';
      this.showToast(response.message || this.t('Tin nhắn đã gửi tới admin.', 'Message sent to admin.'));
    });
  }

  private showToast(message: string): void {
    this.toast = message;
    window.setTimeout(() => {
      this.toast = '';
    }, 2600);
  }
}
