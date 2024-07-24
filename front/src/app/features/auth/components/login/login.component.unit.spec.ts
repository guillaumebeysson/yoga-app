import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Router } from '@angular/router';


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let authServiceMock: jest.Mocked<AuthService>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async () => {

    authServiceMock = {
      login: jest.fn().mockReturnValue(of({} as SessionInformation)),
    } as unknown as jest.Mocked<AuthService>;

    sessionServiceMock = {
      logIn: jest.fn()
    } as unknown as jest.Mocked<SessionService>;

    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should validate form when inputs field are not empty', () => {
    component.form.setValue({ email: "test@gmail.com", password: "azerty" });
    expect(component.form.invalid).toBeFalsy();
  });

  it('should call authService.login() when submit()', () => {
    component.submit();
    expect(authServiceMock.login).toHaveBeenCalled();
  });

  it('should call sessionService.logIn() when submit()', () => {
    component.submit();
    expect(sessionServiceMock.logIn).toHaveBeenCalled();
  });

  it('should navigate to /sessions when submit()', () => {
    component.submit();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions'])
  });

  it('should set onErrror to true when authService.login() throw error during submit()', () => {
    authServiceMock.login.mockImplementation(() => {
      const error = new Error('Error');
      return throwError(() => error);
    });
    component.submit();
    expect(component.onError).toBeTruthy();
  });

});