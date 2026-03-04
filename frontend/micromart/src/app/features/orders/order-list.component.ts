import { Component, OnInit, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OrderResponse, Page } from '../../core/api/api.model';

@Component({
  selector: 'app-order-list',
  standalone: true,
  template: `
    <div class="page">
      <div class="page__header">
        <div>
          <h1>Siparişler</h1>
          <p>Tüm siparişleri görüntüleyin</p>
        </div>
      </div>

      <div class="card">
        <div class="table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th>Sipariş ID</th>
                <th>Kullanıcı ID</th>
                <th>Toplam Tutar</th>
                <th>Durum</th>
              </tr>
            </thead>
            <tbody>
              @for (o of orders(); track o.id) {
                <tr>
                  <td><strong>#{{ o.id }}</strong></td>
                  <td>{{ o.userId }}</td>
                  <td>₺{{ o.totalAmount.toFixed(2) }}</td>
                  <td>
                    <span class="badge"
                          [class.badge--warning]="o.status === 'PENDING'"
                          [class.badge--info]="o.status === 'PREPARED'"
                          [class.badge--success]="o.status === 'CONFIRMED'">
                      {{ statusLabel(o.status) }}
                    </span>
                  </td>
                </tr>
              } @empty {
                <tr><td colspan="4" class="empty-cell">Sipariş bulunamadı.</td></tr>
              }
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .page__header {
      margin-bottom: 1.5rem;
      h1 { font-size: 1.5rem; font-weight: 700; color: $gray-900; }
      p { color: $gray-500; margin-top: 0.25rem; font-size: 0.875rem; }
    }

    .table-wrapper { overflow-x: auto; }

    .data-table {
      width: 100%; border-collapse: collapse;
      th, td { padding: 0.75rem 1rem; text-align: left; font-size: 0.875rem; }
      th { color: $gray-500; font-weight: 600; border-bottom: 2px solid $gray-200; }
      td { border-bottom: 1px solid $gray-100; color: $gray-700; }
      tbody tr:hover { background: $gray-50; }
    }

    .empty-cell { text-align: center; color: $gray-400; padding: 2rem !important; }
  `],
})
export class OrderListComponent implements OnInit {
  orders = signal<OrderResponse[]>([]);

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    // Order service doesn't have a getAll paginated endpoint via gateway yet,
    // so we'll leave this for future implementation.
    // For now, the UI shell is ready.
  }

  statusLabel(status: string): string {
    switch (status) {
      case 'PENDING': return 'Beklemede';
      case 'PREPARED': return 'Hazırlandı';
      case 'CONFIRMED': return 'Onaylandı';
      default: return status;
    }
  }
}



