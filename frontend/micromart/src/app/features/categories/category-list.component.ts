import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Dialog } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { CategoryStore } from '../../core/store/category.store';
import { CategoryDto } from '../../core/api/api.model';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [ReactiveFormsModule, Dialog, InputText],
  template: `
    <div class="page">
      <div class="page__header">
        <div>
          <h1>Kategoriler</h1>
          <p>Ürün kategorilerini yönetin</p>
        </div>
        <button class="btn btn-primary" (click)="openCreate()">
          <span class="material-symbols-outlined">add</span>
          Yeni Kategori
        </button>
      </div>

      <!-- Category Grid -->
      <div class="category-grid">
        @for (cat of categoryStore.categories(); track cat.id) {
          <div class="card category-card">
            <div class="category-card__icon">
              <span class="material-symbols-outlined">label</span>
            </div>
            <div class="category-card__body">
              <h3>{{ cat.name }}</h3>
              <span class="category-card__id">#{{ cat.id }}</span>
            </div>
            <div class="category-card__actions">
              <button class="btn btn-secondary btn-sm" (click)="openEdit(cat)" title="Düzenle">
                <span class="material-symbols-outlined">edit</span>
              </button>
              <button class="btn btn-danger btn-sm" (click)="onDelete(cat)" title="Sil">
                <span class="material-symbols-outlined">delete</span>
              </button>
            </div>
          </div>
        } @empty {
          <div class="card empty-state">
            <span class="material-symbols-outlined">category</span>
            <h3>Henüz kategori yok</h3>
            <p>İlk kategorinizi oluşturmak için "Yeni Kategori" butonuna tıklayın.</p>
          </div>
        }
      </div>

      <!-- Create/Edit Dialog -->
      <p-dialog
        [(visible)]="dialogVisible"
        [header]="editingId() ? 'Kategori Düzenle' : 'Yeni Kategori'"
        [modal]="true"
        [closable]="true"
        [draggable]="false"
        [resizable]="false"
        [style]="{ width: '420px' }">

        <form [formGroup]="form" (ngSubmit)="onSubmit()" class="dialog-form">
          <div class="form-group">
            <label for="cat-name">Kategori Adı</label>
            <input pInputText id="cat-name" type="text"
                   formControlName="name"
                   placeholder="Örn: Elektronik"
                   class="w-full" />
            @if (form.get('name')?.touched && form.get('name')?.hasError('required')) {
              <span class="form-error">Kategori adı zorunludur</span>
            }
            @if (form.get('name')?.touched && form.get('name')?.hasError('minlength')) {
              <span class="form-error">En az 2 karakter olmalı</span>
            }
          </div>

          <div class="dialog-actions">
            <button type="button" class="btn btn-secondary" (click)="dialogVisible = false">İptal</button>
            <button type="submit" class="btn btn-primary" [disabled]="form.invalid">
              <span class="material-symbols-outlined">{{ editingId() ? 'save' : 'add' }}</span>
              {{ editingId() ? 'Güncelle' : 'Oluştur' }}
            </button>
          </div>
        </form>
      </p-dialog>
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .page__header {
      display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1.5rem;
      h1 { font-size: 1.5rem; font-weight: 700; color: $gray-900; }
      p { color: $gray-500; margin-top: 0.25rem; font-size: 0.875rem; }
    }

    .category-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 1rem;
    }

    .category-card {
      display: flex;
      align-items: center;
      gap: 1rem;
      padding: 1.25rem;

      &__icon {
        width: 44px;
        height: 44px;
        border-radius: 10px;
        background: rgba($primary, 0.1);
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;

        .material-symbols-outlined {
          font-size: 22px;
          color: $primary;
        }
      }

      &__body {
        flex: 1;
        min-width: 0;

        h3 {
          font-size: 0.9375rem;
          font-weight: 600;
          color: $text-primary;
          text-transform: capitalize;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      &__id {
        font-size: 0.75rem;
        color: $text-muted;
      }

      &__actions {
        display: flex;
        gap: 0.375rem;
        flex-shrink: 0;
      }
    }

    .btn-sm {
      padding: 0.375rem 0.5rem;
      .material-symbols-outlined { font-size: 18px; }
    }

    .empty-state {
      grid-column: 1 / -1;
      text-align: center;
      padding: 3rem;
      .material-symbols-outlined { font-size: 48px; color: $gray-400; }
      h3 { margin: 1rem 0 0.5rem; color: $text-secondary; }
      p { color: $text-muted; }
    }

    .dialog-form {
      display: flex;
      flex-direction: column;
      gap: 1.25rem;
      padding-top: 0.5rem;
    }

    .dialog-actions {
      display: flex;
      justify-content: flex-end;
      gap: 0.75rem;
      padding-top: 0.5rem;
    }
  `],
})
export class CategoryListComponent implements OnInit {
  readonly categoryStore = inject(CategoryStore);
  private readonly fb = inject(FormBuilder);

  dialogVisible = false;
  editingId = signal<number | null>(null);

  form = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
  });

  ngOnInit(): void {
    this.categoryStore.load();
  }

  openCreate(): void {
    this.editingId.set(null);
    this.form.reset({ name: '' });
    this.dialogVisible = true;
  }

  openEdit(cat: CategoryDto): void {
    this.editingId.set(cat.id!);
    this.form.patchValue({ name: cat.name });
    this.dialogVisible = true;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const name = this.form.getRawValue().name!;
    const id = this.editingId();

    if (id) {
      this.categoryStore.update(id, { name });
    } else {
      this.categoryStore.create({ name });
    }

    this.dialogVisible = false;
    this.form.reset();
  }

  onDelete(cat: CategoryDto): void {
    if (confirm(`"${cat.name}" kategorisi silinecek. Emin misiniz?`)) {
      this.categoryStore.delete(cat.id!);
    }
  }
}

