import { TestBed } from '@angular/core/testing';

import {DecodejwtService} from './decodejwt-service.service';

describe('DecodejwtServiceService', () => {
  let service: DecodejwtService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DecodejwtService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
