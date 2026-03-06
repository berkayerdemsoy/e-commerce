import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './sidebar.component';
import { HeaderComponent } from './header.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, HeaderComponent],
  template: `
    <div class="layout" [class.sidebar-collapsed]="sidebarCollapsed()">
      <app-sidebar
        [collapsed]="sidebarCollapsed()"
        (toggle)="sidebarCollapsed.set(!sidebarCollapsed())" />

      <div class="layout__main">
        <app-header
          [sidebarCollapsed]="sidebarCollapsed()"
          (toggleSidebar)="sidebarCollapsed.set(!sidebarCollapsed())" />

        <main class="layout__content">
          <router-outlet />
        </main>
      </div>
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .layout {
      display: flex;
      min-height: 100vh;

      &__main {
        flex: 1;
        margin-left: $sidebar-width;
        transition: margin-left $transition-base;
        display: flex;
        flex-direction: column;
      }

      &.sidebar-collapsed .layout__main {
        margin-left: $sidebar-collapsed-width;
      }

      &__content {
        flex: 1;
        padding: 1.5rem;
        margin-top: $header-height;
      }
    }
  `],
})
export class LayoutComponent {
  sidebarCollapsed = signal(false);
}

