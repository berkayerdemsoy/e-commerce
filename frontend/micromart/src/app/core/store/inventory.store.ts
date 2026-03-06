import { Injectable, inject, signal } from '@angular/core';
import { WarehouseApiService } from '../api/warehouse-api.service';
import { AisleDto, ShelfDto } from '../api/api.model';

/**
 * Signal-based store for cascading Aisle / Shelf data.
 * Uses the filtered backend endpoints — NO client-side filtering.
 */
@Injectable({ providedIn: 'root' })
export class InventoryStore {
  private readonly warehouseApi = inject(WarehouseApiService);

  // ── Aisles (scoped to a selected warehouse) ──
  readonly aisles = signal<AisleDto[]>([]);
  readonly aislesLoading = signal(false);

  // ── Shelves (scoped to a selected aisle) ──
  readonly shelves = signal<ShelfDto[]>([]);
  readonly shelvesLoading = signal(false);

  /**
   * Fetch aisles for a specific warehouse.
   * Clears downstream shelves automatically.
   */
  loadAislesByWarehouse(warehouseId: number): void {
    this.aisles.set([]);
    this.shelves.set([]);
    this.aislesLoading.set(true);

    this.warehouseApi.getAislesByWarehouse(warehouseId).subscribe({
      next: (list) => {
        this.aisles.set(list);
        this.aislesLoading.set(false);
      },
      error: () => this.aislesLoading.set(false),
    });
  }

  /**
   * Fetch shelves for a specific aisle.
   */
  loadShelvesByAisle(aisleId: number): void {
    this.shelves.set([]);
    this.shelvesLoading.set(true);

    this.warehouseApi.getShelvesByAisle(aisleId).subscribe({
      next: (list) => {
        this.shelves.set(list);
        this.shelvesLoading.set(false);
      },
      error: () => this.shelvesLoading.set(false),
    });
  }

  /** Clear all cached data. */
  reset(): void {
    this.aisles.set([]);
    this.shelves.set([]);
  }
}

