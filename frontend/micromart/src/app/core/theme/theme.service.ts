import { Injectable, signal, effect } from '@angular/core';

export type Theme = 'light' | 'dark';

const THEME_KEY = 'mm_theme';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  /** Current active theme */
  private readonly _theme = signal<Theme>(this.loadFromStorage());

  readonly theme = this._theme.asReadonly();
  readonly isDark = () => this._theme() === 'dark';

  constructor() {
    // Sync theme to DOM whenever the signal changes
    effect(() => {
      const theme = this._theme();
      const root = document.documentElement;

      root.setAttribute('data-theme', theme);
      root.classList.toggle('dark', theme === 'dark');
      localStorage.setItem(THEME_KEY, theme);
    });
  }

  toggle(): void {
    this._theme.update((current) => (current === 'light' ? 'dark' : 'light'));
  }

  setTheme(theme: Theme): void {
    this._theme.set(theme);
  }

  private loadFromStorage(): Theme {
    const stored = localStorage.getItem(THEME_KEY) as Theme | null;
    if (stored === 'light' || stored === 'dark') return stored;

    // Respect OS preference
    if (typeof window !== 'undefined' && window.matchMedia?.('(prefers-color-scheme: dark)').matches) {
      return 'dark';
    }
    return 'light';
  }
}

