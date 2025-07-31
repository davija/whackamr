import { Component, input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { AuthService } from '@services/auth.service';
import { MeService } from '@services/me.service';
import { AuthStore } from '@store/auth/auth.store';

@Component({
  standalone: true,
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
  imports: [FormsModule, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
})
export class LoginPageComponent implements OnInit {
  protected loginForm: FormGroup;
  protected loginError = false;

  private tokenStatus = input.required<string>;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private meService: MeService,
    protected authStore: AuthStore,
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  protected doLogin(): void {
    this.loginError = false;
    this.authService.login(this.loginForm.value.username, this.loginForm.value.password).subscribe(success => {
      if (success) {
        this.handleSuccess();
      } else {
        this.handleFailure();
      }
    });
  }

  private handleSuccess(): void {
    this.meService.getMe().subscribe(currentUser => this.authStore.setCurrentUser(currentUser));
    this.meService.getMyPermissions().subscribe(permissions => this.authStore.setPermissions(permissions));
    this.router.navigate(['mergeRequests']);
  }

  private handleFailure(): void {
    this.loginForm.controls['password'].patchValue('');
    this.loginError = true;
  }
}
