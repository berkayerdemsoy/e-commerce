import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { ProductApiService } from '../../../core/api/product-api.service';
import { ProductCreateDto } from '../../../core/api/api.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [FormsModule, DecimalPipe],
  template: `
    <div class="page">
      <div class="page__header">
        <div>
          <h1>Ürünler</h1>
          <p>Ürün kataloğunu yönetin</p>
        </div>
        <button class="btn btn-primary" (click)="showForm.set(!showForm())">
          <span class="material-symbols-outlined">{{ showForm() ? 'close' : 'add' }}</span>
          {{ showForm() ? 'Kapat' : 'Yeni Ürün' }}
        </button>
      </div>

      <!-- Create/Edit Form -->
      @if (showForm()) {
        <div class="card form-section">
          <h2>{{ editingId ? 'Ürün Düzenle' : 'Yeni Ürün' }}</h2>
          <form class="inline-form" (ngSubmit)="onSubmit()">
            <div class="form-group">
              <label>Ürün Adı</label>
              <input type="text" class="form-input" [(ngModel)]="formData.name" name="name" required />
            </div>
            <div class="form-group">
              <label>Açıklama</label>
              <input type="text" class="form-input" [(ngModel)]="formData.description" name="desc" required />
            </div>
            <div class="form-group">
              <label>Fiyat</label>
              <input type="number" class="form-input" [(ngModel)]="formData.price" name="price" step="0.01" required />
            </div>
            <div class="form-group">
              <label>Kategori ID</label>
              <input type="number" class="form-input" [(ngModel)]="formData.category_id" name="catId" required />
            </div>
            <div class="form-actions">
              <button type="submit" class="btn btn-primary">
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
                  <td>{{ p.category_id }}</td>
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
  products = signal<ProductCreateDto[]>([]);
  currentPage = signal(0);
  totalPages = signal(0);
  showForm = signal(false);
  editingId: number | null = null;
  formData: ProductCreateDto = { name: '', description: '', price: 0, category_id: 0 };

  constructor(private api: ProductApiService) {}

  ngOnInit(): void {
    this.loadPage(0);
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

  onSubmit(): void {
    const op$ = this.editingId
      ? this.api.updateProduct(this.editingId, this.formData)
      : this.api.createProduct(this.formData);

    op$.subscribe({
      next: () => {
        this.loadPage(this.currentPage());
        this.cancelEdit();
      },
    });
  }

  onEdit(p: ProductCreateDto): void {
    this.editingId = p.id!;
    this.formData = { ...p };
    this.showForm.set(true);
  }

  cancelEdit(): void {
    this.editingId = null;
    this.formData = { name: '', description: '', price: 0, category_id: 0 };
    this.showForm.set(false);
  }

  onDelete(p: ProductCreateDto): void {
    if (confirm(`"${p.name}" ürünü silinecek. Emin misiniz?`)) {
      this.api.deleteProduct(p.id!).subscribe({
        next: () => this.loadPage(this.currentPage()),
      });
    }
  }
}




