import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { WarehouseApiService } from '../../../core/api/warehouse-api.service';
import { StockApiService } from '../../../core/api/stock-api.service';
import {
  WarehouseSummaryDto,
  WarehouseCategoryDto,
  ProductShelfResponse,
} from '../../../core/api/api.model';

@Component({
  selector: 'app-warehouse-detail',
  standalone: true,
  imports: [RouterLink],
  template: `
    @if (summary(); as s) {
      <div class="detail">
        <!-- Breadcrumb -->
        <div class="breadcrumb">
          <a routerLink="/warehouses">Depolar</a>
          <span class="material-symbols-outlined">chevron_right</span>
          <span>{{ s.warehouseName }}</span>
        </div>

        <!-- Header -->
        <div class="detail__header">
          <div>
            <h1>{{ s.warehouseName }}</h1>
            <span class="detail__location">
              <span class="material-symbols-outlined">location_on</span>
              {{ s.location }}
            </span>
          </div>
          <a [routerLink]="['/warehouses', s.warehouseId, 'edit']" class="btn btn-outline">
            <span class="material-symbols-outlined">edit</span>
            Düzenle
          </a>
        </div>

        <!-- Stats -->
        <div class="stats-row">
          <div class="card stat-card">
            <span class="material-symbols-outlined stat-icon stat-icon--primary">inventory_2</span>
            <div class="stat-card__body">
              <span class="stat-card__value">{{ s.totalProducts }}</span>
              <span class="stat-card__label">Toplam Ürün</span>
            </div>
          </div>
          <div class="card stat-card">
            <span class="material-symbols-outlined stat-icon stat-icon--info">view_column</span>
            <div class="stat-card__body">
              <span class="stat-card__value">{{ s.totalAisles }}</span>
              <span class="stat-card__label">Koridor</span>
            </div>
          </div>
          <div class="card stat-card">
            <span class="material-symbols-outlined stat-icon stat-icon--accent">shelves</span>
            <div class="stat-card__body">
              <span class="stat-card__value">{{ s.totalShelves }}</span>
              <span class="stat-card__label">Raf</span>
            </div>
          </div>
          <div class="card stat-card">
            <span class="material-symbols-outlined stat-icon stat-icon--success">package_2</span>
            <div class="stat-card__body">
              <span class="stat-card__value">{{ s.totalStockQuantity }}</span>
              <span class="stat-card__label">Toplam Stok</span>
            </div>
          </div>
          @if (s.lowStockAlertCount > 0) {
            <div class="card stat-card stat-card--warning">
              <span class="material-symbols-outlined stat-icon stat-icon--warning">warning</span>
              <div class="stat-card__body">
                <span class="stat-card__value">{{ s.lowStockAlertCount }}</span>
                <span class="stat-card__label">Düşük Stok</span>
              </div>
            </div>
          }
        </div>

        <!-- Categories -->
        <div class="card section">
          <h2 class="section__title">
            <span class="material-symbols-outlined">category</span>
            Kategoriler
          </h2>
          @if (categories().length > 0) {
            <div class="tag-list">
              @for (c of categories(); track c.id) {
                <span class="badge badge--info">Kategori #{{ c.categoryId }}</span>
              }
            </div>
          } @else {
            <p class="section__empty">Bu depoya henüz kategori atanmamış.</p>
          }
        </div>

        <!-- Product Shelves -->
        <div class="card section">
          <div class="section__header">
            <h2 class="section__title">
              <span class="material-symbols-outlined">shelves</span>
              Raf-Ürün Stokları
            </h2>
            <a routerLink="/inventory" class="btn btn-secondary">
              <span class="material-symbols-outlined">open_in_new</span>
              Envanter Yönetimi
            </a>
          </div>
          @if (productShelves().length > 0) {
            <div class="table-wrapper">
              <table class="data-table">
                <thead>
                  <tr>
                    <th>Ürün ID</th>
                    <th>Raf ID</th>
                    <th>Miktar</th>
                    <th>Min. Stok</th>
                    <th>Durum</th>
                  </tr>
                </thead>
                <tbody>
                  @for (ps of productShelves(); track ps.productId + '-' + ps.shelfId) {
                    <tr>
                      <td>{{ ps.productId }}</td>
                      <td>{{ ps.shelfId }}</td>
                      <td>{{ ps.quantity }}</td>
                      <td>{{ ps.minStockLevel }}</td>
                      <td>
                        @if (ps.quantity < ps.minStockLevel) {
                          <span class="badge badge--danger">Düşük</span>
                        } @else {
                          <span class="badge badge--success">Normal</span>
                        }
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          } @else {
            <p class="section__empty">Bu depoda henüz ürün-raf ataması yok.</p>
          }
        </div>
      </div>
    } @else {
      <div class="loading-state">
        <span class="material-symbols-outlined spin">progress_activity</span>
      </div>
    }
  `,
  styles: [`
    @use 'variables' as *;

    .breadcrumb {
      display: flex; align-items: center; gap: 0.5rem;
      font-size: 0.875rem; color: $gray-500; margin-bottom: 1rem;
      a { color: $primary; &:hover { text-decoration: underline; } }
      .material-symbols-outlined { font-size: 18px; }
    }

    .detail {
      &__header {
        display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem;
        h1 { font-size: 1.5rem; font-weight: 700; color: $gray-900; text-transform: capitalize; }
      }
      &__location {
        display: inline-flex; align-items: center; gap: 0.25rem;
        font-size: 0.875rem; color: $gray-500; margin-top: 0.25rem;
        .material-symbols-outlined { font-size: 16px; }
      }
    }

    .stats-row {
      display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
      gap: 1rem; margin-bottom: 1.5rem;
    }

    .stat-card {
      display: flex; align-items: center; gap: 1rem; padding: 1.25rem;
      &--warning { border-color: rgba($warning, 0.3); }
      &__body { display: flex; flex-direction: column; }
      &__value { font-size: 1.5rem; font-weight: 700; color: $gray-900; }
      &__label { font-size: 0.75rem; color: $gray-500; }
    }

    .stat-icon {
      font-size: 32px;
      &--primary { color: $primary; }
      &--info { color: $info; }
      &--accent { color: $accent; }
      &--success { color: $success; }
      &--warning { color: $warning; }
    }

    .section {
      margin-bottom: 1.5rem;
      &__title {
        display: flex; align-items: center; gap: 0.5rem;
        font-size: 1.125rem; font-weight: 600; color: $gray-800; margin-bottom: 1rem;
        .material-symbols-outlined { font-size: 22px; color: $gray-500; }
      }
      &__header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
      &__empty { color: $gray-500; font-size: 0.875rem; }
    }

    .tag-list { display: flex; flex-wrap: wrap; gap: 0.5rem; }

    .table-wrapper { overflow-x: auto; }

    .data-table {
      width: 100%; border-collapse: collapse;
      th, td { padding: 0.75rem 1rem; text-align: left; font-size: 0.875rem; }
      th { color: $gray-500; font-weight: 600; border-bottom: 2px solid $gray-200; }
      td { border-bottom: 1px solid $gray-100; color: $gray-700; }
      tbody tr:hover { background: $gray-50; }
    }

    .loading-state { text-align: center; padding: 3rem; }
    .spin { animation: spin 1s linear infinite; font-size: 36px; color: $gray-400; }
    @keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
  `],
})
export class WarehouseDetailComponent implements OnInit {
  summary = signal<WarehouseSummaryDto | null>(null);
  categories = signal<WarehouseCategoryDto[]>([]);
  productShelves = signal<ProductShelfResponse[]>([]);

  private warehouseId!: number;

  constructor(
    private route: ActivatedRoute,
    private warehouseApi: WarehouseApiService,
    private stockApi: StockApiService,
  ) {}

  ngOnInit(): void {
    this.warehouseId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadData();
  }

  private loadData(): void {
    this.warehouseApi.getSummary(this.warehouseId).subscribe({
      next: (s) => this.summary.set(s),
    });

    this.warehouseApi.getCategoriesByWarehouse(this.warehouseId).subscribe({
      next: (cats) => this.categories.set(cats),
    });

    this.stockApi.getByWarehouseId(this.warehouseId, 0, 50).subscribe({
      next: (page) => this.productShelves.set(page.content),
    });
  }
}

