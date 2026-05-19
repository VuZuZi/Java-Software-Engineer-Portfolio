import { CommonModule } from '@angular/common';
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { profile, projects, services, skills } from '../../shared/portfolio-data';

type Language = 'vi' | 'en';

@Component({
  selector: 'app-landing',
  imports: [CommonModule, RouterLink],
  templateUrl: './landing.component.html',
})
export class LandingComponent implements OnInit, OnDestroy {
  readonly profile = profile;
  readonly services = services;
  readonly projects = projects;
  readonly skills = skills.map((skill) => skill.name);
  readonly sections = ['home', 'services', 'projects', 'skills', 'contact'];

  activeSection = 'home';
  isScrolled = false;
  language: Language = 'vi';
  typedText = '';

  private typedWords: Record<Language, string[]> = {
    vi: ['Đoàn Đình Vũ', 'Java Developer', 'Spring Boot Developer', 'Problem Solver'],
    en: ['Doan Dinh Vu', 'Java Developer', 'Spring Boot Developer', 'Problem Solver'],
  };
  private wordIndex = 0;
  private charIndex = 0;
  private deleting = false;
  private timer?: number;

  ngOnInit(): void {
    this.language = (localStorage.getItem('portfolioLanguage') as Language) || 'vi';
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
}
