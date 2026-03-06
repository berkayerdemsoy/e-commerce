import { Component, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/auth/auth.service';
import { LoginRequest } from '../../core/auth/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="login-page">
      <div class="login-card">
        <div class="login-header">
          <span class="material-symbols-outlined login-logo">warehouse</span>
          <h1>MicroMart</h1>
          <p>Depo Yönetim Sistemi</p>
        </div>

        @if (errorMsg()) {
          <div class="alert alert--danger">{{ errorMsg() }}</div>
        }

        <form (ngSubmit)="onLogin()" class="login-form">
          <div class="form-group">
            <label for="username">Kullanıcı Adı</label>
            <input
              id="username"
              type="text"
              class="form-input"
              [(ngModel)]="credentials.username"
              name="username"
              placeholder="Kullanıcı adınızı girin"
              required />
          </div>

          <div class="form-group">
            <label for="password">Şifre</label>
            <input
              id="password"
              type="password"
              class="form-input"
              [(ngModel)]="credentials.password"
              name="password"
              placeholder="Şifrenizi girin"
              required />
          </div>

          <button
            type="submit"
            class="btn btn-primary login-btn"
            [disabled]="loading()">
            @if (loading()) {
              <span class="material-symbols-outlined spin">progress_activity</span>
              Giriş yapılıyor...
            } @else {
              <span class="material-symbols-outlined">login</span>
              Giriş Yap
            }
          </button>
        </form>

        <div class="login-footer">
          <span>Hesabınız yok mu?</span>
          <a routerLink="/register">Hesap Oluştur</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .login-page {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, $gray-900 0%, $primary-dark 100%);
    }

    .login-card {
      width: 100%;
      max-width: 420px;
      background: #fff;
      border-radius: $border-radius-lg;
      padding: 2.5rem;
      box-shadow: $shadow-lg;
    }

    .login-header {
      text-align: center;
      margin-bottom: 2rem;

      .login-logo {
        font-size: 48px;
        color: $primary;
      }

      h1 {
        font-size: 1.75rem;
        font-weight: 700;
        color: $gray-900;
        margin-top: 0.5rem;
      }

      p {
        color: $gray-500;
        font-size: 0.875rem;
        margin-top: 0.25rem;
      }
    }

    .login-form {
      display: flex;
      flex-direction: column;
      gap: 1.25rem;
    }

    .login-btn {
      width: 100%;
      justify-content: center;
      padding: 0.75rem;
      font-size: 1rem;
      margin-top: 0.5rem;
    }

    .alert {
      padding: 0.75rem 1rem;
      border-radius: $border-radius;
      font-size: 0.875rem;
      margin-bottom: 1rem;

      &--danger {
        background: rgba($danger, 0.1);
        color: $danger;
        border: 1px solid rgba($danger, 0.2);
      }
    }

    .spin {
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      from { transform: rotate(0deg); }
      to { transform: rotate(360deg); }
    }

    .login-footer {
      text-align: center;
      margin-top: 1.5rem;
      font-size: 0.875rem;
      color: $gray-500;

      a {
        color: $primary;
        font-weight: 600;
        margin-left: 0.25rem;
        &:hover { text-decoration: underline; }
      }
    }
  `],
})
export class LoginComponent {
  credentials: LoginRequest = { username: '', password: '' };
  loading = signal(false);
  errorMsg = signal('');

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  onLogin(): void {
    if (!this.credentials.username || !this.credentials.password) {
      this.errorMsg.set('Lütfen tüm alanları doldurun.');
      return;
    }

    this.loading.set(true);
    this.errorMsg.set('');

    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMsg.set(
          err.status === 401
            ? 'Geçersiz kullanıcı adı veya şifre.'
            : 'Bağlantı hatası. Lütfen tekrar deneyin.',
        );
      },
    });
  }
}

