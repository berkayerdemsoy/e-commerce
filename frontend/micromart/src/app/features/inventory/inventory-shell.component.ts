import { Component, OnInit, inject, signal, DestroyRef } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Select } from 'primeng/select';
import { WarehouseApiService } from '../../core/api/warehouse-api.service';
import { StockApiService } from '../../core/api/stock-api.service';
import { ProductApiService } from '../../core/api/product-api.service';
import { WarehouseStore } from '../../core/store/warehouse.store';
import { CategoryStore } from '../../core/store/category.store';
import { InventoryStore } from '../../core/store/inventory.store';
import { NotificationService } from '../../core/notification/notification.service';
import {
  AisleDto, ShelfDto, ProductShelfResponse,
  StockMovementResponse, StockMovementRequest,
  ProductShelfUpdateRequest, ProductCreateDto,
} from '../../core/api/api.model';

@Component({
  selector: 'app-inventory-shell',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, Select],
  template: `
    <div class="page">
      <div class="page__header">
        <div>
          <h1>Envanter Yönetimi</h1>
          <p>Koridorlar, raflar, stok hareketleri ve ürün yerleşimi</p>
        </div>
      </div>

      <!-- Tabs -->
      <div class="tabs">
        <button class="tab" [class.tab--active]="activeTab() === 'aisles'" (click)="activeTab.set('aisles')">
          <span class="material-symbols-outlined">view_column</span> Koridorlar
        </button>
        <button class="tab" [class.tab--active]="activeTab() === 'shelves'" (click)="activeTab.set('shelves')">
          <span class="material-symbols-outlined">shelves</span> Raflar
        </button>
        <button class="tab" [class.tab--active]="activeTab() === 'stock'" (click)="activeTab.set('stock')">
          <span class="material-symbols-outlined">inventory_2</span> Ürün-Raf Stokları
        </button>
        <button class="tab" [class.tab--active]="activeTab() === 'movements'" (click)="activeTab.set('movements')">
          <span class="material-symbols-outlined">swap_vert</span> Stok Hareketleri
        </button>
      </div>

      <!-- ═══ AISLES TAB ═══ -->
      @if (activeTab() === 'aisles') {
        <div class="card section">
          <div class="section__header">
            <h2>Koridorlar</h2>
            <button class="btn btn-primary" (click)="showAisleForm.set(!showAisleForm())">
              <span class="material-symbols-outlined">{{ showAisleForm() ? 'close' : 'add' }}</span>
              {{ showAisleForm() ? 'Kapat' : 'Yeni Koridor' }}
            </button>
          </div>

          @if (showAisleForm()) {
            <form [formGroup]="aisleForm" (ngSubmit)="onCreateAisle()" class="inline-form">
              <div class="form-group" style="min-width: 200px;">
                <label>Depo</label>
                <p-select
                  formControlName="warehouseId"
                  [options]="warehouseStore.warehouses()"
                  optionLabel="name"
                  optionValue="id"
                  placeholder="Depo seçin..."
                  [filter]="true"
                  filterPlaceholder="Ara..."
                  [showClear]="true"
                  styleClass="w-full" />
              </div>
              <div class="form-group" style="min-width: 160px;">
                <label>Kategori</label>
                <p-select
                  formControlName="categoryId"
                  [options]="categoryStore.categories()"
                  optionLabel="name"
                  optionValue="id"
                  placeholder="Kategori seçin..."
                  [filter]="true"
                  filterPlaceholder="Ara..."
                  [showClear]="true"
                  styleClass="w-full" />
              </div>
              <div class="form-group">
                <label>Koridor Kodu</label>
                <input type="text" class="form-input" formControlName="aisleCode" placeholder="Örn: A-01" />
              </div>
              <button type="submit" class="btn btn-primary" [disabled]="aisleForm.invalid">
                <span class="material-symbols-outlined">add</span> Ekle
              </button>
            </form>
          }

          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr><th>ID</th><th>Koridor Kodu</th><th>Depo ID</th><th>Kategori ID</th><th></th></tr>
              </thead>
              <tbody>
                @for (a of aisles(); track a.id) {
                  <tr>
                    <td>{{ a.id }}</td>
                    <td><strong>{{ a.aisleCode }}</strong></td>
                    <td>{{ a.warehouseId }}</td>
                    <td>{{ a.categoryId }}</td>
                    <td>
                      <button class="btn btn-danger btn-sm" (click)="onDeleteAisle(a)">
                        <span class="material-symbols-outlined">delete</span>
                      </button>
                    </td>
                  </tr>
                } @empty {
                  <tr><td colspan="5" class="empty-cell">Koridor bulunamadı.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }

      <!-- ═══ SHELVES TAB ═══ -->
      @if (activeTab() === 'shelves') {
        <div class="card section">
          <div class="section__header">
            <h2>Raflar</h2>
            <button class="btn btn-primary" (click)="showShelfForm.set(!showShelfForm())">
              <span class="material-symbols-outlined">{{ showShelfForm() ? 'close' : 'add' }}</span>
              {{ showShelfForm() ? 'Kapat' : 'Yeni Raf' }}
            </button>
          </div>

          @if (showShelfForm()) {
            <form [formGroup]="shelfForm" (ngSubmit)="onCreateShelf()" class="inline-form">
              <div class="form-group" style="min-width: 200px;">
                <label>Depo</label>
                <p-select
                  formControlName="warehouseId"
                  [options]="warehouseStore.warehouses()"
                  optionLabel="name"
                  optionValue="id"
                  placeholder="Önce depo seçin..."
                  [filter]="true"
                  filterPlaceholder="Ara..."
                  [showClear]="true"
                  styleClass="w-full" />
              </div>
              <div class="form-group" style="min-width: 200px;">
                <label>Koridor</label>
                <p-select
                  formControlName="aisleId"
                  [options]="inventoryStore.aisles()"
                  optionLabel="aisleCode"
                  optionValue="id"
                  placeholder="Koridor seçin..."
                  [filter]="true"
                  filterPlaceholder="Ara..."
                  [disabled]="!shelfForm.get('warehouseId')?.value"
                  [loading]="inventoryStore.aislesLoading()"
                  [showClear]="true"
                  styleClass="w-full" />
              </div>
              <div class="form-group">
                <label>Raf Kodu</label>
                <input type="text" class="form-input" formControlName="shelfCode" placeholder="Örn: S-01" />
              </div>
              <div class="form-group">
                <label>Kapasite</label>
                <input type="number" class="form-input" formControlName="capacity" placeholder="100" />
              </div>
              <button type="submit" class="btn btn-primary" [disabled]="shelfForm.invalid">
                <span class="material-symbols-outlined">add</span> Ekle
              </button>
            </form>
          }

          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr><th>ID</th><th>Raf Kodu</th><th>Koridor ID</th><th>Kapasite</th><th>Kullanılan</th><th></th></tr>
              </thead>
              <tbody>
                @for (s of allShelves(); track s.id) {
                  <tr>
                    <td>{{ s.id }}</td>
                    <td><strong>{{ s.shelfCode }}</strong></td>
                    <td>{{ s.aisleId }}</td>
                    <td>{{ s.capacity }}</td>
                    <td>{{ s.usedCapacity }}</td>
                    <td>
                      <button class="btn btn-danger btn-sm" (click)="onDeleteShelf(s)">
                        <span class="material-symbols-outlined">delete</span>
                      </button>
                    </td>
                  </tr>
                } @empty {
                  <tr><td colspan="6" class="empty-cell">Raf bulunamadı.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }

      <!-- ═══ PRODUCT-SHELF STOCK TAB ═══ -->
      @if (activeTab() === 'stock') {
        <div class="card section">
          <div class="section__header">
            <h2>Ürün-Raf Stokları</h2>
            <div class="section__controls">
              <div class="form-group" style="min-width: 220px;">
                <label>Depo</label>
                <p-select
                  [options]="warehouseStore.warehouses()"
                  optionLabel="name"
                  optionValue="id"
                  placeholder="Depo seçin..."
                  [filter]="true"
                  filterPlaceholder="Ara..."
                  [(ngModel)]="stockWarehouseId"
                  (onChange)="loadProductShelves()"
                  styleClass="w-full" />
              </div>
            </div>
          </div>

          <form [formGroup]="stockUpdateForm" (ngSubmit)="onUpdateStock()" class="inline-form">
            <div class="form-group" style="min-width: 200px;">
              <label>Ürün</label>
              <p-select
                formControlName="productId"
                [options]="products()"
                optionLabel="name"
                optionValue="id"
                placeholder="Ürün seçin..."
                [filter]="true"
                filterPlaceholder="Ara..."
                styleClass="w-full" />
            </div>
            <div class="form-group" style="min-width: 200px;">
              <label>Raf</label>
              <p-select
                formControlName="shelfId"
                [options]="stockShelfOptions()"
                optionLabel="shelfCode"
                optionValue="id"
                placeholder="Raf seçin..."
                [filter]="true"
                filterPlaceholder="Ara..."
                styleClass="w-full" />
            </div>
            <div class="form-group">
              <label>Yeni Miktar</label>
              <input type="number" class="form-input" formControlName="newQuantity" />
            </div>
            <button type="submit" class="btn btn-primary" [disabled]="stockUpdateForm.invalid">
              <span class="material-symbols-outlined">update</span> Güncelle
            </button>
          </form>

          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr><th>Ürün ID</th><th>Raf ID</th><th>Miktar</th><th>Min. Stok</th><th>Durum</th></tr>
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
                } @empty {
                  <tr><td colspan="5" class="empty-cell">Veri yok. Depo seçin.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }

      <!-- ═══ STOCK MOVEMENTS TAB ═══ -->
      @if (activeTab() === 'movements') {
        <div class="card section">
          <div class="section__header">
            <h2>Stok Hareketleri</h2>
            <div class="section__controls">
              <div class="form-group" style="min-width: 220px;">
                <label>Ürün</label>
                <p-select
                  [options]="products()"
                  optionLabel="name"
                  optionValue="id"
                  placeholder="Ürün seçin..."
                  [filter]="true"
                  filterPlaceholder="Ara..."
                  [(ngModel)]="movementProductId"
                  (onChange)="loadMovements()"
                  styleClass="w-full" />
              </div>
            </div>
          </div>

          <form [formGroup]="movementForm" (ngSubmit)="onAddMovement()" class="inline-form">
            <div class="form-group" style="min-width: 200px;">
              <label>Ürün</label>
              <p-select
                formControlName="productId"
                [options]="products()"
                optionLabel="name"
                optionValue="id"
                placeholder="Ürün seçin..."
                [filter]="true"
                filterPlaceholder="Ara..."
                styleClass="w-full" />
            </div>
            <div class="form-group" style="min-width: 200px;">
              <label>Depo</label>
              <p-select
                formControlName="movWarehouseId"
                [options]="warehouseStore.warehouses()"
                optionLabel="name"
                optionValue="id"
                placeholder="Depo seçin..."
                [filter]="true"
                filterPlaceholder="Ara..."
                styleClass="w-full" />
            </div>
            <div class="form-group" style="min-width: 200px;">
              <label>Koridor</label>
              <p-select
                formControlName="movAisleId"
                [options]="movAisles()"
                optionLabel="aisleCode"
                optionValue="id"
                placeholder="Koridor seçin..."
                [disabled]="!movementForm.get('movWarehouseId')?.value"
                [loading]="movAislesLoading()"
                [filter]="true"
                filterPlaceholder="Ara..."
                styleClass="w-full" />
            </div>
            <div class="form-group" style="min-width: 200px;">
              <label>Raf</label>
              <p-select
                formControlName="shelfId"
                [options]="movShelves()"
                optionLabel="shelfCode"
                optionValue="id"
                placeholder="Raf seçin..."
                [disabled]="!movementForm.get('movAisleId')?.value"
                [loading]="movShelvesLoading()"
                [filter]="true"
                filterPlaceholder="Ara..."
                styleClass="w-full" />
            </div>
            <div class="form-group">
              <label>Yeni Miktar</label>
              <input type="number" class="form-input" formControlName="newQuantity" />
            </div>
            <div class="form-group">
              <label>Sebep</label>
              <input type="text" class="form-input" formControlName="reason" placeholder="Hareket sebebi" />
            </div>
            <button type="submit" class="btn btn-primary"
                    [disabled]="!movementForm.get('productId')?.value || !movementForm.get('shelfId')?.value || !movementForm.get('reason')?.value">
              <span class="material-symbols-outlined">add</span> Ekle
            </button>
          </form>

          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr><th>ID</th><th>Tür</th><th>Ürün</th><th>Raf</th><th>Önceki</th><th>Yeni</th><th>Sebep</th><th>Tarih</th></tr>
              </thead>
              <tbody>
                @for (m of movements(); track m.id) {
                  <tr>
                    <td>{{ m.id }}</td>
                    <td>
                      <span class="badge"
                            [class.badge--success]="m.movementType === 'INBOUND'"
                            [class.badge--danger]="m.movementType === 'OUTBOUND'"
                            [class.badge--warning]="m.movementType === 'ADJUSTMENT'"
                            [class.badge--info]="m.movementType === 'TRANSFER'">
                        {{ m.movementType }}
                      </span>
                    </td>
                    <td>{{ m.productId }}</td>
                    <td>{{ m.shelfId }}</td>
                    <td>{{ m.previousQuantity }}</td>
                    <td>{{ m.newQuantity }}</td>
                    <td>{{ m.reason }}</td>
                    <td>{{ m.createdAt }}</td>
                  </tr>
                } @empty {
                  <tr><td colspan="8" class="empty-cell">Ürün seçerek hareketleri görüntüleyin.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .page__header {
      margin-bottom: 1.5rem;
      h1 { font-size: 1.5rem; font-weight: 700; color: $gray-900; }
      p { color: $gray-500; margin-top: 0.25rem; font-size: 0.875rem; }
    }

    .tabs {
      display: flex; gap: 0.25rem; margin-bottom: 1.5rem;
      border-bottom: 2px solid $gray-200; padding-bottom: 0;
    }

    .tab {
      display: inline-flex; align-items: center; gap: 0.5rem;
      padding: 0.75rem 1.25rem;
      border: none; background: transparent;
      font-family: inherit; font-size: 0.875rem; font-weight: 500;
      color: $gray-500; cursor: pointer;
      border-bottom: 2px solid transparent;
      margin-bottom: -2px;
      transition: all $transition-fast;

      &:hover { color: $gray-700; }

      &--active {
        color: $primary;
        border-bottom-color: $primary;
      }

      .material-symbols-outlined { font-size: 20px; }
    }

    .section {
      &__header {
        display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;
        h2 { font-size: 1.125rem; font-weight: 600; color: $gray-800; }
      }
      &__controls { display: flex; align-items: flex-end; gap: 0.75rem; }
    }

    .inline-form {
      display: flex; flex-wrap: wrap; gap: 1rem; align-items: flex-end;
      padding: 1rem; background: $gray-50; border-radius: $border-radius;
      margin-bottom: 1rem;
    }

    .table-wrapper { overflow-x: auto; }

    .data-table {
      width: 100%; border-collapse: collapse;
      th, td { padding: 0.625rem 0.75rem; text-align: left; font-size: 0.8125rem; }
      th { color: $gray-500; font-weight: 600; border-bottom: 2px solid $gray-200; }
      td { border-bottom: 1px solid $gray-100; color: $gray-700; }
      tbody tr:hover { background: $gray-50; }
    }

    .empty-cell { text-align: center; color: $gray-400; padding: 2rem !important; }

    .btn-sm { padding: 0.375rem 0.5rem; .material-symbols-outlined { font-size: 18px; } }
  `],
})
export class InventoryShellComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly warehouseApi = inject(WarehouseApiService);
  private readonly stockApi = inject(StockApiService);
  private readonly productApi = inject(ProductApiService);
  private readonly notify = inject(NotificationService);
  private readonly destroyRef = inject(DestroyRef);

  readonly warehouseStore = inject(WarehouseStore);
  readonly categoryStore = inject(CategoryStore);
  readonly inventoryStore = inject(InventoryStore);

  activeTab = signal<'aisles' | 'shelves' | 'stock' | 'movements'>('aisles');

  // ── Aisles ──
  aisles = signal<AisleDto[]>([]);
  showAisleForm = signal(false);
  aisleForm = this.fb.group({
    warehouseId: [null as number | null, Validators.required],
    aisleCode: ['', Validators.required],
    categoryId: [null as number | null, Validators.required],
  });

  // ── Shelves ──
  allShelves = signal<ShelfDto[]>([]);
  showShelfForm = signal(false);
  shelfForm = this.fb.group({
    warehouseId: [null as number | null, Validators.required],
    aisleId: [null as number | null, Validators.required],
    shelfCode: ['', Validators.required],
    capacity: [0, [Validators.required, Validators.min(1)]],
  });

  // ── Product-Shelf stock ──
  productShelves = signal<ProductShelfResponse[]>([]);
  products = signal<ProductCreateDto[]>([]);
  stockShelfOptions = signal<ShelfDto[]>([]);
  stockWarehouseId: number | null = null;
  stockUpdateForm = this.fb.group({
    productId: [null as number | null, Validators.required],
    shelfId: [null as number | null, Validators.required],
    newQuantity: [0, Validators.required],
  });

  // ── Movements ──
  movements = signal<StockMovementResponse[]>([]);
  movementProductId: number | null = null;
  movAisles = signal<AisleDto[]>([]);
  movAislesLoading = signal(false);
  movShelves = signal<ShelfDto[]>([]);
  movShelvesLoading = signal(false);

  movementForm = this.fb.group({
    productId: [null as number | null, Validators.required],
    movWarehouseId: [null as number | null],
    movAisleId: [null as number | null],
    shelfId: [null as number | null, Validators.required],
    newQuantity: [0, Validators.required],
    reason: ['', Validators.required],
  });

  ngOnInit(): void {
    this.warehouseStore.load();
    this.categoryStore.load();
    this.loadAisles();
    this.loadAllShelves();
    this.loadProducts();

    // Shelf form: warehouse → load aisles cascade
    this.shelfForm.get('warehouseId')!.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((warehouseId) => {
        this.shelfForm.patchValue({ aisleId: null });
        if (warehouseId) {
          this.inventoryStore.loadAislesByWarehouse(warehouseId);
        } else {
          this.inventoryStore.reset();
        }
      });

    // Movement form: warehouse → aisles cascade
    this.movementForm.get('movWarehouseId')!.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((warehouseId) => {
        this.movementForm.patchValue({ movAisleId: null, shelfId: null });
        this.movShelves.set([]);
        if (warehouseId) {
          this.movAislesLoading.set(true);
          this.warehouseApi.getAislesByWarehouse(warehouseId).subscribe({
            next: (list) => { this.movAisles.set(list); this.movAislesLoading.set(false); },
            error: () => this.movAislesLoading.set(false),
          });
        } else {
          this.movAisles.set([]);
        }
      });

    // Movement form: aisle → shelves cascade
    this.movementForm.get('movAisleId')!.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((aisleId) => {
        this.movementForm.patchValue({ shelfId: null });
        if (aisleId) {
          this.movShelvesLoading.set(true);
          this.warehouseApi.getShelvesByAisle(aisleId).subscribe({
            next: (list) => { this.movShelves.set(list); this.movShelvesLoading.set(false); },
            error: () => this.movShelvesLoading.set(false),
          });
        } else {
          this.movShelves.set([]);
        }
      });
  }

  // ── Aisles ──
  loadAisles(): void {
    this.warehouseApi.getAllAisles(0, 200).subscribe({
      next: (p) => this.aisles.set(p.content),
    });
  }

  onCreateAisle(): void {
    const val = this.aisleForm.getRawValue();
    const dto: AisleDto = { warehouseId: val.warehouseId!, aisleCode: val.aisleCode!, categoryId: val.categoryId! };
    this.warehouseApi.createAisle(dto).subscribe({
      next: () => {
        this.loadAisles();
        this.aisleForm.reset();
        this.showAisleForm.set(false);
        this.notify.success('Koridor başarıyla oluşturuldu.');
      },
    });
  }

  onDeleteAisle(a: AisleDto): void {
    if (confirm(`"${a.aisleCode}" koridoru silinecek. Emin misiniz?`)) {
      this.warehouseApi.deleteAisle(a.id!).subscribe({
        next: () => { this.loadAisles(); this.notify.success('Koridor silindi.'); },
      });
    }
  }

  // ── Shelves ──
  loadAllShelves(): void {
    this.warehouseApi.getAllShelves(0, 200).subscribe({
      next: (p) => this.allShelves.set(p.content),
    });
  }

  onCreateShelf(): void {
    const val = this.shelfForm.getRawValue();
    const dto: ShelfDto = { aisleId: val.aisleId!, shelfCode: val.shelfCode!, capacity: val.capacity!, usedCapacity: 0 };
    this.warehouseApi.createShelf(dto).subscribe({
      next: () => {
        this.loadAllShelves();
        this.shelfForm.reset();
        this.showShelfForm.set(false);
        this.notify.success('Raf başarıyla oluşturuldu.');
      },
    });
  }

  onDeleteShelf(s: ShelfDto): void {
    if (confirm(`"${s.shelfCode}" rafı silinecek. Emin misiniz?`)) {
      this.warehouseApi.deleteShelf(s.id!).subscribe({
        next: () => { this.loadAllShelves(); this.notify.success('Raf silindi.'); },
      });
    }
  }

  // ── Products (for dropdowns) ──
  loadProducts(): void {
    this.productApi.getAllProducts(0, 500).subscribe({
      next: (p) => this.products.set(p.content),
    });
  }

  // ── Product-Shelf Stock ──
  loadProductShelves(): void {
    if (this.stockWarehouseId && this.stockWarehouseId > 0) {
      this.stockApi.getByWarehouseId(this.stockWarehouseId, 0, 200).subscribe({
        next: (p) => this.productShelves.set(p.content),
      });
      this.warehouseApi.getAllShelves(0, 200).subscribe({
        next: (p) => this.stockShelfOptions.set(p.content),
      });
    }
  }

  onUpdateStock(): void {
    const val = this.stockUpdateForm.getRawValue();
    const req: ProductShelfUpdateRequest = { productId: val.productId!, shelfId: val.shelfId!, newQuantity: val.newQuantity! };
    this.stockApi.updateStock(req).subscribe({
      next: () => { this.loadProductShelves(); this.stockUpdateForm.reset(); this.notify.success('Stok güncellendi.'); },
    });
  }

  // ── Movements ──
  loadMovements(): void {
    if (this.movementProductId && this.movementProductId > 0) {
      this.stockApi.getMovementsByProduct(this.movementProductId, 0, 50).subscribe({
        next: (p) => this.movements.set(p.content),
      });
    }
  }

  onAddMovement(): void {
    const val = this.movementForm.getRawValue();
    const req: StockMovementRequest = { productId: val.productId!, shelfId: val.shelfId!, newQuantity: val.newQuantity!, reason: val.reason! };
    this.stockApi.addMovement(req).subscribe({
      next: () => {
        this.movementProductId = val.productId;
        this.loadMovements();
        this.movementForm.reset();
        this.notify.success('Stok hareketi eklendi.');
      },
    });
  }
}


