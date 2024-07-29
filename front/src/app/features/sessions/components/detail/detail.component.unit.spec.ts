import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { ActivatedRoute, Router } from '@angular/router';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;

  const sessionServiceMock = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const sessionMock: Session = {
    name: '',
    description: '',
    date: new Date(),
    teacher_id: 0,
    users: [sessionServiceMock.sessionInformation.id]
  };

  const teacherMock: Teacher = {
    id: 0,
    lastName: '',
    firstName: '',
    createdAt: new Date(),
    updatedAt: new Date()
  }

  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let teacherServiceMock: jest.Mocked<TeacherService>;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let routerMock: jest.Mocked<Router>;
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;

  beforeEach(async () => {

    sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of(sessionMock)),
      delete: jest.fn().mockReturnValue(of({} as any)),
      participate: jest.fn().mockReturnValue(of(void 0)),
      unParticipate: jest.fn().mockReturnValue(of(void 0))
    } as unknown as jest.Mocked<SessionApiService>;

    teacherServiceMock = {
      detail: jest.fn().mockReturnValue(of(teacherMock))
    } as unknown as jest.Mocked<TeacherService>;

    matSnackBarMock = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    routerMock = {
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
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session when ngOnInit()', () => {
    component.ngOnInit();
    expect(sessionApiServiceMock.detail).toHaveBeenCalled();
    expect(component.session).toBe(sessionMock);
    expect(component.isParticipate).toBeTruthy();
    expect(teacherServiceMock.detail).toHaveBeenCalledWith(component.session!.teacher_id.toString());
    expect(component.teacher).toBe(teacherMock);
  })

  it('should call window.history.back() when back()', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('should call sessionApiService.delete(), matSnackBar.open() and router.navigate() when delete()', () => {
    component.delete();
    expect(sessionApiServiceMock.delete).toHaveBeenCalledWith(component.sessionId);
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call sessionApiService.participate() when participate()', () => {
    component.participate();
    expect(sessionApiServiceMock.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
  });

  it('should call sessionApiService.unParticipate() when unParticipate()', () => {
    component.unParticipate();
    expect(sessionApiServiceMock.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
  });

});