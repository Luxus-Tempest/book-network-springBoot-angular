import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services";

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {
  message = '';
  isOkay = true;
  submitted = false;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {

  }


  redirectToLogin() {
    this.router.navigate(['/login']);
  }

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  private confirmAccount(token: string) {
    this.authService.activate({
      token
    }).subscribe({
      next: () => {
        this.message = 'Your account has been activated successfully.\n You can now login';
        this.isOkay = true;
        this.submitted = true;
      },
      error: (err) => {
        this.message = 'An error occurred while activating your account';
        this.isOkay = false;
        this.submitted = true;
      }
    })
  }
}
