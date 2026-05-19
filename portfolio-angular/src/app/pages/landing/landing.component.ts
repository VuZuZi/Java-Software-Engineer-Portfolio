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

type LocalizedString = {
  vi: string;
  en: string;
};

interface Experience {
  title: string;
  period: string;
  technology: string;
  aisupport: string;
  description: LocalizedString | string;
  contribute: string;
  company: string;
}

interface Education {
  title: LocalizedString | string;
  period: string;
  institution: string;
}

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
  activeSkillTab = 'experience';
  skillProgress = [
    { name: 'Java', value: 85, colorClass: 'bg-primary' },                    // Xanh dương
    { name: 'Spring Boot', value: 80, colorClass: 'bg-success' },             // Xanh lá
    { name: 'Angular', value: 75, colorClass: 'bg-danger' },                  // Đỏ
    { name: 'MySQL', value: 80, colorClass: 'bg-warning text-dark' },         // Vàng (chữ đen)
    { name: 'AWS', value: 65, colorClass: 'bg-info text-dark' },              // Xanh nhạt (chữ đen)
    { name: 'Git', value: 80, colorClass: 'bg-secondary' },                   // Xám
    { name: 'Jira', value: 80, colorClass: 'bg-dark' },                       // Đen
    { name: 'Teamwork', value: 85, colorClass: 'bg-primary bg-gradient' }     // Xanh dương gradient
  ];

  // Experiences với đa ngôn ngữ
  experiences: Experience[] = [
    {
      title: 'Developer1',
      period: '05/2025 - 04/2025',
      technology: 'Java, Spring Boot, Angular, MySQL, AWS, jira, git, teamwork, postman, netbeans, vs code',
      aisupport: 'ChatGPT, codevista, Stack Overflow',
      description: {
        vi: 'Nâng cấp công nghệ cho hệ thống ngân hàng, cải thiện hiệu suất và bảo mật, đồng thời hỗ trợ triển khai trên AWS.',
        en: 'Upgrading technology for banking system, improving performance and security, while supporting AWS deployment.'
      },
      contribute: 'backend, frontend, database, testing, deployment',
      company: 'Core Technology Upgrade Project – Oursource Bank Japan (FPT Software)'
    },
    {
      title: 'Developer1',
      period: '05/2025 - 01/2026',
      technology: 'Java, Spring Boot, Angular, MySQL, AWS, jira, git, teamwork',
      aisupport: 'ChatGPT, codevista, Stack Overflow',
      description: {
        vi: 'Phát triển ứng dụng đặt hàng tại Nhật Bản, đảm bảo chất lượng và hiệu suất cao.',
        en: 'Developing ordering application in Japan, ensuring high quality and performance.'
      },
      contribute: 'backend, frontend, database, testing, deployment',
      company: 'Oursource Delivery Management Platform for Japan Market – FPT Software'
    },
    {
      title: 'Freelance Developer',
      period: '01/2023 - 05/2023',
      technology: 'JavaScript, React, Node.js, MongoDB, Git',
      aisupport: 'ChatGPT, codevista, Stack Overflow, deepseek',
      description: {
        vi: 'Phát triển hệ thống quản lý dân cư tại khu đô thị FPT City.',
        en: 'Developing resident management system at FPT City urban area.'
      },
      contribute: 'backend, frontend, database, testing, deployment',
      company: 'Upwork / Freelancer'
    },
    {
      title: 'My Project',
      period: '01/2026 - 05/2026',
      technology: 'nodejs, express, react, mongodb, git',
      aisupport: 'ChatGPT, codevista, Stack Overflow, deepseek',
      description: {
        vi: 'Phát triển hệ thống kết nối tình nguyện viên CareConnect.',
        en: 'Developing CareConnect volunteer connection system.'
      },
      contribute: 'backend, frontend, database, testing, deployment',
      company: 'FPT University / Team Project'
    }
  ];

  // Education với đa ngôn ngữ
  education: Education[] = [
    {
      title: {
        vi: 'Cử nhân Công nghệ Thông tin',
        en: 'Bachelor of Information Technology'
      },
      period: '2020 - 2026',
      institution: 'FPT University'
    },
    {
      title: {
        vi: 'Chứng chỉ Java Spring Boot',
        en: 'Java Spring Boot Certification'
      },
      period: '2022 - 2023',
      institution: 'CodeGym Academy'
    },
    {
      title: {
        vi: 'Chứng chỉ IELTS 4.5',
        en: 'IELTS 4.5 Certification'
      },
      period: '2020',
      institution: 'IELTS Fighter - Mr. Hoa'
    },
    {
      title: {
        vi: 'Chứng chỉ tiếng Nhật N4',
        en: 'N4 Japanese Certification'
      },
      period: '2025',
      institution: 'FPT University'
    }
  ];

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
  // Lấy description theo ngôn ngữ hiện tại
  getDescription(item: any): string {
    if (!item.description) return '';
    return typeof item.description === 'string'
      ? item.description
      : item.description[this.language];
  }

  // Lấy title cho education
  getEducationTitle(item: any): string {
    if (!item.title) return '';
    return typeof item.title === 'string'
      ? item.title
      : item.title[this.language];
  }

  // Format technology array
  getTechnologyList(technology: string): string[] {
    return technology?.split(',').map(t => t.trim()) || [];
  }

  // Format AI support list
  getAISupportList(aisupport: string): string[] {
    return aisupport?.split(',').map(ai => ai.trim()) || [];
  }

  // Format contribute list
  getContributeList(contribute: string): string[] {
    return contribute?.split(',').map(c => c.trim()) || [];
  }
  private showToast(message: string): void {
    this.toast = message;
    window.setTimeout(() => {
      this.toast = '';
    }, 2600);
  }
}
