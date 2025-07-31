import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { AuthService } from '@services/auth.service';
import { AuthStore } from '@store/auth/auth.store';
import { MenuLink } from '../../models/app.model';
import { NavMenuComponent } from '../nav-menu/nav-menu.component';

@Component({
  selector: 'app-header',
  standalone: true,
  styleUrls: ['./app-header.component.scss'],
  templateUrl: './app-header.component.html',
  imports: [MatButtonModule, MatMenuModule, NavMenuComponent],
})
export class AppHeaderComponent {
  mergeRequestLinks: MenuLink[] = [
    {
      url: '/1',
      text: 'View current merge requests',
    },
    {
      url: '/2',
      text: 'View my merge requests',
    },
    {
      url: '/3',
      text: 'Create new merge request',
    },
  ];

  adminLinks: MenuLink[] = [
    {
      url: '/1',
      text: 'Manage Users',
    },
    {
      url: '/1',
      text: 'Manage Teams',
    },
    {
      url: '/1',
      text: 'Manage Roles',
    },
    {
      url: '/1',
      text: 'Manage Permissions',
    },
  ];

  constructor(
    protected authStore: AuthStore,
    protected authService: AuthService,
  ) {}

  protected openMenu(menuTrigger: MatMenuTrigger) {
    menuTrigger.openMenu();
  }

  protected closeMenu(menuTrigger: MatMenuTrigger) {
    menuTrigger.closeMenu();
  }
}
