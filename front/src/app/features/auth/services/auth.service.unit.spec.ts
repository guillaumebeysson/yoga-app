import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', (): void => {

    let service: AuthService;
    let controller: HttpTestingController;
    let pathService = 'api/auth';

    beforeEach((): void => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule]
        });
        service = TestBed.inject(AuthService);
        controller = TestBed.inject(HttpTestingController);
    });

    afterEach((): void => {
        controller.verify();
    })

    it('should be created', (): void => {
        expect(service).toBeTruthy();
    });

    it('should handle register request properly', (done) => {
        const registerRequestMock: RegisterRequest = {
            email: '',
            firstName: '',
            lastName: '',
            password: ''
        }
        service.register(registerRequestMock)
            .subscribe((response) => {
                expect(response).toBe(null);
                done()
            });
        const request: TestRequest = controller.expectOne(`${pathService}/register`);
        expect(request.request.method).toBe('POST');
        request.flush(null);
    });

    it('should handle register login properly', (done) => {
        const loginRequestMock: LoginRequest = {
            email: '',
            password: ''
        }
        const sessionInformationMock: SessionInformation = {
            token: '',
            type: '',
            id: 0,
            username: '',
            firstName: '',
            lastName: '',
            admin: false
        }
        service.login(loginRequestMock)
            .subscribe(response => {
                expect(response).toBe(sessionInformationMock);
                done();
            });
        const request: TestRequest = controller.expectOne(`${pathService}/login`);
        expect(request.request.method).toBe('POST');
        request.flush(sessionInformationMock);
    });

});