import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Page,
  WarehouseDto,
  WarehouseSummaryDto,
  AisleDto,
  ShelfDto,
  WarehouseAssignmentRequest,
  WarehouseAssignmentDto,
  WarehouseCategoryDto,
  WarehouseCategoryRequest,
  WarehouseRole,
} from './api.model';

@Injectable({ providedIn: 'root' })
export class WarehouseApiService {
  private readonly base = '/api/warehouse';
  private readonly aisleBase = '/api/aisles';
  private readonly shelfBase = '/api/shelves';
  private readonly assignmentBase = '/api/assignments';
  private readonly categoryBase = '/api/warehouse-categories';

  constructor(private http: HttpClient) {}

  // ── Warehouse CRUD ──
  getAll(page = 0, size = 10): Observable<Page<WarehouseDto>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<WarehouseDto>>(`${this.base}/all`, { params });
  }

  getById(id: number): Observable<WarehouseDto> {
    return this.http.get<WarehouseDto>(`${this.base}/id/${id}`);
  }

  getByName(name: string): Observable<WarehouseDto> {
    return this.http.get<WarehouseDto>(`${this.base}/name/${name}`);
  }

  create(dto: WarehouseDto): Observable<WarehouseDto> {
    return this.http.post<WarehouseDto>(`${this.base}/create`, dto);
  }

  update(id: number, dto: WarehouseDto): Observable<WarehouseDto> {
    return this.http.put<WarehouseDto>(`${this.base}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getSummary(id: number): Observable<WarehouseSummaryDto> {
    return this.http.get<WarehouseSummaryDto>(`${this.base}/${id}/summary`);
  }

  // ── Aisle CRUD ──
  getAllAisles(page = 0, size = 20): Observable<Page<AisleDto>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<AisleDto>>(`${this.aisleBase}/all`, { params });
  }

  /** Filtered: aisles belonging to a specific warehouse */
  getAislesByWarehouse(warehouseId: number): Observable<AisleDto[]> {
    return this.http.get<AisleDto[]>(`${this.base}/${warehouseId}/aisles`);
  }

  getAisleById(id: number): Observable<AisleDto> {
    return this.http.get<AisleDto>(`${this.aisleBase}/id/${id}`);
  }

  createAisle(dto: AisleDto): Observable<AisleDto> {
    return this.http.post<AisleDto>(`${this.aisleBase}/create`, dto);
  }

  updateAisle(id: number, dto: AisleDto): Observable<AisleDto> {
    return this.http.put<AisleDto>(`${this.aisleBase}/update/${id}`, dto);
  }

  deleteAisle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.aisleBase}/${id}`);
  }

  // ── Shelf CRUD ──
  getAllShelves(page = 0, size = 20): Observable<Page<ShelfDto>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<ShelfDto>>(`${this.shelfBase}/all`, { params });
  }

  /** Filtered: shelves belonging to a specific aisle */
  getShelvesByAisle(aisleId: number): Observable<ShelfDto[]> {
    return this.http.get<ShelfDto[]>(`${this.aisleBase}/${aisleId}/shelves`);
  }

  getShelfById(id: number): Observable<ShelfDto> {
    return this.http.get<ShelfDto>(`${this.shelfBase}/id/${id}`);
  }

  createShelf(dto: ShelfDto): Observable<ShelfDto> {
    return this.http.post<ShelfDto>(`${this.shelfBase}/create`, dto);
  }

  updateShelf(id: number, dto: ShelfDto): Observable<ShelfDto> {
    return this.http.put<ShelfDto>(`${this.shelfBase}/update/${id}`, dto);
  }

  deleteShelf(id: number): Observable<void> {
    return this.http.delete<void>(`${this.shelfBase}/${id}`);
  }

  // ── Assignment ──
  assignAdmin(req: WarehouseAssignmentRequest): Observable<WarehouseAssignmentDto> {
    return this.http.post<WarehouseAssignmentDto>(`${this.assignmentBase}/admin`, req);
  }

  assignManager(req: WarehouseAssignmentRequest): Observable<WarehouseAssignmentDto> {
    return this.http.post<WarehouseAssignmentDto>(`${this.assignmentBase}/manager`, req);
  }

  removeAssignment(warehouseId: number, userId: number, role: WarehouseRole): Observable<void> {
    return this.http.delete<void>(`${this.assignmentBase}/${warehouseId}/user/${userId}/role/${role}`);
  }

  // ── Warehouse Category ──
  assignCategory(req: WarehouseCategoryRequest): Observable<WarehouseCategoryDto> {
    return this.http.post<WarehouseCategoryDto>(`${this.categoryBase}/assign`, req);
  }

  removeCategory(warehouseId: number, categoryId: number): Observable<WarehouseCategoryDto> {
    return this.http.delete<WarehouseCategoryDto>(`${this.categoryBase}/remove/${warehouseId}/${categoryId}`);
  }

  getCategoriesByWarehouse(warehouseId: number): Observable<WarehouseCategoryDto[]> {
    return this.http.get<WarehouseCategoryDto[]>(`${this.categoryBase}/${warehouseId}`);
  }
}

