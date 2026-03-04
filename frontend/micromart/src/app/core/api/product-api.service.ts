import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, ProductCreateDto, CategoryDto } from './api.model';

@Injectable({ providedIn: 'root' })
export class ProductApiService {
  private readonly productBase = '/api/product';
  private readonly categoryBase = '/api/category';

  constructor(private http: HttpClient) {}

  // ── Product ──
  getAllProducts(page = 0, size = 20): Observable<Page<ProductCreateDto>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<ProductCreateDto>>(`${this.productBase}/all`, { params });
  }

  getProductById(id: number): Observable<ProductCreateDto> {
    return this.http.get<ProductCreateDto>(`${this.productBase}/id/${id}`);
  }

  createProduct(dto: ProductCreateDto): Observable<ProductCreateDto> {
    return this.http.post<ProductCreateDto>(`${this.productBase}/create`, dto);
  }

  updateProduct(id: number, dto: ProductCreateDto): Observable<ProductCreateDto> {
    return this.http.put<ProductCreateDto>(`${this.productBase}/${id}`, dto);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.productBase}/${id}`);
  }

  // ── Category ──
  getAllCategories(page = 0, size = 50): Observable<Page<CategoryDto>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<CategoryDto>>(`${this.categoryBase}/all`, { params });
  }

  getCategoryById(id: number): Observable<CategoryDto> {
    return this.http.get<CategoryDto>(`${this.categoryBase}/id/${id}`);
  }

  createCategory(dto: CategoryDto): Observable<CategoryDto> {
    return this.http.post<CategoryDto>(`${this.categoryBase}/create`, dto);
  }

  updateCategory(id: number, dto: CategoryDto): Observable<CategoryDto> {
    return this.http.put<CategoryDto>(`${this.categoryBase}/${id}`, dto);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.categoryBase}/${id}`);
  }
}

