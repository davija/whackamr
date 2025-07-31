import { Routes } from '@angular/router';
import { MergeRequestsPageComponent } from '@pages/merge-requests/merge-requests-page.component';
import { AuthGuard } from './guards/auth-guard';
import { LoginPageComponent } from './pages/login/login-page.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  {
    path: 'mergeRequests',
    component: MergeRequestsPageComponent,
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
  },
];
