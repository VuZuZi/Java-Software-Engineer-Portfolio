import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { LanguageService } from '../core/language.service';
import { map } from 'rxjs';

@Component({
  selector: 'app-header-auth',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="header-auth d-flex align-items-center gap-2">
      <!-- Language Toggle Button -->
      <button type="button" class="btn btn-outline-primary language-toggle" (click)="toggleLanguage()">
        <i class="fas fa-globe"></i>
        <span>{{ (language$ | async) === 'vi' ? 'EN' : 'VI' }}</span>
      </button>

      <!-- User Dropdown when authenticated -->
      <ng-container *ngIf="authenticated$ | async; else loginLink">
        <div class="dropdown">
          <button class="btn btn-link dropdown-toggle user-dropdown-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
            <img [src]="userAvatar$ | async" alt="Avatar" class="rounded-circle me-2" width="34" height="34">
            <span class="user-name">{{ userName$ | async }}</span>
          </button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li class="dropdown-header">
              <div class="user-info-dropdown">
                <strong>{{ userName$ | async }}</strong>
                <small class="d-block text-muted">{{ userEmail }}</small>
              </div>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li>
              <a class="dropdown-item" routerLink="/profile">
                <i class="fas fa-user-circle me-2"></i>{{ (language$ | async) === 'vi' ? 'Hồ sơ' : 'Profile' }}
              </a>
            </li>
            <li>
              <a class="dropdown-item" routerLink="/settings">
                <i class="fas fa-cog me-2"></i>{{ (language$ | async) === 'vi' ? 'Cài đặt' : 'Settings' }}
              </a>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li>
              <button class="dropdown-item text-danger" (click)="logout()">
                <i class="fas fa-sign-out-alt me-2"></i>{{ (language$ | async) === 'vi' ? 'Đăng xuất' : 'Sign Out' }}
              </button>
            </li>
          </ul>
        </div>
      </ng-container>

      <!-- Login Link for unauthenticated users -->
      <ng-template #loginLink>
        <a routerLink="/login" class="btn btn-danger">
          <i class="fab fa-google me-1"></i>
          <span class="d-none d-sm-inline">{{ (language$ | async) === 'vi' ? 'Đăng nhập' : 'Login' }}</span>
        </a>
      </ng-template>
    </div>
  `,
  styles: [`
    .header-auth {
      display: flex;
      align-items: center;
      gap: 0.75rem;
    }

    .language-toggle {
      border-radius: 50px;
      padding: 0.375rem 1rem;
      font-weight: 500;
      transition: all 0.3s ease;
    }

    .language-toggle:hover {
      transform: translateY(-2px);
    }

    .user-dropdown-btn {
      text-decoration: none;
      color: #333;
      display: flex;
      align-items: center;
      padding: 0.25rem 0.5rem;
      border-radius: 50px;
      transition: all 0.3s ease;
    }

    .user-dropdown-btn:hover {
      background-color: rgba(13, 110, 253, 0.1);
      transform: translateY(-2px);
    }

    .user-name {
      font-weight: 500;
      color: #333;
      max-width: 120px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .dropdown-header {
      padding: 0.75rem 1rem;
    }

    .user-info-dropdown {
      min-width: 180px;
    }

    .user-info-dropdown strong {
      display: block;
      font-size: 0.9rem;
      color: #333;
    }

    .user-info-dropdown small {
      font-size: 0.75rem;
      word-break: break-all;
    }

    .dropdown-item {
      cursor: pointer;
      transition: all 0.2s ease;
    }

    .dropdown-item:hover {
      background-color: #f8f9fa;
      padding-left: 1.5rem;
    }

    .dropdown-item.text-danger:hover {
      background-color: #fee2e2;
    }

    @media (max-width: 576px) {
      .user-name {
        display: none;
      }
      
      .language-toggle span {
        display: none;
      }
      
      .language-toggle i {
        margin-right: 0;
      }
      
      .language-toggle {
        padding: 0.375rem 0.75rem;
      }
    }
  `]
})
export class HeaderAuthComponent {
  // Lưu email từ localStorage
  userEmail: string = '';

  constructor(
    private readonly authService: AuthService,
    private readonly languageService: LanguageService,
  ) {
    // Lấy email từ localStorage khi component khởi tạo
    this.loadUserEmail();

    // Theo dõi thay đổi authentication state
    this.authenticated$.subscribe(authenticated => {
      if (authenticated) {
        this.loadUserEmail();
      } else {
        this.userEmail = '';
      }
    });
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

  logout(): void {
    // Xóa localStorage
    localStorage.removeItem('user');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName');
    localStorage.removeItem('userAvatar');

    // Cập nhật userEmail
    this.userEmail = '';

    // Refresh auth service state
    this.authService.refresh();

    // Tải lại trang để cập nhật UI
    window.location.reload();
  }

  toggleLanguage(): void {
    this.languageService.toggleLanguage();
  }
}