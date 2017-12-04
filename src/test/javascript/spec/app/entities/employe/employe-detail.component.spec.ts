/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ResavisionTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EmployeDetailComponent } from '../../../../../../main/webapp/app/entities/employe/employe-detail.component';
import { EmployeService } from '../../../../../../main/webapp/app/entities/employe/employe.service';
import { Employe } from '../../../../../../main/webapp/app/entities/employe/employe.model';

describe('Component Tests', () => {

    describe('Employe Management Detail Component', () => {
        let comp: EmployeDetailComponent;
        let fixture: ComponentFixture<EmployeDetailComponent>;
        let service: EmployeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ResavisionTestModule],
                declarations: [EmployeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EmployeService,
                    JhiEventManager
                ]
            }).overrideTemplate(EmployeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmployeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Employe(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.employe).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
