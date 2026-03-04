import { Component, OnInit, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Page } from '../../core/api/api.model';
import { UserResponse } from '../../core/auth/auth.model';

@Component({
  selector: 'app-user-list',
  standalone: true,
  template: `
    <div class="page">
      <div class="page__header">
        <div>
          <h1>Kullanıcılar</h1>
          <p>Sistem kullanıcılarını yönetin</p>
        </div>
      </div>

      <div class="card">
        <div class="table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Kullanıcı Adı</th>
                <th>E-posta</th>
                <th>Roller</th>
                <th>İşlemler</th>
              </tr>
            </thead>
            <tbody>
              @for (u of users(); track u.id) {
                <tr>
                  <td>{{ u.id }}</td>
                  <td>
                    <div class="user-cell">
                      <div class="user-cell__avatar">{{ u.username.charAt(0).toUpperCase() }}</div>
                      <strong>{{ u.username }}</strong>
                    </div>
                  </td>
                  <td>{{ u.email }}</td>
                  <td>
                    <div class="role-list">
                      @for (r of u.roles; track r) {
                        <span class="badge badge--info">{{ r }}</span>
                      }
                    </div>
                  </td>
                  <td>
                    <button class="btn btn-danger btn-sm" (click)="onDelete(u)">
                      <span class="material-symbols-outlined">delete</span>
                    </button>
                  </td>
                </tr>
              } @empty {
                <tr><td colspan="5" class="empty-cell">Kullanıcı bulunamadı.</td></tr>
              }
            </tbody>
          </table>
        </div>

        @if (totalPages() > 1) {
          <div class="pagination">
            <button class="btn btn-secondary" [disabled]="currentPage() === 0" (click)="loadPage(currentPage() - 1)">
              <span class="material-symbols-outlined">chevron_left</span>
            </button>
            <span class="pagination__info">{{ currentPage() + 1 }} / {{ totalPages() }}</span>
            <button class="btn btn-secondary" [disabled]="currentPage() >= totalPages() - 1" (click)="loadPage(currentPage() + 1)">
              <span class="material-symbols-outlined">chevron_right</span>
            </button>
          </div>
        }
      </div>
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .page__header {
      margin-bottom: 1.5rem;
      h1 { font-size: 1.5rem; font-weight: 700; color: $gray-900; }
      p { color: $gray-500; margin-top: 0.25rem; font-size: 0.875rem; }
    }

    .table-wrapper { overflow-x: auto; }

    .data-table {
      width: 100%; border-collapse: collapse;
      th, td { padding: 0.75rem 1rem; text-align: left; font-size: 0.875rem; }
      th { color: $gray-500; font-weight: 600; border-bottom: 2px solid $gray-200; }
      td { border-bottom: 1px solid $gray-100; color: $gray-700; }
      tbody tr:hover { background: $gray-50; }
    }

    .user-cell {
      display: flex; align-items: center; gap: 0.75rem;
      &__avatar {
        width: 32px; height: 32px; border-radius: 50%;
        background: $primary; color: #fff;
        display: flex; align-items: center; justify-content: center;
        font-size: 0.8rem; font-weight: 600;
      }
    }

    .role-list { display: flex; flex-wrap: wrap; gap: 0.375rem; }
    .empty-cell { text-align: center; color: $gray-400; padding: 2rem !important; }
    .btn-sm { padding: 0.375rem 0.5rem; .material-symbols-outlined { font-size: 18px; } }

    .pagination {
      display: flex; align-items: center; justify-content: center; gap: 1rem; margin-top: 1.5rem;
      &__info { font-size: 0.875rem; color: $gray-600; }
    }
  `],
})
export class UserListComponent implements OnInit {
  users = signal<UserResponse[]>([]);
  currentPage = signal(0);
  totalPages = signal(0);

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadPage(0);
  }

  loadPage(page: number): void {
    const params = new HttpParams().set('page', page).set('size', 15);
    this.http.get<Page<UserResponse>>('/api/users', { params }).subscribe({
      next: (res) => {
        this.users.set(res.content);
        this.currentPage.set(res.number);
        this.totalPages.set(res.totalPages);
      },
    });
  }

  onDelete(u: UserResponse): void {
    if (confirm(`"${u.username}" kullanıcısı silinecek. Emin misiniz?`)) {
      this.http.delete(`/api/users/${u.id}`).subscribe({
        next: () => this.loadPage(this.currentPage()),
      });
    }
  }
}



