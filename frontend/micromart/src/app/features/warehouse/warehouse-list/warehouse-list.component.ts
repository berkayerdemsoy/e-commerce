import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { WarehouseApiService } from '../../../core/api/warehouse-api.service';
import { WarehouseDto } from '../../../core/api/api.model';

@Component({
  selector: 'app-warehouse-list',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="page">
      <div class="page__header">
        <div>
          <h1>Depolar</h1>
          <p>Tüm depoları görüntüleyin ve yönetin</p>
        </div>
        <button class="btn btn-primary" routerLink="/warehouses/new">
          <span class="material-symbols-outlined">add</span>
          Yeni Depo
        </button>
      </div>

      @if (loading()) {
        <div class="loading-state">
          <span class="material-symbols-outlined spin">progress_activity</span>
        </div>
      }

      <div class="grid">
        @for (w of warehouses(); track w.id) {
          <div class="card warehouse-card">
            <div class="warehouse-card__top">
              <div class="warehouse-card__icon">
                <span class="material-symbols-outlined">warehouse</span>
              </div>
              <div class="warehouse-card__info">
                <h3>{{ w.name }}</h3>
                <span class="warehouse-card__location">
                  <span class="material-symbols-outlined">location_on</span>
                  {{ w.location }}
                </span>
              </div>
            </div>
            <div class="warehouse-card__actions">
              <a [routerLink]="['/warehouses', w.id]" class="btn btn-secondary">
                <span class="material-symbols-outlined">visibility</span>
                Detay
              </a>
              <a [routerLink]="['/warehouses', w.id, 'edit']" class="btn btn-outline">
                <span class="material-symbols-outlined">edit</span>
                Düzenle
              </a>
              <button class="btn btn-danger" (click)="onDelete(w)">
                <span class="material-symbols-outlined">delete</span>
              </button>
            </div>
          </div>
        }
      </div>

      @if (!loading() && warehouses().length === 0) {
        <div class="empty-state card">
          <span class="material-symbols-outlined">warehouse</span>
          <h3>Henüz depo eklenmemiş</h3>
          <p>İlk deponuzu oluşturarak başlayın.</p>
        </div>
      }

      <!-- Pagination -->
      @if (totalPages() > 1) {
        <div class="pagination">
          <button class="btn btn-secondary" [disabled]="currentPage() === 0"
                  (click)="loadPage(currentPage() - 1)">
            <span class="material-symbols-outlined">chevron_left</span>
          </button>
          <span class="pagination__info">
            {{ currentPage() + 1 }} / {{ totalPages() }}
          </span>
          <button class="btn btn-secondary" [disabled]="currentPage() >= totalPages() - 1"
                  (click)="loadPage(currentPage() + 1)">
            <span class="material-symbols-outlined">chevron_right</span>
          </button>
        </div>
      }
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .page {
      &__header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 1.5rem;
        h1 { font-size: 1.5rem; font-weight: 700; color: $gray-900; }
        p { color: $gray-500; margin-top: 0.25rem; font-size: 0.875rem; }
      }
    }

    .grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
      gap: 1rem;
    }

    .warehouse-card {
      &__top {
        display: flex;
        gap: 1rem;
        margin-bottom: 1rem;
      }

      &__icon {
        width: 48px; height: 48px;
        border-radius: $border-radius;
        background: rgba($primary, 0.1);
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;
        .material-symbols-outlined { color: $primary; font-size: 24px; }
      }

      &__info {
        h3 { font-size: 1rem; font-weight: 600; color: $gray-900; text-transform: capitalize; }
      }

      &__location {
        display: inline-flex;
        align-items: center;
        gap: 0.25rem;
        font-size: 0.8rem;
        color: $gray-500;
        margin-top: 0.25rem;
        .material-symbols-outlined { font-size: 14px; }
      }

      &__actions {
        display: flex;
        gap: 0.5rem;
        border-top: 1px solid $gray-100;
        padding-top: 1rem;
      }
    }

    .pagination {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 1rem;
      margin-top: 2rem;
      &__info { font-size: 0.875rem; color: $gray-600; }
    }

    .empty-state {
      text-align: center;
      padding: 3rem;
      .material-symbols-outlined { font-size: 48px; color: $gray-400; }
      h3 { margin: 1rem 0 0.5rem; color: $gray-700; }
      p { color: $gray-500; }
    }

    .loading-state { text-align: center; padding: 3rem; }
    .spin { animation: spin 1s linear infinite; font-size: 36px; color: $gray-400; }
    @keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
  `],
})
export class WarehouseListComponent implements OnInit {
  warehouses = signal<WarehouseDto[]>([]);
  loading = signal(true);
  currentPage = signal(0);
  totalPages = signal(0);

  constructor(private api: WarehouseApiService) {}

  ngOnInit(): void {
    this.loadPage(0);
  }

  loadPage(page: number): void {
    this.loading.set(true);
    this.api.getAll(page, 12).subscribe({
      next: (res) => {
        this.warehouses.set(res.content);
        this.currentPage.set(res.number);
        this.totalPages.set(res.totalPages);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onDelete(w: WarehouseDto): void {
    if (confirm(`"${w.name}" deposu silinecek. Emin misiniz?`)) {
      this.api.delete(w.id!).subscribe({
        next: () => this.loadPage(this.currentPage()),
      });
    }
  }
}


