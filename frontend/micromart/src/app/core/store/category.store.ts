import { Injectable, inject, signal } from '@angular/core';
import { ProductApiService } from '../api/product-api.service';
import { CategoryDto } from '../api/api.model';
import { NotificationService } from '../notification/notification.service';

/**
 * Signal-based store for Categories.
 * Provides a cached list, loading state, and CRUD helpers.
 */
@Injectable({ providedIn: 'root' })
export class CategoryStore {
  private readonly api = inject(ProductApiService);
  private readonly notify = inject(NotificationService);

  readonly categories = signal<CategoryDto[]>([]);
  readonly loading = signal(false);
  private loaded = false;

  /** Load all categories (cached — call forceReload() to refresh). */
  load(): void {
    if (this.loaded) return;
    this.forceReload();
  }

  /** Force reload from the API. */
  forceReload(): void {
    this.loading.set(true);
    this.api.getAllCategories(0, 200).subscribe({
      next: (page) => {
        this.categories.set(page.content);
        this.loading.set(false);
        this.loaded = true;
      },
      error: () => this.loading.set(false),
    });
  }

  /** Create a new category and append to the local list. */
  create(dto: CategoryDto): void {
    this.api.createCategory(dto).subscribe({
      next: (created) => {
        this.categories.update((list) => [...list, created]);
        this.notify.success(`"${created.name}" kategorisi oluşturuldu.`);
      },
    });
  }

  /** Update an existing category in the local list. */
  update(id: number, dto: CategoryDto): void {
    this.api.updateCategory(id, dto).subscribe({
      next: (updated) => {
        this.categories.update((list) =>
          list.map((c) => (c.id === id ? updated : c)),
        );
        this.notify.success(`"${updated.name}" kategorisi güncellendi.`);
      },
    });
  }

  /** Delete a category and remove from the local list. */
  delete(id: number): void {
    this.api.deleteCategory(id).subscribe({
      next: () => {
        this.categories.update((list) => list.filter((c) => c.id !== id));
        this.notify.success('Kategori silindi.');
      },
    });
  }
}

