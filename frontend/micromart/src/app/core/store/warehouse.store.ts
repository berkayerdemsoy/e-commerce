import { Injectable, inject, signal } from '@angular/core';
import { WarehouseApiService } from '../api/warehouse-api.service';
import { WarehouseDto } from '../api/api.model';

/**
 * Signal-based store for Warehouses.
 * Provides a cached list and loading state.
 */
@Injectable({ providedIn: 'root' })
export class WarehouseStore {
  private readonly api = inject(WarehouseApiService);

  readonly warehouses = signal<WarehouseDto[]>([]);
  readonly loading = signal(false);
  private loaded = false;

  /** Load all warehouses (cached — call reload() to force refresh). */
  load(): void {
    if (this.loaded) return;
    this.forceReload();
  }

  /** Force reload from the API. */
  forceReload(): void {
    this.loading.set(true);
    this.api.getAll(0, 200).subscribe({
      next: (page) => {
        this.warehouses.set(page.content);
        this.loading.set(false);
        this.loaded = true;
      },
      error: () => this.loading.set(false),
    });
  }
}

