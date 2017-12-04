/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ResavisionTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ClientDetailComponent } from '../../../../../../main/webapp/app/entities/client/client-detail.component';
import { ClientService } from '../../../../../../main/webapp/app/entities/client/client.service';
import { Client } from '../../../../../../main/webapp/app/entities/client/client.model';

describe('Component Tests', () => {

    describe('Client Management Detail Component', () => {
        let comp: ClientDetailComponent;
        let fixture: ComponentFixture<ClientDetailComponent>;
        let service: ClientService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ResavisionTestModule],
                declarations: [ClientDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ClientService,
                    JhiEventManager
                ]
            }).overrideTemplate(ClientDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClientDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClientService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Client(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.client).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
