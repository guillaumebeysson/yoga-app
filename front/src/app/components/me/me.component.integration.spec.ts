import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const sessionInfoMock: SessionInformation = {
    token: '',
    type: '',
    id: 1,
    username: '',
    firstName: '',
    lastName: '',
    admin: false
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      providers: [
        UserService,
        SessionService,
        MatSnackBar
      ],
    })
      .compileComponents();

    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    sessionService.logIn(sessionInfoMock);

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;

    fixture.detectChanges()
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should delete user properly', async () => {
    const userServiceSpy = jest.spyOn(userService, 'delete').mockReturnValue(of({} as any))
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open')
    const sessionServiceSpy = jest.spyOn(sessionService, 'logOut')
    const routerSpy = jest.spyOn(router, 'navigate');

    component.delete();

    expect(userServiceSpy).toHaveBeenCalledWith(sessionInfoMock.id.toString());
    expect(matSnackBarSpy).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    await expect(sessionServiceSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/']);
  });

});