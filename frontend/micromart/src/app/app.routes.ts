import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  // ── Public ──
  {
    path: 'login',
    loadComponent: () =>
      import('./features/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/register/register.component').then((m) => m.RegisterComponent),
  },

  // ── Authenticated (Layout wrapper) ──
  {
    path: '',
    loadComponent: () =>
      import('./core/layout/layout.component').then((m) => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent),
      },

      // Warehouse
      {
        path: 'warehouses',
        loadComponent: () =>
          import('./features/warehouse/warehouse-list/warehouse-list.component').then(
            (m) => m.WarehouseListComponent,
          ),
      },
      {
        path: 'warehouses/new',
        loadComponent: () =>
          import('./features/warehouse/warehouse-form/warehouse-form.component').then(
            (m) => m.WarehouseFormComponent,
          ),
      },
      {
        path: 'warehouses/:id/edit',
        loadComponent: () =>
          import('./features/warehouse/warehouse-form/warehouse-form.component').then(
            (m) => m.WarehouseFormComponent,
          ),
      },
      {
        path: 'warehouses/:id',
        loadComponent: () =>
          import('./features/warehouse/warehouse-detail/warehouse-detail.component').then(
            (m) => m.WarehouseDetailComponent,
          ),
      },

      // Inventory
      {
        path: 'inventory',
        loadComponent: () =>
          import('./features/inventory/inventory-shell.component').then(
            (m) => m.InventoryShellComponent,
          ),
      },

      // Products
      {
        path: 'products',
        loadComponent: () =>
          import('./features/products/product-list/product-list.component').then(
            (m) => m.ProductListComponent,
          ),
      },

      // Categories
      {
        path: 'categories',
        loadComponent: () =>
          import('./features/categories/category-list.component').then(
            (m) => m.CategoryListComponent,
          ),
      },

      // Orders
      {
        path: 'orders',
        loadComponent: () =>
          import('./features/orders/order-list.component').then(
            (m) => m.OrderListComponent,
          ),
      },

      // Users (Admin only)
      {
        path: 'users',
        loadComponent: () =>
          import('./features/users/user-list.component').then(
            (m) => m.UserListComponent,
          ),
        canActivate: [roleGuard],
        data: { roles: ['ROLE_ADMIN'] },
      },

      // Default redirect
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },

  // Fallback
  { path: '**', redirectTo: 'login' },
];
