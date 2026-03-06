import { Component, OnInit, computed } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ChartModule } from 'primeng/chart';
import { ProgressBar } from 'primeng/progressbar';
import { AuthService } from '../../core/auth/auth.service';
import { ThemeService } from '../../core/theme/theme.service';
import { DashboardService } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, ChartModule, ProgressBar, DecimalPipe],
  template: `
    <div class="dashboard">
      <!-- ── Header ── -->
      <div class="dashboard__header">
        <div>
          <h1>Hoş geldin, {{ authService.currentUser()?.username }}!</h1>
          <p>Depo yönetim sistemi genel özeti</p>
        </div>
        <div class="dashboard__header-date">
          <span class="material-symbols-outlined">calendar_today</span>
          {{ today }}
        </div>
      </div>

      <!-- ── Loading ── -->
      @if (dashboardService.loading()) {
        <div class="loading-state">
          <span class="material-symbols-outlined spin">progress_activity</span>
          <p>Veriler yükleniyor...</p>
        </div>
      }

      <!-- ── Empty State ── -->
      @if (dashboardService.summaries().length === 0 && !dashboardService.loading()) {
        <div class="empty-state glass-card">
          <span class="material-symbols-outlined">add_business</span>
          <h3>Henüz depo bulunmuyor</h3>
          <p>İlk deponuzu oluşturmak için Depolar sayfasını ziyaret edin.</p>
          <a routerLink="/warehouses" class="btn btn-primary">
            <span class="material-symbols-outlined">add</span>
            Depo Oluştur
          </a>
        </div>
      }

      @if (dashboardService.summaries().length > 0 && !dashboardService.loading()) {
        <!-- ── KPI Summary Cards ── -->
        <div class="kpi-grid">
          <div class="kpi-card kpi-card--warehouses glass-card">
            <div class="kpi-card__icon-wrap kpi-card__icon-wrap--indigo">
              <span class="material-symbols-outlined">warehouse</span>
            </div>
            <div class="kpi-card__content">
              <span class="kpi-card__value">{{ dashboardService.totalWarehouses() }}</span>
              <span class="kpi-card__label">Toplam Depo</span>
            </div>
          </div>

          <div class="kpi-card kpi-card--products glass-card">
            <div class="kpi-card__icon-wrap kpi-card__icon-wrap--cyan">
              <span class="material-symbols-outlined">inventory_2</span>
            </div>
            <div class="kpi-card__content">
              <span class="kpi-card__value">{{ dashboardService.totalProducts() }}</span>
              <span class="kpi-card__label">Toplam Ürün</span>
            </div>
          </div>

          <div class="kpi-card kpi-card--stock glass-card">
            <div class="kpi-card__icon-wrap kpi-card__icon-wrap--emerald">
              <span class="material-symbols-outlined">deployed_code</span>
            </div>
            <div class="kpi-card__content">
              <span class="kpi-card__value">{{ dashboardService.totalStock() | number }}</span>
              <span class="kpi-card__label">Toplam Stok</span>
            </div>
          </div>

          <div class="kpi-card kpi-card--alerts glass-card">
            <div class="kpi-card__icon-wrap kpi-card__icon-wrap--rose">
              <span class="material-symbols-outlined">
                {{ dashboardService.totalAlerts() > 0 ? 'warning' : 'check_circle' }}
              </span>
            </div>
            <div class="kpi-card__content">
              <span class="kpi-card__value">{{ dashboardService.totalAlerts() }}</span>
              <span class="kpi-card__label">Düşük Stok Uyarısı</span>
            </div>
          </div>
        </div>

        <!-- ── Charts Row ── -->
        <div class="charts-grid">
          <!-- Stock Distribution Doughnut -->
          <div class="chart-card glass-card">
            <div class="chart-card__header">
              <span class="material-symbols-outlined">pie_chart</span>
              <h3>Depo Bazlı Stok Dağılımı</h3>
            </div>
            <div class="chart-card__body">
              <p-chart type="doughnut"
                       [data]="stockChartData()"
                       [options]="doughnutOptions()"
                       [style]="{ width: '100%', maxWidth: '320px', margin: '0 auto' }" />
            </div>
          </div>

          <!-- Category Breakdown Doughnut -->
          <div class="chart-card glass-card">
            <div class="chart-card__header">
              <span class="material-symbols-outlined">category</span>
              <h3>Kategori Dağılımı</h3>
            </div>
            <div class="chart-card__body">
              <p-chart type="doughnut"
                       [data]="categoryChartData()"
                       [options]="doughnutOptions()"
                       [style]="{ width: '100%', maxWidth: '320px', margin: '0 auto' }" />
            </div>
          </div>
        </div>

        <!-- ── Capacity Progress Bars ── -->
        <div class="capacity-section glass-card">
          <div class="capacity-section__header">
            <span class="material-symbols-outlined">speed</span>
            <h3>Depo Kapasite Durumu</h3>
          </div>

          @if (dashboardService.capacities().length === 0) {
            <p class="capacity-section__empty">Kapasite verisi bulunamadı.</p>
          }

          <div class="capacity-list">
            @for (cap of dashboardService.capacities(); track cap.warehouseId) {
              <div class="capacity-item">
                <div class="capacity-item__header">
                  <div class="capacity-item__info">
                    <span class="capacity-item__name">{{ cap.warehouseName }}</span>
                    <span class="capacity-item__location">
                      <span class="material-symbols-outlined">location_on</span>
                      {{ cap.location }}
                    </span>
                  </div>
                  <div class="capacity-item__numbers">
                    <span class="capacity-item__used">{{ cap.usedCapacity }}</span>
                    <span class="capacity-item__sep">/</span>
                    <span class="capacity-item__total">{{ cap.totalCapacity }}</span>
                    <span class="capacity-item__percent"
                          [class.text-green]="cap.percent < 60"
                          [class.text-amber]="cap.percent >= 60 && cap.percent < 85"
                          [class.text-red]="cap.percent >= 85">
                      ({{ cap.percent }}%)
                    </span>
                  </div>
                </div>
                <p-progressBar
                  [value]="cap.percent"
                  [showValue]="false"
                  [style]="{ height: '12px', borderRadius: '6px' }"
                  [styleClass]="getCapacityBarClass(cap.percent)" />
              </div>
            }
          </div>
        </div>

        <!-- ── Warehouse Detail Cards ── -->
        <div class="warehouse-grid">
          @for (s of dashboardService.summaries(); track s.warehouseId) {
            <div class="warehouse-card glass-card">
              <div class="warehouse-card__header">
                <div>
                  <h3>{{ s.warehouseName }}</h3>
                  <span class="warehouse-card__location">
                    <span class="material-symbols-outlined">location_on</span>
                    {{ s.location }}
                  </span>
                </div>
                <a [routerLink]="['/warehouses', s.warehouseId]" class="btn btn-secondary btn-sm">
                  <span class="material-symbols-outlined">open_in_new</span>
                  Detay
                </a>
              </div>

              <div class="warehouse-card__stats">
                <div class="mini-stat">
                  <span class="mini-stat__icon material-symbols-outlined">inventory_2</span>
                  <span class="mini-stat__value">{{ s.totalProducts }}</span>
                  <span class="mini-stat__label">Ürün</span>
                </div>
                <div class="mini-stat">
                  <span class="mini-stat__icon material-symbols-outlined">view_column</span>
                  <span class="mini-stat__value">{{ s.totalAisles }}</span>
                  <span class="mini-stat__label">Koridor</span>
                </div>
                <div class="mini-stat">
                  <span class="mini-stat__icon material-symbols-outlined">shelves</span>
                  <span class="mini-stat__value">{{ s.totalShelves }}</span>
                  <span class="mini-stat__label">Raf</span>
                </div>
                <div class="mini-stat">
                  <span class="mini-stat__icon material-symbols-outlined">deployed_code</span>
                  <span class="mini-stat__value">{{ s.totalStockQuantity }}</span>
                  <span class="mini-stat__label">Stok</span>
                </div>
              </div>

              @if (s.lowStockAlertCount > 0) {
                <div class="warehouse-card__alert">
                  <span class="material-symbols-outlined">warning</span>
                  {{ s.lowStockAlertCount }} üründe düşük stok uyarısı
                </div>
              }
            </div>
          }
        </div>
      }
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    // ── Glassmorphism Card ──
    .glass-card {
      background: $surface-card;
      border: 1px solid $surface-border;
      border-radius: $border-radius-lg;
      box-shadow: $shadow-sm;
      padding: 1.5rem;
      backdrop-filter: blur(12px);
      transition: box-shadow $transition-base, background-color $transition-base,
                  border-color $transition-base, transform 200ms ease;

      &:hover {
        box-shadow: $shadow-md;
        transform: translateY(-2px);
      }
    }

    // ── Dashboard ──
    .dashboard {
      &__header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 2rem;

        h1 {
          font-size: 1.625rem;
          font-weight: 700;
          color: $text-primary;
          letter-spacing: -0.025em;
        }
        p {
          color: $text-muted;
          margin-top: 0.25rem;
          font-size: 0.9rem;
        }

        &-date {
          display: inline-flex;
          align-items: center;
          gap: 0.5rem;
          font-size: 0.875rem;
          color: $text-muted;
          background: $surface-card;
          padding: 0.5rem 1rem;
          border-radius: $border-radius;
          border: 1px solid $surface-border;
          .material-symbols-outlined { font-size: 18px; }
        }
      }
    }

    // ── KPI Grid ──
    .kpi-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 1.25rem;
      margin-bottom: 1.75rem;

      @media (max-width: 1024px) { grid-template-columns: repeat(2, 1fr); }
      @media (max-width: 640px) { grid-template-columns: 1fr; }
    }

    .kpi-card {
      display: flex;
      align-items: center;
      gap: 1.25rem;
      padding: 1.5rem;
      position: relative;
      overflow: hidden;

      // Subtle gradient accent at top
      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 3px;
        border-radius: $border-radius-lg $border-radius-lg 0 0;
      }

      &--warehouses::before { background: linear-gradient(90deg, $primary, $primary-light); }
      &--products::before  { background: linear-gradient(90deg, $accent, #22d3ee); }
      &--stock::before     { background: linear-gradient(90deg, $success, #34d399); }
      &--alerts::before    { background: linear-gradient(90deg, $danger, #fb7185); }

      &__icon-wrap {
        width: 52px;
        height: 52px;
        border-radius: $border-radius-lg;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;

        .material-symbols-outlined { font-size: 26px; color: #fff; }

        &--indigo  { background: linear-gradient(135deg, $primary, $primary-light); }
        &--cyan    { background: linear-gradient(135deg, $accent, #22d3ee); }
        &--emerald { background: linear-gradient(135deg, $success, #34d399); }
        &--rose    { background: linear-gradient(135deg, $danger, #fb7185); }
      }

      &__content {
        display: flex;
        flex-direction: column;
      }

      &__value {
        font-size: 1.75rem;
        font-weight: 800;
        color: $text-primary;
        line-height: 1.1;
        letter-spacing: -0.025em;
      }

      &__label {
        font-size: 0.8125rem;
        color: $text-muted;
        margin-top: 0.25rem;
        font-weight: 500;
      }
    }

    // ── Charts Grid ──
    .charts-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 1.25rem;
      margin-bottom: 1.75rem;

      @media (max-width: 768px) { grid-template-columns: 1fr; }
    }

    .chart-card {
      &__header {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        margin-bottom: 1.25rem;
        padding-bottom: 0.75rem;
        border-bottom: 1px solid $surface-border;

        .material-symbols-outlined {
          font-size: 22px;
          color: $primary;
        }

        h3 {
          font-size: 1rem;
          font-weight: 600;
          color: $text-primary;
        }
      }

      &__body {
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 280px;
      }
    }

    // ── Capacity Section ──
    .capacity-section {
      margin-bottom: 1.75rem;

      &__header {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        margin-bottom: 1.5rem;
        padding-bottom: 0.75rem;
        border-bottom: 1px solid $surface-border;

        .material-symbols-outlined {
          font-size: 22px;
          color: $primary;
        }

        h3 {
          font-size: 1rem;
          font-weight: 600;
          color: $text-primary;
        }
      }

      &__empty {
        color: $text-muted;
        font-size: 0.875rem;
        text-align: center;
        padding: 1rem;
      }
    }

    .capacity-list {
      display: flex;
      flex-direction: column;
      gap: 1.25rem;
    }

    .capacity-item {
      &__header {
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        margin-bottom: 0.5rem;
      }

      &__info {
        display: flex;
        flex-direction: column;
      }

      &__name {
        font-weight: 600;
        font-size: 0.9375rem;
        color: $text-primary;
        text-transform: capitalize;
      }

      &__location {
        display: inline-flex;
        align-items: center;
        gap: 0.2rem;
        font-size: 0.75rem;
        color: $text-muted;
        margin-top: 0.125rem;
        .material-symbols-outlined { font-size: 14px; }
      }

      &__numbers {
        font-size: 0.8125rem;
        color: $text-secondary;
        font-weight: 500;
      }

      &__used { font-weight: 700; color: $text-primary; }
      &__sep  { margin: 0 0.125rem; color: $text-muted; }
      &__total { color: $text-muted; }

      &__percent {
        font-weight: 700;
        margin-left: 0.375rem;
      }
    }

    .text-green  { color: $success; }
    .text-amber  { color: $warning; }
    .text-red    { color: $danger; }

    // ── Warehouse Cards Grid ──
    .warehouse-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
      gap: 1.25rem;
    }

    .warehouse-card {
      &__header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 1rem;

        h3 {
          font-size: 1.0625rem;
          font-weight: 600;
          color: $text-primary;
          text-transform: capitalize;
        }
      }

      &__location {
        display: inline-flex;
        align-items: center;
        gap: 0.25rem;
        font-size: 0.8rem;
        color: $text-muted;
        margin-top: 0.25rem;
        .material-symbols-outlined { font-size: 16px; }
      }

      &__stats {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 0.75rem;
        padding: 1rem 0;
        border-top: 1px solid $surface-border;
      }

      &__alert {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        padding: 0.625rem 0.875rem;
        background: rgba($warning, 0.08);
        color: $warning;
        border-radius: $border-radius;
        font-size: 0.8125rem;
        font-weight: 500;
        margin-top: 0.5rem;
        .material-symbols-outlined { font-size: 18px; }
      }
    }

    .mini-stat {
      display: flex;
      flex-direction: column;
      align-items: center;
      text-align: center;
      gap: 0.125rem;

      &__icon {
        font-size: 20px;
        color: $primary-light;
        margin-bottom: 0.125rem;
      }

      &__value {
        font-size: 1.375rem;
        font-weight: 700;
        color: $text-primary;
        line-height: 1.1;
      }

      &__label {
        font-size: 0.6875rem;
        color: $text-muted;
        font-weight: 500;
      }
    }

    .btn-sm {
      padding: 0.375rem 0.75rem;
      font-size: 0.8125rem;
      .material-symbols-outlined { font-size: 16px; }
    }

    // ── Progress bar color classes ──
    :host ::ng-deep {
      .capacity-bar-green .p-progressbar-value {
        background: linear-gradient(90deg, $success, #34d399) !important;
      }
      .capacity-bar-amber .p-progressbar-value {
        background: linear-gradient(90deg, $warning, #fbbf24) !important;
      }
      .capacity-bar-red .p-progressbar-value {
        background: linear-gradient(90deg, $danger, #fb7185) !important;
      }
      .capacity-bar-green .p-progressbar,
      .capacity-bar-amber .p-progressbar,
      .capacity-bar-red .p-progressbar {
        background: $gray-200;
        border-radius: 6px;
        overflow: hidden;
      }
    }

    // ── States ──
    .empty-state {
      text-align: center;
      padding: 3rem;
      .material-symbols-outlined { font-size: 48px; color: $gray-400; }
      h3 { margin: 1rem 0 0.5rem; color: $text-primary; font-size: 1.125rem; }
      p { color: $text-muted; margin-bottom: 1.5rem; }
    }

    .loading-state {
      text-align: center;
      padding: 3rem;
      color: $text-muted;
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
  readonly today = new Date().toLocaleDateString('tr-TR', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  // ── Chart palette ──
  private readonly CHART_COLORS = [
    '#4f46e5', '#06b6d4', '#10b981', '#f59e0b', '#ef4444',
    '#8b5cf6', '#ec4899', '#14b8a6', '#f97316', '#6366f1',
  ];

  // ── Computed chart data objects ──
  readonly stockChartData = computed(() => ({
    labels: this.dashboardService.stockDistributionLabels(),
    datasets: [
      {
        data: this.dashboardService.stockDistributionData(),
        backgroundColor: this.CHART_COLORS.slice(
          0,
          this.dashboardService.stockDistributionData().length,
        ),
        borderWidth: 0,
        hoverOffset: 8,
      },
    ],
  }));

  readonly categoryChartData = computed(() => ({
    labels: this.dashboardService.categoryLabels(),
    datasets: [
      {
        data: this.dashboardService.categoryData(),
        backgroundColor: this.CHART_COLORS.slice(
          0,
          this.dashboardService.categoryData().length,
        ),
        borderWidth: 0,
        hoverOffset: 8,
      },
    ],
  }));

  readonly doughnutOptions = computed(() => {
    const isDark = this.themeService.isDark();
    const textColor = isDark ? '#d1d5db' : '#374151';
    return {
      responsive: true,
      maintainAspectRatio: true,
      cutout: '65%',
      plugins: {
        legend: {
          position: 'bottom' as const,
          labels: {
            color: textColor,
            padding: 16,
            usePointStyle: true,
            pointStyleWidth: 10,
            font: { size: 12, family: 'Inter, sans-serif' },
          },
        },
      },
    };
  });

  constructor(
    public authService: AuthService,
    public dashboardService: DashboardService,
    public themeService: ThemeService,
  ) {}

  ngOnInit(): void {
    this.dashboardService.loadDashboard();
  }

  getCapacityBarClass(percent: number): string {
    if (percent >= 85) return 'capacity-bar-red';
    if (percent >= 60) return 'capacity-bar-amber';
    return 'capacity-bar-green';
  }
}
