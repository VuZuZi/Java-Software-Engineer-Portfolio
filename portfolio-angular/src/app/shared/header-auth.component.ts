import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { LanguageService } from '../core/language.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-header-auth',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="header-auth d-flex align-items-center gap-2">
      <!-- Language Toggle Button -->
      <button type="button" class="btn btn-outline-primary rounded-pill" (click)="toggleLanguage()">
        <i class="fas fa-globe me-1"></i>
        <span>{{ (language$ | async) === 'vi' ? 'EN' : 'VI' }}</span>
      </button>

      <!-- User Dropdown when authenticated -->
      <ng-container *ngIf="authenticated$ | async; else loginLink">
        <div class="dropdown">
          <button class="btn btn-link text-decoration-none dropdown-toggle d-flex align-items-center gap-2" 
                  type="button" 
                  data-bs-toggle="dropdown" 
                  aria-expanded="false">
            <img [src]="userAvatar$ | async" alt="Avatar" class="rounded-circle" width="34" height="34">
            <span class="fw-semibold text-dark">{{ userName$ | async }}</span>
          </button>
          <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2">
            <li class="dropdown-header">
              <div class="py-2">
                <strong class="d-block">{{ userName$ | async }}</strong>
                <small class="text-muted">{{ userEmail }}</small>
              </div>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li>
              <a class="dropdown-item" routerLink="/profile">
                <i class="fas fa-user-circle me-2 text-primary"></i>{{ (language$ | async) === 'vi' ? 'Hồ sơ' : 'Profile' }}
              </a>
            </li>
            <li>
              <a class="dropdown-item" routerLink="/settings">
                <i class="fas fa-cog me-2 text-secondary"></i>{{ (language$ | async) === 'vi' ? 'Cài đặt' : 'Settings' }}
              </a>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li>
              <button class="dropdown-item text-danger" (click)="logout()" [disabled]="isLoggingOut">
                <i class="fas fa-spinner fa-spin me-2" *ngIf="isLoggingOut"></i>
                <i class="fas fa-sign-out-alt me-2" *ngIf="!isLoggingOut"></i>
                {{ (language$ | async) === 'vi' ? 'Đăng xuất' : 'Sign Out' }}
              </button>
            </li>
          </ul>
        </div>
      </ng-container>

      <!-- Login Link for unauthenticated users -->
      <ng-template #loginLink>
        <a routerLink="/login" class="btn btn-danger rounded-pill">
          <i class="fab fa-google me-2"></i>
          <span>{{ (language$ | async) === 'vi' ? 'Đăng nhập' : 'Login' }}</span>
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

    .btn-outline-primary {
      transition: all 0.3s ease;
    }

    .btn-outline-primary:hover {
      transform: translateY(-2px);
    }

    .dropdown-toggle::after {
      margin-left: 0.5rem;
    }

    .dropdown-menu {
      border-radius: 0.75rem;
      animation: fadeInDown 0.2s ease;
    }

    @keyframes fadeInDown {
      from {
        opacity: 0;
        transform: translateY(-10px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .dropdown-item {
      padding: 0.5rem 1rem;
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

    .dropdown-item:active {
      background-color: #e9ecef;
    }

    .btn-danger {
      background: linear-gradient(135deg, #ea4335, #c5221f);
      border: none;
      transition: all 0.3s ease;
    }

    .btn-danger:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(234, 67, 53, 0.3);
    }

    @media (max-width: 576px) {
      .dropdown-toggle span {
        display: none;
      }
      
      .btn-outline-primary span {
        display: none;
      }
      
      .btn-outline-primary i {
        margin-right: 0;
      }
      
      .btn-danger span {
        display: none;
      }
      
      .btn-danger i {
        margin-right: 0;
      }
    }
  `]
})
export class HeaderAuthComponent {
  userEmail: string = '';
  isLoggingOut: boolean = false;

  constructor(
    private readonly authService: AuthService,
    private readonly languageService: LanguageService,
    private readonly http: HttpClient
  ) {
    this.loadUserEmail();
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
    this.isLoggingOut = true;

    // Gọi API logout
    this.http.post('/api/logout', {}, { withCredentials: true }).subscribe({
      next: () => {
        this.clearLocalData();
      },
      error: (error) => {
        console.error('Logout error:', error);
        // Vẫn xóa local data kể cả API fail
        this.clearLocalData();
      }
    });
  }

  private clearLocalData(): void {
    // Xóa localStorage
    localStorage.removeItem('user');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName');
    localStorage.removeItem('userAvatar');
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');

    // Cập nhật userEmail
    this.userEmail = '';

    // Refresh auth service state
    this.authService.refresh();

    this.isLoggingOut = false;
    this.menuOpen = false;

    // Tải lại trang để cập nhật UI
    window.location.href = '/';
  }

  menuOpen = false;

  toggleLanguage(): void {
    this.languageService.toggleLanguage();
  }
}