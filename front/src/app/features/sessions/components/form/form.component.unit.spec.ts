import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const sessionMock = {
    name: 'name',
    description: 'description',
    date: new Date(),
    teacher_id: 1,
    users: []
  } as jest.Mocked<Session>;

  let sessionServiceMock: jest.Mocked<SessionService>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let routerMock: jest.Mocked<Router>;
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;

  beforeEach(async () => {

    sessionServiceMock = {
      sessionInformation: {
        admin: true
      }
    } as unknown as jest.Mocked<SessionService>;

    sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of(sessionMock)),
      create: jest.fn().mockReturnValue(of(sessionMock)),
      update: jest.fn().mockReturnValue(of(sessionMock))
    } as unknown as jest.Mocked<SessionApiService>;

    matSnackBarMock = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    routerMock = {
      url: '/',
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue(of('1'))
        }
      }
    } as unknown as jest.Mocked<ActivatedRoute>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('when ngOnInit()', () => {
    it('should not navigate to /sessions if admin user', () => {
      component.ngOnInit();
      expect(routerMock.navigate).not.toHaveBeenCalled();
    });
    it('should navigate to /sessions if not admin user', () => {
      sessionServiceMock.sessionInformation!.admin = false;
      component.ngOnInit();
      expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
    });
    it('should init sessionForm', () => {
      expect(component.sessionForm).toBeUndefined();
      component.ngOnInit();
      expect(component.sessionForm).toBeDefined();
    });
    it('should init an empty form if router.url not includes "update"', () => {
      expect(component.sessionForm?.valid).toBeFalsy();
    });
    it('should init the form with session data if router.url includes "update"', () => {
      // @ts-ignore : modify read-only property routerMock.url
      routerMock.url = '/update';
      component.ngOnInit();
      expect(component.onUpdate).toBeTruthy();
      expect(sessionApiServiceMock.detail).toHaveBeenCalledWith(activatedRouteMock.snapshot.paramMap.get(''));
      expect(component.sessionForm?.valid).toBeTruthy();
      expect(component.sessionForm?.value.name).toBe(sessionMock.name);
      expect(component.sessionForm?.value.description).toBe(sessionMock.description);
      expect(component.sessionForm?.value.date).toBe(sessionMock.date.toISOString().split('T')[0]);
      expect(component.sessionForm?.value.teacher_id).toBe(sessionMock.teacher_id);
    });
  });

  describe('when submit()', () => {

    let session: Session;

    beforeEach(async () => {
      component.ngOnInit();
      session = component.sessionForm?.value as Session;
    });

    it('should create session if onUpdate is false, by default', () => {
      component.submit();
      expect(sessionApiServiceMock.create).toHaveBeenCalledWith(session);
      expect(matSnackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    });
    it('should update session if onUpdate is true', () => {
      component.onUpdate = true;
      component.submit();
      // @ts-ignore : access private property component.id
      expect(sessionApiServiceMock.update).toHaveBeenCalledWith(component.id, session);
      expect(matSnackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

});