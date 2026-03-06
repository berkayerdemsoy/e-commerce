import { Component, input, output } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { ThemeService } from '../theme/theme.service';

@Component({
  selector: 'app-header',
  standalone: true,
  template: `
    <header class="header" [class.sidebar-collapsed]="sidebarCollapsed()">
      <div class="header__left">
        <button class="header__menu-btn" (click)="toggleSidebar.emit()">
          <span class="material-symbols-outlined">menu</span>
        </button>
        <h2 class="header__title">Depo Yönetim Sistemi</h2>
      </div>

      <div class="header__right">
        <!-- Dark mode toggle -->
        <button class="header__theme-btn" (click)="themeService.toggle()"
                [title]="themeService.isDark() ? 'Açık Tema' : 'Koyu Tema'">
          <span class="material-symbols-outlined">
            {{ themeService.isDark() ? 'light_mode' : 'dark_mode' }}
          </span>
        </button>

        @if (authService.currentUser(); as user) {
          <div class="header__user">
            <div class="header__avatar">
              {{ user.username.charAt(0).toUpperCase() }}
            </div>
            <div class="header__user-info">
              <span class="header__username">{{ user.username }}</span>
              <span class="header__role">{{ primaryRole }}</span>
            </div>
          </div>
          <button class="btn btn-secondary" (click)="authService.logout()">
            <span class="material-symbols-outlined">logout</span>
            Çıkış
          </button>
        }
      </div>
    </header>
  `,
  styles: [`
    @use 'variables' as *;

    .header {
      position: fixed;
      top: 0;
      right: 0;
      left: $sidebar-width;
      height: $header-height;
      background: var(--mm-header-bg, #fff);
      border-bottom: 1px solid var(--mm-header-border, #e5e7eb);
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 1.5rem;
      z-index: 90;
      transition: left $transition-base, background-color $transition-base, border-color $transition-base;

      &.sidebar-collapsed {
        left: $sidebar-collapsed-width;
      }

      &__left {
        display: flex;
        align-items: center;
        gap: 1rem;
      }

      &__menu-btn {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        border: none;
        border-radius: $border-radius;
        background: transparent;
        color: $text-muted;
        cursor: pointer;
        transition: background $transition-fast;
        &:hover { background: $gray-100; }
      }

      &__title {
        font-size: 1.125rem;
        font-weight: 600;
        color: $text-primary;
      }

      &__right {
        display: flex;
        align-items: center;
        gap: 1rem;
      }

      &__theme-btn {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        border: 1px solid $surface-border;
        border-radius: $border-radius;
        background: $surface-card;
        color: $text-muted;
        cursor: pointer;
        transition: all $transition-fast;

        &:hover {
          background: $gray-100;
          color: $text-primary;
          border-color: $gray-400;
        }

        .material-symbols-outlined {
          font-size: 20px;
        }
      }

      &__user {
        display: flex;
        align-items: center;
        gap: 0.75rem;
      }

      &__avatar {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background: $primary;
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 600;
        font-size: 0.875rem;
      }

      &__user-info {
        display: flex;
        flex-direction: column;
        line-height: 1.3;
      }

      &__username {
        font-size: 0.875rem;
        font-weight: 600;
        color: $text-primary;
      }

      &__role {
        font-size: 0.75rem;
        color: $text-muted;
      }
    }
  `],
})
export class HeaderComponent {
  sidebarCollapsed = input(false);
  toggleSidebar = output<void>();

  constructor(
    public authService: AuthService,
    public themeService: ThemeService,
  ) {}

  get primaryRole(): string {
    const roles = this.authService.roles();
    if (roles.includes('ROLE_ADMIN')) return 'Admin';
    if (roles.includes('ROLE_WAREHOUSE_ADMIN')) return 'Depo Admin';
    if (roles.includes('ROLE_WAREHOUSE_MANAGER')) return 'Depo Yöneticisi';
    return 'Kullanıcı';
  }
}

