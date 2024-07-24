import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';


describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async () => {

    authServiceMock = {
      register: jest.fn().mockReturnValue(of(void 0))
    } as unknown as jest.Mocked<AuthService>;

    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should validate form when inputs field are not empty', () => {
    component.form.setValue({
      email: 'test@gmail.com',
      firstName: 'test',
      lastName: 'test',
      password: 'azerty'
    })
    expect(component.form.invalid).toBeFalsy();
  });

  it('should call authService.register() when submit()', () => {
    component.submit();
    expect(authServiceMock.register).toHaveBeenCalled();
  });

  it('should navigate to /login when submit()', () => {
    component.submit();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should set onErrror to true when authService.register() throw error during submit()', () => {
    authServiceMock.register.mockReturnValue(throwError(() => new Error()));
    component.submit();
    expect(component.onError).toBeTruthy();
  });
});