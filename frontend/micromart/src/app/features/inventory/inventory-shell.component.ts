import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WarehouseApiService } from '../../core/api/warehouse-api.service';
import { StockApiService } from '../../core/api/stock-api.service';
import {
  AisleDto, ShelfDto, ProductShelfResponse,
  StockMovementResponse, StockMovementRequest,
  ProductShelfUpdateRequest, Page,
} from '../../core/api/api.model';

@Component({
  selector: 'app-inventory-shell',
  standalone: true,
  imports: [FormsModule],
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
            <form class="inline-form" (ngSubmit)="onCreateAisle()">
              <div class="form-group">
                <label>Depo ID</label>
                <input type="number" class="form-input" [(ngModel)]="newAisle.warehouseId" name="wId" required />
              </div>
              <div class="form-group">
                <label>Koridor Kodu</label>
                <input type="text" class="form-input" [(ngModel)]="newAisle.aisleCode" name="code" required />
              </div>
              <div class="form-group">
                <label>Kategori ID</label>
                <input type="number" class="form-input" [(ngModel)]="newAisle.categoryId" name="catId" required />
              </div>
              <button type="submit" class="btn btn-primary">Ekle</button>
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
            <form class="inline-form" (ngSubmit)="onCreateShelf()">
              <div class="form-group">
                <label>Koridor ID</label>
                <input type="number" class="form-input" [(ngModel)]="newShelf.aisleId" name="aisleId" required />
              </div>
              <div class="form-group">
                <label>Raf Kodu</label>
                <input type="text" class="form-input" [(ngModel)]="newShelf.shelfCode" name="shelfCode" required />
              </div>
              <div class="form-group">
                <label>Kapasite</label>
                <input type="number" class="form-input" [(ngModel)]="newShelf.capacity" name="cap" required />
              </div>
              <button type="submit" class="btn btn-primary">Ekle</button>
            </form>
          }

          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr><th>ID</th><th>Raf Kodu</th><th>Koridor ID</th><th>Kapasite</th><th>Kullanılan</th><th></th></tr>
              </thead>
              <tbody>
                @for (s of shelves(); track s.id) {
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
              <div class="form-group form-group--inline">
                <label>Depo ID:</label>
                <input type="number" class="form-input" [(ngModel)]="stockWarehouseId"
                       name="stockWid" (change)="loadProductShelves()" />
              </div>
            </div>
          </div>

          <!-- Stock update form -->
          <div class="inline-form" style="margin-bottom: 1rem;">
            <div class="form-group">
              <label>Ürün ID</label>
              <input type="number" class="form-input" [(ngModel)]="stockUpdate.productId" name="suPid" />
            </div>
            <div class="form-group">
              <label>Raf ID</label>
              <input type="number" class="form-input" [(ngModel)]="stockUpdate.shelfId" name="suSid" />
            </div>
            <div class="form-group">
              <label>Yeni Miktar</label>
              <input type="number" class="form-input" [(ngModel)]="stockUpdate.newQuantity" name="suQty" />
            </div>
            <button class="btn btn-primary" (click)="onUpdateStock()">
              <span class="material-symbols-outlined">update</span> Güncelle
            </button>
          </div>

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
                  <tr><td colspan="5" class="empty-cell">Veri yok. Depo ID girin.</td></tr>
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
              <div class="form-group form-group--inline">
                <label>Ürün ID:</label>
                <input type="number" class="form-input" [(ngModel)]="movementProductId"
                       name="movPid" (change)="loadMovements()" />
              </div>
            </div>
          </div>

          <!-- New movement form -->
          <form class="inline-form" (ngSubmit)="onAddMovement()" style="margin-bottom: 1rem;">
            <div class="form-group">
              <label>Ürün ID</label>
              <input type="number" class="form-input" [(ngModel)]="newMovement.productId" name="nmPid" required />
            </div>
            <div class="form-group">
              <label>Raf ID</label>
              <input type="number" class="form-input" [(ngModel)]="newMovement.shelfId" name="nmSid" required />
            </div>
            <div class="form-group">
              <label>Yeni Miktar</label>
              <input type="number" class="form-input" [(ngModel)]="newMovement.newQuantity" name="nmQty" required />
            </div>
            <div class="form-group">
              <label>Sebep</label>
              <input type="text" class="form-input" [(ngModel)]="newMovement.reason" name="nmReason" required />
            </div>
            <button type="submit" class="btn btn-primary">
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
                  <tr><td colspan="8" class="empty-cell">Ürün ID girerek hareketleri görüntüleyin.</td></tr>
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

    .form-group--inline { flex-direction: row; align-items: center; gap: 0.5rem; }

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
  activeTab = signal<'aisles' | 'shelves' | 'stock' | 'movements'>('aisles');

  // Aisles
  aisles = signal<AisleDto[]>([]);
  showAisleForm = signal(false);
  newAisle: AisleDto = { warehouseId: 0, aisleCode: '', categoryId: 0 };

  // Shelves
  shelves = signal<ShelfDto[]>([]);
  showShelfForm = signal(false);
  newShelf: ShelfDto = { aisleId: 0, shelfCode: '', capacity: 0, usedCapacity: 0 };

  // Product-Shelf stock
  productShelves = signal<ProductShelfResponse[]>([]);
  stockWarehouseId = 0;
  stockUpdate: ProductShelfUpdateRequest = { productId: 0, shelfId: 0, newQuantity: 0 };

  // Movements
  movements = signal<StockMovementResponse[]>([]);
  movementProductId = 0;
  newMovement: StockMovementRequest = { productId: 0, shelfId: 0, newQuantity: 0, reason: '' };

  constructor(
    private warehouseApi: WarehouseApiService,
    private stockApi: StockApiService,
  ) {}

  ngOnInit(): void {
    this.loadAisles();
    this.loadShelves();
  }

  // ── Aisles ──
  loadAisles(): void {
    this.warehouseApi.getAllAisles(0, 100).subscribe({
      next: (p) => this.aisles.set(p.content),
    });
  }

  onCreateAisle(): void {
    this.warehouseApi.createAisle(this.newAisle).subscribe({
      next: () => {
        this.loadAisles();
        this.newAisle = { warehouseId: 0, aisleCode: '', categoryId: 0 };
        this.showAisleForm.set(false);
      },
    });
  }

  onDeleteAisle(a: AisleDto): void {
    if (confirm(`"${a.aisleCode}" koridoru silinecek. Emin misiniz?`)) {
      this.warehouseApi.deleteAisle(a.id!).subscribe({ next: () => this.loadAisles() });
    }
  }

  // ── Shelves ──
  loadShelves(): void {
    this.warehouseApi.getAllShelves(0, 100).subscribe({
      next: (p) => this.shelves.set(p.content),
    });
  }

  onCreateShelf(): void {
    this.warehouseApi.createShelf(this.newShelf).subscribe({
      next: () => {
        this.loadShelves();
        this.newShelf = { aisleId: 0, shelfCode: '', capacity: 0, usedCapacity: 0 };
        this.showShelfForm.set(false);
      },
    });
  }

  onDeleteShelf(s: ShelfDto): void {
    if (confirm(`"${s.shelfCode}" rafı silinecek. Emin misiniz?`)) {
      this.warehouseApi.deleteShelf(s.id!).subscribe({ next: () => this.loadShelves() });
    }
  }

  // ── Product-Shelf Stock ──
  loadProductShelves(): void {
    if (this.stockWarehouseId > 0) {
      this.stockApi.getByWarehouseId(this.stockWarehouseId, 0, 100).subscribe({
        next: (p) => this.productShelves.set(p.content),
      });
    }
  }

  onUpdateStock(): void {
    this.stockApi.updateStock(this.stockUpdate).subscribe({
      next: () => this.loadProductShelves(),
    });
  }

  // ── Movements ──
  loadMovements(): void {
    if (this.movementProductId > 0) {
      this.stockApi.getMovementsByProduct(this.movementProductId, 0, 50).subscribe({
        next: (p) => this.movements.set(p.content),
      });
    }
  }

  onAddMovement(): void {
    this.stockApi.addMovement(this.newMovement).subscribe({
      next: () => {
        this.movementProductId = this.newMovement.productId;
        this.loadMovements();
        this.newMovement = { productId: 0, shelfId: 0, newQuantity: 0, reason: '' };
      },
    });
  }
}



