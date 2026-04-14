import {Component, inject, OnInit} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Router, RouterLink} from "@angular/router";
import {AuthService} from '../../core/service/auth-service';

@Component({
  selector: 'app-navbar',
  imports: [FormsModule,
    ReactiveFormsModule,
    RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
  standalone: true
})
export class Navbar {
  private authService = inject(AuthService);

  currentUser = this.authService.currentUser$;  // expose le signal directement

  logout() {
    this.authService.logout();
  }
}
