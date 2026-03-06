import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Toast } from 'primeng/toast';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Toast],
  template: `
    <p-toast key="global" position="top-right" />
    <router-outlet />
  `,
})
export class AppComponent {}
