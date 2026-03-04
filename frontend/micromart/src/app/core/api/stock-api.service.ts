import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Page,
  ProductShelfResponse,
  ProductShelfFilterRequest,
  ProductShelfUpdateRequest,
  StockMovementRequest,
  StockMovementResponse,
} from './api.model';

@Injectable({ providedIn: 'root' })
export class StockApiService {
  private readonly productShelfBase = '/api/product-shelves';
  private readonly stockMovementBase = '/api/stock-movements';

  constructor(private http: HttpClient) {}

  // ── Product-Shelf ──
  assignProduct(req: ProductShelfFilterRequest): Observable<ProductShelfResponse> {
    return this.http.post<ProductShelfResponse>(`${this.productShelfBase}/assign`, req);
  }

  updateStock(req: ProductShelfUpdateRequest): Observable<ProductShelfResponse> {
    return this.http.put<ProductShelfResponse>(`${this.productShelfBase}/update-stock`, req);
  }

  getLowStockAlerts(warehouseId: number, page = 0, size = 20): Observable<Page<ProductShelfResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<ProductShelfResponse>>(
      `${this.productShelfBase}/low-stock/${warehouseId}`,
      { params },
    );
  }

  getByWarehouseId(warehouseId: number, page = 0, size = 20): Observable<Page<ProductShelfResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<ProductShelfResponse>>(
      `${this.productShelfBase}/warehouse/${warehouseId}`,
      { params },
    );
  }

  // ── Stock Movement ──
  addMovement(req: StockMovementRequest): Observable<StockMovementResponse> {
    return this.http.post<StockMovementResponse>(this.stockMovementBase, req);
  }

  getMovementsByProduct(productId: number, page = 0, size = 20): Observable<Page<StockMovementResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<StockMovementResponse>>(
      `${this.stockMovementBase}/${productId}`,
      { params },
    );
  }
}

