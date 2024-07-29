import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Session } from '../../interfaces/session.interface';


describe('DetailComponent', () => {
    let component: DetailComponent;
    let fixture: ComponentFixture<DetailComponent>;

    const sessionInfoMock: SessionInformation = {
        admin: true,
        id: 1,
        token: '',
        type: '',
        username: '',
        firstName: '',
        lastName: ''
    }

    let sessionService: SessionService;
    let sessionApiService: SessionApiService;
    let teacherService: TeacherService;
    let matSnackBar: MatSnackBar;
    let router: Router;

    let activatedRouteMock: jest.Mocked<ActivatedRoute>;
    let sessionMock: jest.Mocked<Session>

    beforeEach(async () => {

        activatedRouteMock = {
            snapshot: {
                paramMap: {
                    get: jest.fn().mockReturnValue(of('1'))
                }
            }
        } as unknown as jest.Mocked<ActivatedRoute>;

        sessionMock = {
            name: 'Test',
            description: 'Description Test',
            date: new Date(),
            teacher_id: 2,
            users: []
        };

        await TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
                HttpClientModule,
                MatSnackBarModule,
                ReactiveFormsModule,
                BrowserAnimationsModule,
                NoopAnimationsModule
            ],
            declarations: [DetailComponent],
            providers: [
                SessionService,
                SessionApiService,
                TeacherService,
                MatSnackBar,
                { provide: ActivatedRoute, useValue: activatedRouteMock },
            ],
        })
            .compileComponents();

        sessionService = TestBed.inject(SessionService);
        sessionApiService = TestBed.inject(SessionApiService);
        teacherService = TestBed.inject(TeacherService);
        matSnackBar = TestBed.inject(MatSnackBar);
        router = TestBed.inject(Router);

        sessionService.logIn(sessionInfoMock);

        fixture = TestBed.createComponent(DetailComponent);
        component = fixture.componentInstance;

        fixture.detectChanges();
    });

    it('should be created', () => {
        expect(component).toBeTruthy();
    });

    it('should delete session properly', async () => {
        const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of({} as any));
        const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
        const routerSpy = jest.spyOn(router, 'navigate');

        component.delete();

        expect(sessionApiServiceSpy).toHaveBeenCalledWith(component.sessionId);
        expect(matSnackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
        await expect(routerSpy).toHaveBeenCalledWith(['sessions']);
    });

    it('should participate properly', () => {
        const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(void 0));

        sessionMock.users.push(sessionInfoMock.id);
        const sessionApiServiceSpy2 = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(sessionMock));

        component.participate();
        expect(sessionApiServiceSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
        expect(sessionApiServiceSpy2).toHaveBeenCalledWith(component.sessionId);
        expect(component.isParticipate).toBeTruthy();
    });

    it('should unparticipate properly', () => {
        const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(void 0));

        const sessionApiServiceSpy2 = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(sessionMock));

        component.unParticipate();
        expect(sessionApiServiceSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
        expect(sessionApiServiceSpy2).toHaveBeenCalledWith(component.sessionId);
        expect(component.isParticipate).toBeFalsy();
    });

});