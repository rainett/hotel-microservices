import {Component, HostListener} from '@angular/core';
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  isMenuOpen = false;
  isBigScreen = false;
  isLogged: boolean = false;

  constructor(private keycloak: KeycloakService) {}

  async ngOnInit() {
    this.isLogged = await this.keycloak.isLoggedIn();
    this.onResize();
  }

  @HostListener('window:resize')
  onResize() {
    this.isBigScreen = window.innerWidth >= 768;
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  login() {
    this.keycloak.login();
  }

  profile() {
    this.keycloak.getKeycloakInstance().accountManagement();
  }
}
