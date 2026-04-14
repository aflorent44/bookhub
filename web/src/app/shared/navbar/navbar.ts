import {Component} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { RouterLink } from "@angular/router";

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
  isLoggedIn: boolean = true;

  login() {
    this.isLoggedIn = true;
  }

  logout() {
    this.isLoggedIn = false;
  }
}
