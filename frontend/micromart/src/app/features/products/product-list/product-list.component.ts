import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { Select } from 'primeng/select';
import { ProductApiService } from '../../../core/api/product-api.service';
import { CategoryStore } from '../../../core/store/category.store';
import { NotificationService } from '../../../core/notification/notification.service';
import { ProductCreateDto } from '../../../core/api/api.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [ReactiveFormsModule, DecimalPipe, Select],
  template: `
    <div class="page">
      <div class="page__header">
        <div>
          <h1>Ürünler</h1>
          <p>Ürün kataloğunu yönetin</p>
        </div>
        <button class="btn btn-primary" (click)="toggleForm()">
          <span class="material-symbols-outlined">{{ showForm() ? 'close' : 'add' }}</span>
          {{ showForm() ? 'Kapat' : 'Yeni Ürün' }}
        </button>
      </div>

      <!-- Create/Edit Form -->
      @if (showForm()) {
        <div class="card form-section">
          <h2>{{ editingId ? 'Ürün Düzenle' : 'Yeni Ürün' }}</h2>
          <form [formGroup]="form" (ngSubmit)="onSubmit()" class="inline-form">
            <div class="form-group">
              <label>Ürün Adı</label>
              <input type="text" class="form-input" formControlName="name" placeholder="Ürün adı" />
            </div>
            <div class="form-group">
              <label>Açıklama</label>
              <input type="text" class="form-input" formControlName="description" placeholder="Ürün açıklaması" />
            </div>
            <div class="form-group">
              <label>Fiyat</label>
              <input type="number" class="form-input" formControlName="price" step="0.01" />
            </div>
            <div class="form-group" style="min-width: 200px;">
              <label>Kategori</label>
              <p-select
                formControlName="category_id"
                [options]="categoryStore.categories()"
                optionLabel="name"
                optionValue="id"
                placeholder="Kategori seçin..."
                [filter]="true"
                filterPlaceholder="Ara..."
                [showClear]="true"
                styleClass="w-full" />
            </div>
            <div class="form-actions">
              <button type="submit" class="btn btn-primary" [disabled]="form.invalid">
                {{ editingId ? 'Güncelle' : 'Oluştur' }}
              </button>
              @if (editingId) {
                <button type="button" class="btn btn-secondary" (click)="cancelEdit()">İptal</button>
              }
            </div>
          </form>
        </div>
      }

      <!-- Table -->
      <div class="card">
        <div class="table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Ürün Adı</th>
                <th>Açıklama</th>
                <th>Fiyat</th>
                <th>Kategori</th>
                <th>İşlemler</th>
              </tr>
            </thead>
            <tbody>
              @for (p of products(); track p.id) {
                <tr>
                  <td>{{ p.id }}</td>
                  <td><strong>{{ p.name }}</strong></td>
                  <td class="desc-cell">{{ p.description }}</td>
                  <td>₺{{ p.price | number:'1.2-2' }}</td>
                  <td>{{ getCategoryName(p.category_id) }}</td>
                  <td class="action-cell">
                    <button class="btn btn-secondary btn-sm" (click)="onEdit(p)">
                      <span class="material-symbols-outlined">edit</span>
                    </button>
                    <button class="btn btn-danger btn-sm" (click)="onDelete(p)">
                      <span class="material-symbols-outlined">delete</span>
                    </button>
                  </td>
                </tr>
              } @empty {
                <tr><td colspan="6" class="empty-cell">Ürün bulunamadı.</td></tr>
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
      display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem;
      h1 { font-size: 1.5rem; font-weight: 700; color: $gray-900; }
      p { color: $gray-500; margin-top: 0.25rem; font-size: 0.875rem; }
    }

    .form-section {
      margin-bottom: 1.5rem;
      h2 { font-size: 1.125rem; font-weight: 600; margin-bottom: 1rem; color: $gray-800; }
    }

    .inline-form {
      display: flex; flex-wrap: wrap; gap: 1rem; align-items: flex-end;
      padding: 1rem; background: $gray-50; border-radius: $border-radius;
    }

    .form-actions { display: flex; gap: 0.5rem; align-items: flex-end; }

    .table-wrapper { overflow-x: auto; }

    .data-table {
      width: 100%; border-collapse: collapse;
      th, td { padding: 0.625rem 0.75rem; text-align: left; font-size: 0.8125rem; }
      th { color: $gray-500; font-weight: 600; border-bottom: 2px solid $gray-200; }
      td { border-bottom: 1px solid $gray-100; color: $gray-700; }
      tbody tr:hover { background: $gray-50; }
    }

    .desc-cell { max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    .action-cell { display: flex; gap: 0.375rem; }
    .empty-cell { text-align: center; color: $gray-400; padding: 2rem !important; }
    .btn-sm { padding: 0.375rem 0.5rem; .material-symbols-outlined { font-size: 18px; } }

    .pagination {
      display: flex; align-items: center; justify-content: center; gap: 1rem; margin-top: 1.5rem;
      &__info { font-size: 0.875rem; color: $gray-600; }
    }
  `],
})
export class ProductListComponent implements OnInit {
  private readonly api = inject(ProductApiService);
  private readonly fb = inject(FormBuilder);
  private readonly notify = inject(NotificationService);
  readonly categoryStore = inject(CategoryStore);

  products = signal<ProductCreateDto[]>([]);
  currentPage = signal(0);
  totalPages = signal(0);
  showForm = signal(false);
  editingId: number | null = null;

  form = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
    price: [0, [Validators.required, Validators.min(0.01)]],
    category_id: [null as number | null, Validators.required],
  });

  ngOnInit(): void {
    this.loadPage(0);
    this.categoryStore.load();
  }

  toggleForm(): void {
    if (this.showForm()) {
      this.cancelEdit();
    } else {
      this.showForm.set(true);
    }
  }

  loadPage(page: number): void {
    this.api.getAllProducts(page, 15).subscribe({
      next: (res) => {
        this.products.set(res.content);
        this.currentPage.set(res.number);
        this.totalPages.set(res.totalPages);
      },
    });
  }

  getCategoryName(categoryId: number): string {
    const cat = this.categoryStore.categories().find((c) => c.id === categoryId);
    return cat ? cat.name : `#${categoryId}`;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const val = this.form.getRawValue();
    const dto: ProductCreateDto = {
      name: val.name!,
      description: val.description!,
      price: val.price!,
      category_id: val.category_id!,
    };

    const op$ = this.editingId
      ? this.api.updateProduct(this.editingId, dto)
      : this.api.createProduct(dto);

    op$.subscribe({
      next: () => {
        this.loadPage(this.currentPage());
        this.cancelEdit();
        this.notify.success(this.editingId ? 'Ürün güncellendi.' : 'Ürün oluşturuldu.');
      },
    });
  }

  onEdit(p: ProductCreateDto): void {
    this.editingId = p.id!;
    this.form.patchValue({
      name: p.name,
      description: p.description,
      price: p.price,
      category_id: p.category_id,
    });
    this.showForm.set(true);
  }

  cancelEdit(): void {
    this.editingId = null;
    this.form.reset();
    this.showForm.set(false);
  }

  onDelete(p: ProductCreateDto): void {
    if (confirm(`"${p.name}" ürünü silinecek. Emin misiniz?`)) {
      this.api.deleteProduct(p.id!).subscribe({
        next: () => {
          this.loadPage(this.currentPage());
          this.notify.success('Ürün silindi.');
        },
      });
    }
  }
}




