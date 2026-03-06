import { Injectable, inject } from '@angular/core';
import { MessageService } from 'primeng/api';

export type ToastSeverity = 'success' | 'info' | 'warn' | 'error' | 'secondary' | 'contrast';

export interface ToastOptions {
  summary?: string;
  detail: string;
  life?: number;
  sticky?: boolean;
  key?: string;
}

/**
 * Centralised notification service wrapping PrimeNG MessageService.
 * Inject this in any component/service to show toasts.
 */
@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly messageService = inject(MessageService);

  success(detail: string, summary = 'Başarılı'): void {
    this.show('success', { detail, summary });
  }

  error(detail: string, summary = 'Hata'): void {
    this.show('error', { detail, summary, life: 6000 });
  }

  warn(detail: string, summary = 'Uyarı'): void {
    this.show('warn', { detail, summary });
  }

  info(detail: string, summary = 'Bilgi'): void {
    this.show('info', { detail, summary });
  }

  /**
   * Parses an HTTP error response and shows an appropriate toast.
   */
  httpError(error: any): void {
    const status = error?.status ?? 0;
    const backendMessage =
      error?.error?.message ||
      error?.error?.detail ||
      error?.error?.error ||
      (typeof error?.error === 'string' ? error.error : null);

    let detail: string;
    let summary: string;

    switch (true) {
      case status === 0:
        summary = 'Bağlantı Hatası';
        detail = 'Sunucuya bağlanılamadı. Lütfen internet bağlantınızı kontrol edin.';
        break;
      case status === 400:
        summary = 'Geçersiz İstek';
        detail = backendMessage || 'Gönderilen veriler geçersiz.';
        break;
      case status === 401:
        summary = 'Yetkisiz';
        detail = backendMessage || 'Oturum süresi dolmuş. Lütfen tekrar giriş yapın.';
        break;
      case status === 403:
        summary = 'Erişim Engellendi';
        detail = backendMessage || 'Bu işlem için yetkiniz bulunmuyor.';
        break;
      case status === 404:
        summary = 'Bulunamadı';
        detail = backendMessage || 'İstenen kaynak bulunamadı.';
        break;
      case status === 409:
        summary = 'Çakışma';
        detail = backendMessage || 'Bu işlem mevcut verilerle çakışıyor.';
        break;
      case status === 422:
        summary = 'İşlenemedi';
        detail = backendMessage || 'Gönderilen veriler işlenemedi.';
        break;
      case status >= 500:
        summary = 'Sunucu Hatası';
        detail = backendMessage || 'Sunucuda beklenmeyen bir hata oluştu.';
        break;
      default:
        summary = 'Hata';
        detail = backendMessage || `Beklenmeyen bir hata oluştu. (Kod: ${status})`;
    }

    this.show('error', { detail, summary, life: 8000 });
  }

  clear(): void {
    this.messageService.clear();
  }

  private show(severity: ToastSeverity, opts: ToastOptions): void {
    this.messageService.add({
      severity,
      summary: opts.summary,
      detail: opts.detail,
      life: opts.life ?? 4000,
      sticky: opts.sticky ?? false,
      key: opts.key ?? 'global',
    });
  }
}

