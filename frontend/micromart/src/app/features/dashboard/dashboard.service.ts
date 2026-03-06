import { Injectable, signal, computed } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { WarehouseApiService } from '../../core/api/warehouse-api.service';
import { ProductApiService } from '../../core/api/product-api.service';
import {
  WarehouseSummaryDto,
  ShelfDto,
  CategoryDto,
  WarehouseCategoryDto,
} from '../../core/api/api.model';

// ── Derived models ──
export interface WarehouseCapacity {
  warehouseId: number;
  warehouseName: string;
  location: string;
  totalCapacity: number;
  usedCapacity: number;
  percent: number;
}

export interface CategoryBreakdown {
  categoryName: string;
  productCount: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  // ── Raw data signals ──
  readonly summaries = signal<WarehouseSummaryDto[]>([]);
  readonly capacities = signal<WarehouseCapacity[]>([]);
  readonly categoryBreakdown = signal<CategoryBreakdown[]>([]);
  readonly loading = signal(true);

  // ── Computed: KPI totals ──
  readonly totalWarehouses = computed(() => this.summaries().length);
  readonly totalProducts = computed(() =>
    this.summaries().reduce((sum, s) => sum + s.totalProducts, 0),
  );
  readonly totalStock = computed(() =>
    this.summaries().reduce((sum, s) => sum + s.totalStockQuantity, 0),
  );
  readonly totalAlerts = computed(() =>
    this.summaries().reduce((sum, s) => sum + s.lowStockAlertCount, 0),
  );

  // ── Computed: Stock Distribution Doughnut ──
  readonly stockDistributionLabels = computed(() =>
    this.summaries().map((s) => s.warehouseName),
  );
  readonly stockDistributionData = computed(() =>
    this.summaries().map((s) => s.totalStockQuantity),
  );

  // ── Computed: Category Breakdown Doughnut ──
  readonly categoryLabels = computed(() =>
    this.categoryBreakdown().map((c) => c.categoryName),
  );
  readonly categoryData = computed(() =>
    this.categoryBreakdown().map((c) => c.productCount),
  );

  constructor(
    private warehouseApi: WarehouseApiService,
    private productApi: ProductApiService,
  ) {}

  loadDashboard(): void {
    this.loading.set(true);

    this.warehouseApi.getAll(0, 100).pipe(
      switchMap((page) => {
        const warehouses = page.content;
        if (warehouses.length === 0) {
          this.summaries.set([]);
          this.capacities.set([]);
          this.categoryBreakdown.set([]);
          this.loading.set(false);
          return of(null);
        }

        // Parallel: summaries + shelves per warehouse + categories
        const summaryObs = warehouses.map((w) =>
          this.warehouseApi.getSummary(w.id!).pipe(catchError(() => of(null))),
        );

        const shelfObs = warehouses.map((w) =>
          this.warehouseApi.getAislesByWarehouse(w.id!).pipe(
            switchMap((aisles) => {
              if (aisles.length === 0) return of([] as ShelfDto[]);
              return forkJoin(
                aisles.map((a) =>
                  this.warehouseApi.getShelvesByAisle(a.id!).pipe(catchError(() => of([] as ShelfDto[]))),
                ),
              ).pipe(
                switchMap((shelfArrays) => of(shelfArrays.flat())),
              );
            }),
            catchError(() => of([] as ShelfDto[])),
          ),
        );

        const warehouseCategoryObs = warehouses.map((w) =>
          this.warehouseApi.getCategoriesByWarehouse(w.id!).pipe(catchError(() => of([] as WarehouseCategoryDto[]))),
        );

        const allCategoriesObs = this.productApi.getAllCategories(0, 200).pipe(
          catchError(() => of({ content: [] as CategoryDto[] })),
        );

        return forkJoin({
          summaries: forkJoin(summaryObs),
          shelves: forkJoin(shelfObs),
          warehouseCategories: forkJoin(warehouseCategoryObs),
          allCategories: allCategoriesObs,
        }).pipe(
          switchMap(({ summaries, shelves, warehouseCategories, allCategories }) => {
            // Process summaries
            const validSummaries = summaries.filter((s): s is WarehouseSummaryDto => s !== null);
            this.summaries.set(validSummaries);

            // Process capacities
            const caps: WarehouseCapacity[] = warehouses.map((w, i) => {
              const warehouseShelves = shelves[i];
              const totalCap = warehouseShelves.reduce((sum, sh) => sum + sh.capacity, 0);
              const usedCap = warehouseShelves.reduce((sum, sh) => sum + sh.usedCapacity, 0);
              const matchingSummary = validSummaries.find((s) => s.warehouseId === w.id);
              return {
                warehouseId: w.id!,
                warehouseName: matchingSummary?.warehouseName ?? w.name,
                location: matchingSummary?.location ?? w.location,
                totalCapacity: totalCap,
                usedCapacity: usedCap,
                percent: totalCap > 0 ? Math.round((usedCap / totalCap) * 100) : 0,
              };
            });
            this.capacities.set(caps);

            // Process category breakdown
            const categoryMap = new Map<number, string>();
            allCategories.content.forEach((c) => categoryMap.set(c.id!, c.name));

            const catCountMap = new Map<string, number>();
            warehouseCategories.flat().forEach((wc) => {
              const catName = categoryMap.get(wc.categoryId) ?? `Kategori #${wc.categoryId}`;
              catCountMap.set(catName, (catCountMap.get(catName) ?? 0) + 1);
            });

            // Also attribute product counts from summaries if possible
            // Use warehouse category assignment count as the breakdown
            const breakdown: CategoryBreakdown[] = Array.from(catCountMap.entries()).map(
              ([name, count]) => ({ categoryName: name, productCount: count }),
            );
            this.categoryBreakdown.set(breakdown.length > 0 ? breakdown : this.getMockCategoryBreakdown());

            this.loading.set(false);
            return of(null);
          }),
        );
      }),
    ).subscribe({
      error: () => this.loading.set(false),
    });
  }

  /** Fallback mock data when no categories are assigned */
  private getMockCategoryBreakdown(): CategoryBreakdown[] {
    return [
      { categoryName: 'Elektronik', productCount: 12 },
      { categoryName: 'Giyim', productCount: 8 },
      { categoryName: 'Gıda', productCount: 15 },
      { categoryName: 'Mobilya', productCount: 5 },
      { categoryName: 'Kozmetik', productCount: 7 },
    ];
  }
}



