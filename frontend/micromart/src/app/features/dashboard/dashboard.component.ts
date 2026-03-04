import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { WarehouseApiService } from '../../core/api/warehouse-api.service';
import { WarehouseSummaryDto } from '../../core/api/api.model';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="dashboard">
      <div class="dashboard__header">
        <h1>Hoş geldin, {{ authService.currentUser()?.username }}!</h1>
        <p>Depo yönetim sistemi genel özeti</p>
      </div>

      <!-- Warehouse Summary Cards -->
      @if (summaries().length === 0 && !loading()) {
        <div class="empty-state card">
          <span class="material-symbols-outlined">add_business</span>
          <h3>Henüz depo bulunmuyor</h3>
          <p>İlk deponuzu oluşturmak için Depolar sayfasını ziyaret edin.</p>
          <a routerLink="/warehouses" class="btn btn-primary">
            <span class="material-symbols-outlined">add</span>
            Depo Oluştur
          </a>
        </div>
      }

      @if (loading()) {
        <div class="loading-state">
          <span class="material-symbols-outlined spin">progress_activity</span>
          <p>Veriler yükleniyor...</p>
        </div>
      }

      <div class="summary-grid">
        @for (s of summaries(); track s.warehouseId) {
          <div class="card summary-card">
            <div class="summary-card__header">
              <div>
                <h3>{{ s.warehouseName }}</h3>
                <span class="summary-card__location">
                  <span class="material-symbols-outlined">location_on</span>
                  {{ s.location }}
                </span>
              </div>
              <a [routerLink]="['/warehouses', s.warehouseId]" class="btn btn-secondary">
                Detay
              </a>
            </div>

            <div class="summary-card__stats">
              <div class="stat">
                <span class="stat__value">{{ s.totalProducts }}</span>
                <span class="stat__label">Ürün</span>
              </div>
              <div class="stat">
                <span class="stat__value">{{ s.totalAisles }}</span>
                <span class="stat__label">Koridor</span>
              </div>
              <div class="stat">
                <span class="stat__value">{{ s.totalShelves }}</span>
                <span class="stat__label">Raf</span>
              </div>
              <div class="stat">
                <span class="stat__value">{{ s.totalStockQuantity }}</span>
                <span class="stat__label">Toplam Stok</span>
              </div>
            </div>

            @if (s.lowStockAlertCount > 0) {
              <div class="summary-card__alert">
                <span class="material-symbols-outlined">warning</span>
                {{ s.lowStockAlertCount }} üründe düşük stok uyarısı
              </div>
            }
          </div>
        }
      </div>
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .dashboard {
      &__header {
        margin-bottom: 2rem;
        h1 { font-size: 1.5rem; font-weight: 700; color: $gray-900; }
        p { color: $gray-500; margin-top: 0.25rem; }
      }
    }

    .summary-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
      gap: 1.5rem;
    }

    .summary-card {
      &__header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 1.25rem;

        h3 { font-size: 1.125rem; font-weight: 600; color: $gray-900; text-transform: capitalize; }
      }

      &__location {
        display: inline-flex;
        align-items: center;
        gap: 0.25rem;
        font-size: 0.8rem;
        color: $gray-500;
        margin-top: 0.25rem;

        .material-symbols-outlined { font-size: 16px; }
      }

      &__stats {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 1rem;
        padding: 1rem 0;
        border-top: 1px solid $gray-100;
      }

      &__alert {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        padding: 0.75rem 1rem;
        background: rgba($warning, 0.08);
        color: $warning;
        border-radius: $border-radius;
        font-size: 0.8125rem;
        font-weight: 500;
        margin-top: 0.75rem;

        .material-symbols-outlined { font-size: 20px; }
      }
    }

    .stat {
      text-align: center;
      &__value { display: block; font-size: 1.5rem; font-weight: 700; color: $gray-900; }
      &__label { font-size: 0.75rem; color: $gray-500; }
    }

    .empty-state {
      text-align: center;
      padding: 3rem;
      .material-symbols-outlined { font-size: 48px; color: $gray-400; }
      h3 { margin: 1rem 0 0.5rem; color: $gray-700; }
      p { color: $gray-500; margin-bottom: 1.5rem; }
    }

    .loading-state {
      text-align: center;
      padding: 3rem;
      color: $gray-500;
      .spin { font-size: 36px; animation: spin 1s linear infinite; }
      p { margin-top: 0.75rem; }
    }

    @keyframes spin {
      from { transform: rotate(0deg); }
      to { transform: rotate(360deg); }
    }
  `],
})
export class DashboardComponent implements OnInit {
  summaries = signal<WarehouseSummaryDto[]>([]);
  loading = signal(true);

  constructor(
    public authService: AuthService,
    private warehouseApi: WarehouseApiService,
  ) {}

  ngOnInit(): void {
    this.warehouseApi.getAll(0, 100).subscribe({
      next: (page) => {
        const warehouses = page.content;
        if (warehouses.length === 0) {
          this.loading.set(false);
          return;
        }

        let loaded = 0;
        const results: WarehouseSummaryDto[] = [];

        warehouses.forEach((w) => {
          this.warehouseApi.getSummary(w.id!).subscribe({
            next: (summary) => {
              results.push(summary);
              loaded++;
              if (loaded === warehouses.length) {
                this.summaries.set(results);
                this.loading.set(false);
              }
            },
            error: () => {
              loaded++;
              if (loaded === warehouses.length) {
                this.summaries.set(results);
                this.loading.set(false);
              }
            },
          });
        });
      },
      error: () => this.loading.set(false),
    });
  }
}


