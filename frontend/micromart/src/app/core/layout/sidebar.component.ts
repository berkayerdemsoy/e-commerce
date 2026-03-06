import { Component, input, output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../auth/auth.service';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  roles?: string[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <aside class="sidebar" [class.collapsed]="collapsed()">
      <!-- Logo -->
      <div class="sidebar__logo">
        <span class="material-symbols-outlined logo-icon">warehouse</span>
        @if (!collapsed()) {
          <span class="logo-text">MicroMart</span>
        }
      </div>

      <!-- Navigation -->
      <nav class="sidebar__nav">
        @for (item of visibleItems; track item.route) {
          <a class="nav-item"
             [routerLink]="item.route"
             routerLinkActive="nav-item--active"
             [title]="item.label">
            <span class="material-symbols-outlined">{{ item.icon }}</span>
            @if (!collapsed()) {
              <span class="nav-label">{{ item.label }}</span>
            }
          </a>
        }
      </nav>

      <!-- Collapse toggle -->
      <button class="sidebar__toggle" (click)="toggle.emit()">
        <span class="material-symbols-outlined">
          {{ collapsed() ? 'chevron_right' : 'chevron_left' }}
        </span>
      </button>
    </aside>
  `,
  styles: [`
    @use 'variables' as *;

    .sidebar {
      position: fixed;
      top: 0;
      left: 0;
      bottom: 0;
      width: $sidebar-width;
      background: var(--mm-sidebar-bg, #111827);
      color: var(--mm-sidebar-text, #d1d5db);
      display: flex;
      flex-direction: column;
      z-index: 100;
      transition: width $transition-base, background-color $transition-base;
      overflow: hidden;

      &.collapsed {
        width: $sidebar-collapsed-width;
      }

      &__logo {
        display: flex;
        align-items: center;
        gap: 0.75rem;
        padding: 1.25rem 1rem;
        border-bottom: 1px solid var(--mm-sidebar-border, #374151);
        min-height: $header-height;

        .logo-icon {
          font-size: 28px;
          color: $primary-light;
        }

        .logo-text {
          font-size: 1.25rem;
          font-weight: 700;
          color: #fff;
          white-space: nowrap;
        }
      }

      &__nav {
        flex: 1;
        padding: 0.75rem 0.5rem;
        display: flex;
        flex-direction: column;
        gap: 0.25rem;
        overflow-y: auto;
      }

      &__toggle {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0.75rem;
        border: none;
        border-top: 1px solid var(--mm-sidebar-border, #374151);
        background: transparent;
        color: $gray-400;
        cursor: pointer;
        transition: color $transition-fast;

        &:hover { color: #fff; }
      }
    }

    .nav-item {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 0.625rem 0.75rem;
      border-radius: $border-radius;
      color: $gray-400;
      font-size: 0.875rem;
      font-weight: 500;
      transition: all $transition-fast;
      white-space: nowrap;
      text-decoration: none;

      &:hover {
        background: rgba(255, 255, 255, 0.08);
        color: #fff;
      }

      &--active {
        background: rgba($primary, 0.15);
        color: $primary-light;

        .material-symbols-outlined {
          color: $primary-light;
        }
      }

      .material-symbols-outlined {
        font-size: 22px;
        flex-shrink: 0;
      }
    }
  `],
})
export class SidebarComponent {
  collapsed = input(false);
  toggle = output<void>();

  private allItems: NavItem[] = [
    { label: 'Dashboard',        icon: 'dashboard',         route: '/dashboard' },
    { label: 'Depolar',          icon: 'warehouse',         route: '/warehouses' },
    { label: 'Envanter',         icon: 'inventory_2',       route: '/inventory' },
    { label: 'Ürünler',          icon: 'shopping_bag',      route: '/products' },
    { label: 'Kategoriler',      icon: 'label',             route: '/categories' },
    { label: 'Siparişler',       icon: 'shopping_cart',     route: '/orders' },
    { label: 'Kullanıcılar',     icon: 'group',             route: '/users',
      roles: ['ROLE_ADMIN'] },
  ];

  constructor(private authService: AuthService) {}

  get visibleItems(): NavItem[] {
    return this.allItems.filter((item) => {
      if (!item.roles || item.roles.length === 0) return true;
      return this.authService.hasAnyRole(...item.roles);
    });
  }
}

