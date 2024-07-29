import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {

  const sessionMock: Session = {
    name: '',
    description: '',
    date: new Date(),
    teacher_id: 0,
    users: []
  }

  let service: SessionApiService;
  let controller: HttpTestingController;
  let pathService = 'api/session';

  beforeEach((): void => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(SessionApiService);
    controller = TestBed.inject(HttpTestingController);
  });

  afterEach((): void => {
    controller.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle get All request properly', (done) => {
    const sessionListMock: Session[] = [sessionMock];
    service.all()
      .subscribe((response) => {
        expect(response).toBe(sessionListMock);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}`);
    expect(request.request.method).toBe('GET');
    request.flush(sessionListMock);
  });

  it('should handle get Detail request properly', (done) => {
    const id = '1';
    service.detail(id)
      .subscribe((response) => {
        expect(response).toBe(sessionMock);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}/${id}`);
    expect(request.request.method).toBe('GET');
    request.flush(sessionMock);
  });

  it('should handle Delete request properly', (done) => {
    const id = '1';
    service.delete(id)
      .subscribe((response) => {
        expect(response).toBe(null);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}/${id}`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });

  it('should handle Create request properly', (done) => {
    service.create(sessionMock)
      .subscribe((response) => {
        expect(response).toBe(sessionMock);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toBe(sessionMock);
    request.flush(sessionMock);
  });

  it('should handle Update request properly', (done) => {
    const id = '1';
    service.update(id, sessionMock)
      .subscribe((response) => {
        expect(response).toBe(sessionMock);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}/${id}`);
    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toBe(sessionMock);
    request.flush(sessionMock);
  });

  it('should handle Participate request properly', (done) => {
    const id = '1';
    const userId = '2';
    service.participate(id, userId)
      .subscribe((response) => {
        expect(response).toBe(null);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}/${id}/participate/${userId}`);
    expect(request.request.method).toBe('POST');
    request.flush(null);
  });

  it('should handle unParticipate request properly', (done) => {
    const id = '1';
    const userId = '2';
    service.unParticipate(id, userId)
      .subscribe((response) => {
        expect(response).toBe(null);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}/${id}/participate/${userId}`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });

});