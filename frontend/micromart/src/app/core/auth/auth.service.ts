import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, catchError, throwError } from 'rxjs';
import {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
  UserResponse,
} from './auth.model';

const TOKEN_KEY = 'mm_access_token';
const USER_KEY = 'mm_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  // ── Signals ──
  private _currentUser = signal<UserResponse | null>(this.loadUserFromStorage());
  private _token = signal<string | null>(this.loadTokenFromStorage());

  readonly currentUser = this._currentUser.asReadonly();
  readonly token = this._token.asReadonly();
  readonly isLoggedIn = computed(() => !!this._token());
  readonly roles = computed(() => this._currentUser()?.roles ?? []);

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  // ── Public API ──

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/auth/login', credentials).pipe(
      tap((res) => this.handleAuthSuccess(res)),
      catchError((err) => throwError(() => err)),
    );
  }

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/auth/register', data).pipe(
      tap((res) => this.handleAuthSuccess(res)),
      catchError((err) => throwError(() => err)),
    );
  }

  refreshToken(): Observable<AuthResponse> {
    const currentToken = this._token();
    return this.http
      .post<AuthResponse>('/auth/refresh-token', { refreshToken: currentToken })
      .pipe(
        tap((res) => this.handleAuthSuccess(res)),
        catchError((err) => {
          this.logout();
          return throwError(() => err);
        }),
      );
  }

  fetchCurrentUser(): Observable<UserResponse> {
    return this.http.get<UserResponse>('/auth/me').pipe(
      tap((user) => {
        this._currentUser.set(user);
        localStorage.setItem(USER_KEY, JSON.stringify(user));
      }),
    );
  }

  logout(): void {
    this._token.set(null);
    this._currentUser.set(null);
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.router.navigate(['/login']);
  }

  hasRole(role: string): boolean {
    return this.roles().includes(role);
  }

  hasAnyRole(...roles: string[]): boolean {
    return roles.some((r) => this.roles().includes(r));
  }

  // ── Private ──

  private handleAuthSuccess(res: AuthResponse): void {
    this._token.set(res.token);
    localStorage.setItem(TOKEN_KEY, res.token);

    const user: UserResponse = {
      id: res.userId,
      username: res.username,
      email: res.email,
      roles: res.roles,
    };
    this._currentUser.set(user);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  private loadTokenFromStorage(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  private loadUserFromStorage(): UserResponse | null {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
  }
}

