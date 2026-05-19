import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

type Language = 'vi' | 'en';

@Injectable({ providedIn: 'root' })
export class LanguageService {
    private readonly storageKey = 'portfolioLanguage';
    private readonly languageSubject = new BehaviorSubject<Language>(this.loadLanguage());

    readonly language$ = this.languageSubject.asObservable();

    get current(): Language {
        return this.languageSubject.value;
    }

    toggleLanguage(): void {
        this.setLanguage(this.current === 'vi' ? 'en' : 'vi');
    }

    setLanguage(language: Language): void {
        localStorage.setItem(this.storageKey, language);
        this.languageSubject.next(language);
    }

    private loadLanguage(): Language {
        return (localStorage.getItem(this.storageKey) as Language) || 'vi';
    }
}
