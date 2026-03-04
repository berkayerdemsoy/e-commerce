import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { WarehouseApiService } from '../../../core/api/warehouse-api.service';
import { WarehouseDto } from '../../../core/api/api.model';

@Component({
  selector: 'app-warehouse-form',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="form-page">
      <div class="breadcrumb">
        <a routerLink="/warehouses">Depolar</a>
        <span class="material-symbols-outlined">chevron_right</span>
        <span>{{ isEdit() ? 'Düzenle' : 'Yeni Depo' }}</span>
      </div>

      <div class="card form-card">
        <h1>{{ isEdit() ? 'Depo Düzenle' : 'Yeni Depo Oluştur' }}</h1>

        @if (errorMsg()) {
          <div class="alert alert--danger">{{ errorMsg() }}</div>
        }

        <form (ngSubmit)="onSubmit()" class="form">
          <div class="form-group">
            <label for="name">Depo Adı</label>
            <input id="name" type="text" class="form-input"
                   [(ngModel)]="warehouse.name" name="name"
                   placeholder="Örn: İstanbul Merkez Depo" required />
          </div>

          <div class="form-group">
            <label for="location">Konum</label>
            <input id="location" type="text" class="form-input"
                   [(ngModel)]="warehouse.location" name="location"
                   placeholder="Örn: İstanbul, Türkiye" required />
          </div>

          <div class="form__actions">
            <button type="button" class="btn btn-secondary" routerLink="/warehouses">İptal</button>
            <button type="submit" class="btn btn-primary" [disabled]="saving()">
              @if (saving()) {
                <span class="material-symbols-outlined spin">progress_activity</span>
              }
              {{ isEdit() ? 'Güncelle' : 'Oluştur' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .breadcrumb {
      display: flex; align-items: center; gap: 0.5rem;
      font-size: 0.875rem; color: $gray-500; margin-bottom: 1rem;
      a { color: $primary; }
      .material-symbols-outlined { font-size: 18px; }
    }

    .form-card {
      max-width: 560px;
      h1 { font-size: 1.25rem; font-weight: 700; color: $gray-900; margin-bottom: 1.5rem; }
    }

    .form { display: flex; flex-direction: column; gap: 1.25rem; }
    .form__actions { display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 0.5rem; }

    .alert {
      padding: 0.75rem 1rem; border-radius: $border-radius; font-size: 0.875rem; margin-bottom: 1rem;
      &--danger { background: rgba($danger, 0.1); color: $danger; border: 1px solid rgba($danger, 0.2); }
    }

    .spin { animation: spin 1s linear infinite; }
    @keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
  `],
})
export class WarehouseFormComponent implements OnInit {
  warehouse: WarehouseDto = { name: '', location: '' };
  isEdit = signal(false);
  saving = signal(false);
  errorMsg = signal('');
  private editId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private api: WarehouseApiService,
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam && idParam !== 'new') {
      this.editId = Number(idParam);
      this.isEdit.set(true);
      this.api.getById(this.editId).subscribe({
        next: (w) => (this.warehouse = { ...w }),
        error: () => this.errorMsg.set('Depo bilgisi yüklenemedi.'),
      });
    }
  }

  onSubmit(): void {
    if (!this.warehouse.name || !this.warehouse.location) {
      this.errorMsg.set('Tüm alanları doldurun.');
      return;
    }

    this.saving.set(true);
    this.errorMsg.set('');

    const op$ = this.isEdit()
      ? this.api.update(this.editId!, this.warehouse)
      : this.api.create(this.warehouse);

    op$.subscribe({
      next: () => this.router.navigate(['/warehouses']),
      error: (err) => {
        this.saving.set(false);
        this.errorMsg.set(err?.error?.message || 'İşlem başarısız.');
      },
    });
  }
}

