// ──────────────────────────────────────
// Shared API Models (mirrors backend DTOs)
// ──────────────────────────────────────

// ── Pagination ──
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;          // current page (0-based)
  first: boolean;
  last: boolean;
}

// ── Warehouse ──
export interface WarehouseDto {
  id?: number;
  name: string;
  location: string;
}

export interface WarehouseSummaryDto {
  warehouseId: number;
  warehouseName: string;
  location: string;
  totalProducts: number;
  totalAisles: number;
  totalShelves: number;
  lowStockAlertCount: number;
  totalStockQuantity: number;
}

// ── Aisle ──
export interface AisleDto {
  id?: number;
  warehouseId: number;
  aisleCode: string;
  categoryId: number;
}

// ── Shelf ──
export interface ShelfDto {
  id?: number;
  aisleId: number;
  shelfCode: string;
  capacity: number;
  usedCapacity: number;
}

// ── Product ──
export interface ProductCreateDto {
  id?: number;
  name: string;
  description: string;
  price: number;
  category_id: number;
}

// ── Category ──
export interface CategoryDto {
  id?: number;
  name: string;
}

// ── Product-Shelf ──
export interface ProductShelfResponse {
  productId: number;
  shelfId: number;
  quantity: number;
  minStockLevel: number;
}

export interface ProductShelfFilterRequest {
  warehouseId: number;
  shelfId: number;
  productId: number;
  initialQuantity: number;
  minQuantity: number;
  maxQuantity: number;
}

export interface ProductShelfUpdateRequest {
  shelfId: number;
  productId: number;
  newQuantity: number;
}

// ── Stock Movement ──
export type MovementType = 'INBOUND' | 'OUTBOUND' | 'ADJUSTMENT' | 'TRANSFER';

export interface StockMovementRequest {
  productId: number;
  shelfId: number;
  newQuantity: number;
  reason: string;
}

export interface StockMovementResponse {
  id: number;
  productId: number;
  shelfId: number;
  movementType: MovementType;
  previousQuantity: number;
  newQuantity: number;
  reason: string;
  createdAt: string;
}

// ── Warehouse Assignment ──
export type WarehouseRole = 'WAREHOUSE_MANAGER' | 'WAREHOUSE_ADMIN';

export interface WarehouseAssignmentDto {
  id: number;
  warehouseId: number;
  userId: number;
  role: WarehouseRole;
  assignedAt: string;
}

export interface WarehouseAssignmentRequest {
  userId: number;
  warehouseId: number;
}

// ── Warehouse Category ──
export interface WarehouseCategoryDto {
  id: number;
  warehouseId: number;
  categoryId: number;
  assignedAt: string;
  assignedBy: number;
  isActive: boolean;
}

export interface WarehouseCategoryRequest {
  warehouseId: number;
  categoryId: number;
}

// ── Order ──
export type StatusType = 'PENDING' | 'PREPARED' | 'CONFIRMED';

export interface OrderResponse {
  id: number;
  userId: number;
  totalAmount: number;
  status: StatusType;
}

// ── Cart ──
export interface CartItemDTO {
  productId: string;
  productName: string;
  price: number;
  quantity: number;
}

export interface CartDTO {
  userId: string;
  items: CartItemDTO[];
  updatedAt: string;
}

