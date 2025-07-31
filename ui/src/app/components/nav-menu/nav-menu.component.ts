import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { MenuLink } from '../../models/app.model';

@Component({
  selector: 'app-nav-menu',
  standalone: true,
  templateUrl: './nav-menu.component.html',
  styleUrl: './nav-menu.component.scss',
  imports: [MatMenuModule, MatButtonModule],
})
export class NavMenuComponent {
  @Input() name: string;
  @Input() text: string;
  @Input() links: MenuLink[] = [];

  private doCloseMenu = false;
  private timeout: number;
  private prevTriggerRef: MatMenuTrigger | undefined;

  protected openMenu(menuTrigger: MatMenuTrigger, timeout: number) {
    if (this.links.length > 0) {
      if (this.prevTriggerRef) {
        this.prevTriggerRef.closeMenu();
        this.prevTriggerRef = undefined;
      }

      this.prevTriggerRef = menuTrigger;
      this.timeout = timeout;

      menuTrigger.openMenu();

      this.doCloseMenu = false;
      this.closeMenuLoop(menuTrigger);
    }
  }

  protected signalCloseMenu(timeout: number) {
    this.doCloseMenu = true;
    this.timeout = timeout;
  }

  protected signalMenuStayOpen(timeout: number) {
    this.doCloseMenu = false;
    this.timeout = timeout;
  }

  private closeMenuLoop(menuTrigger: MatMenuTrigger) {
    const closeLoop = () => {
      if (this.doCloseMenu) {
        menuTrigger.closeMenu();
        this.prevTriggerRef = undefined;
      } else {
        this.closeMenuLoop(menuTrigger);
      }
    };

    window.setTimeout(closeLoop.bind(this), this.timeout);
  }
}
