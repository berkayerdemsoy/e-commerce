import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Select } from 'primeng/select';
import { AuthService } from '../../core/auth/auth.service';
import { NotificationService } from '../../core/notification/notification.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, Select],
  template: `
    <div class="register-page">
      <div class="register-card">
        <!-- Header -->
        <div class="register-header">
          <span class="material-symbols-outlined register-logo">warehouse</span>
          <h1>MicroMart</h1>
          <p>Yeni hesap oluşturun</p>
        </div>

        <!-- Form -->
        <form [formGroup]="form" (ngSubmit)="onRegister()" class="register-form">
          <!-- Row 1: Username + Email -->
          <div class="form-row">
            <div class="form-group">
              <label for="reg-username">Kullanıcı Adı</label>
              <input id="reg-username" type="text" class="form-input"
                     formControlName="username" placeholder="kullanici_adi" />
              @if (form.get('username')?.touched && form.get('username')?.hasError('required')) {
                <span class="form-error">Zorunlu alan</span>
              }
              @if (form.get('username')?.touched && form.get('username')?.hasError('minlength')) {
                <span class="form-error">En az 3 karakter</span>
              }
            </div>
            <div class="form-group">
              <label for="reg-email">E-posta</label>
              <input id="reg-email" type="email" class="form-input"
                     formControlName="email" placeholder="ornek@mail.com" />
              @if (form.get('email')?.touched && form.get('email')?.hasError('required')) {
                <span class="form-error">Zorunlu alan</span>
              }
              @if (form.get('email')?.touched && form.get('email')?.hasError('email')) {
                <span class="form-error">Geçerli bir e-posta girin</span>
              }
            </div>
          </div>

          <!-- Row 2: Full Name + Phone -->
          <div class="form-row">
            <div class="form-group">
              <label for="reg-fullname">Ad Soyad</label>
              <input id="reg-fullname" type="text" class="form-input"
                     formControlName="full_name" placeholder="Ad Soyad" />
              @if (form.get('full_name')?.touched && form.get('full_name')?.hasError('required')) {
                <span class="form-error">Zorunlu alan</span>
              }
            </div>
            <div class="form-group">
              <label for="reg-phone">Telefon</label>
              <input id="reg-phone" type="tel" class="form-input"
                     formControlName="phone_number" placeholder="+90 5XX XXX XX XX" />
              @if (form.get('phone_number')?.touched && form.get('phone_number')?.hasError('required')) {
                <span class="form-error">Zorunlu alan</span>
              }
            </div>
          </div>

          <!-- Row 3: Password -->
          <div class="form-group">
            <label for="reg-password">Şifre</label>
            <input id="reg-password" type="password" class="form-input"
                   formControlName="password" placeholder="En az 6 karakter" />
            @if (form.get('password')?.touched && form.get('password')?.hasError('required')) {
              <span class="form-error">Zorunlu alan</span>
            }
            @if (form.get('password')?.touched && form.get('password')?.hasError('minlength')) {
              <span class="form-error">En az 6 karakter olmalı</span>
            }
          </div>

          <!-- Row 4: Address -->
          <div class="form-group">
            <label for="reg-address">Adres</label>
            <input id="reg-address" type="text" class="form-input"
                   formControlName="address" placeholder="Mahalle, sokak, il..." />
          </div>

          <!-- Row 5: DOB + Gender -->
          <div class="form-row">
            <div class="form-group">
              <label for="reg-dob">Doğum Tarihi</label>
              <input id="reg-dob" type="date" class="form-input"
                     formControlName="dob" />
            </div>
            <div class="form-group">
              <label>Cinsiyet</label>
              <p-select
                formControlName="gender"
                [options]="genderOptions"
                optionLabel="label"
                optionValue="value"
                placeholder="Seçin..."
                styleClass="w-full" />
            </div>
          </div>

          <!-- Submit -->
          <button type="submit" class="btn btn-primary register-btn"
                  [disabled]="form.invalid || loading()">
            @if (loading()) {
              <span class="material-symbols-outlined spin">progress_activity</span>
              Kayıt yapılıyor...
            } @else {
              <span class="material-symbols-outlined">person_add</span>
              Hesap Oluştur
            }
          </button>
        </form>

        <!-- Link to login -->
        <div class="register-footer">
          <span>Zaten hesabınız var mı?</span>
          <a routerLink="/login">Giriş Yap</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    @use 'variables' as *;

    .register-page {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem 1rem;
      background: linear-gradient(135deg, #111827 0%, $primary-dark 50%, #0f172a 100%);
    }

    .register-card {
      width: 100%;
      max-width: 540px;
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(20px);
      border-radius: $border-radius-lg;
      padding: 2.5rem;
      box-shadow: $shadow-lg;
      border: 1px solid rgba(255, 255, 255, 0.2);
    }

    :host-context([data-theme='dark']) .register-card {
      background: rgba(26, 26, 46, 0.9);
      border: 1px solid rgba(255, 255, 255, 0.08);
    }

    .register-header {
      text-align: center;
      margin-bottom: 2rem;

      .register-logo {
        font-size: 44px;
        color: $primary;
      }

      h1 {
        font-size: 1.5rem;
        font-weight: 700;
        color: $text-primary;
        margin-top: 0.5rem;
      }

      p {
        color: $text-muted;
        font-size: 0.875rem;
        margin-top: 0.25rem;
      }
    }

    .register-form {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }

    .form-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1rem;
    }

    .register-btn {
      width: 100%;
      justify-content: center;
      padding: 0.75rem;
      font-size: 1rem;
      margin-top: 0.5rem;
    }

    .register-footer {
      text-align: center;
      margin-top: 1.5rem;
      font-size: 0.875rem;
      color: $text-muted;

      a {
        color: $primary;
        font-weight: 600;
        margin-left: 0.25rem;
        &:hover { text-decoration: underline; }
      }
    }

    .spin {
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      from { transform: rotate(0deg); }
      to { transform: rotate(360deg); }
    }
  `],
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly notify = inject(NotificationService);

  loading = signal(false);

  genderOptions = [
    { label: 'Erkek', value: 'MALE' },
    { label: 'Kadın', value: 'FEMALE' },
    { label: 'Belirtmek İstemiyorum', value: 'OTHER' },
  ];

  form = this.fb.group({
    username:     ['', [Validators.required, Validators.minLength(3)]],
    email:        ['', [Validators.required, Validators.email]],
    password:     ['', [Validators.required, Validators.minLength(6)]],
    full_name:    ['', Validators.required],
    phone_number: ['', Validators.required],
    address:      [''],
    dob:          [''],
    gender:       ['MALE'],
  });

  onRegister(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);

    const val = this.form.getRawValue();
    this.authService.register({
      username: val.username!,
      email: val.email!,
      password: val.password!,
      full_name: val.full_name!,
      phone_number: val.phone_number!,
      address: val.address || '',
      dob: val.dob || '',
      gender: val.gender || 'MALE',
    }).subscribe({
      next: () => {
        this.notify.success('Hesabınız başarıyla oluşturuldu!');
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.loading.set(false);
        // Toast is already handled by the global interceptor
      },
    });
  }
}

