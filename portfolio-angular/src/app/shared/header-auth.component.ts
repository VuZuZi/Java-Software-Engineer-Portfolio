import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { LanguageService } from '../core/language.service';

@Component({
    selector: 'app-header-auth',
    standalone: true,
    imports: [CommonModule, RouterLink],
    template: `
    <div class="header-auth">
      <button type="button" class="btn btn-outline-primary language-toggle" (click)="toggleLanguage()">
        <i class="fas fa-globe"></i>
        <span>{{ (language$ | async) === 'vi' ? 'EN' : 'VI' }}</span>
      </button>

      <ng-container *ngIf="authenticated$ | async; else loginLink">
        <div class="d-flex align-items-center user-header text-white">
          <img [src]="userAvatar$ | async" alt="Avatar" class="rounded-circle me-2" width="34" height="34">
          <span>{{ userName$ | async }}</span>
        </div>
      </ng-container>

      <ng-template #loginLink>
        <a routerLink="/login" class="btn btn-danger">
          <i class="fab fa-google me-1"></i>
        </a>
      </ng-template>
    </div>
  `,
})
export class HeaderAuthComponent {
    constructor(
        private readonly authService: AuthService,
        private readonly languageService: LanguageService,
    ) { }

    get language$() {
        return this.languageService.language$;
    }

    get authenticated$() {
        return this.authService.authenticated$;
    }

    get userName$() {
        return this.authService.userName$;
    }

    get userAvatar$() {
        return this.authService.userAvatar$;
    }

    toggleLanguage(): void {
        this.languageService.toggleLanguage();
    }
}
