/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ResavisionTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ResponsableDetailComponent } from '../../../../../../main/webapp/app/entities/responsable/responsable-detail.component';
import { ResponsableService } from '../../../../../../main/webapp/app/entities/responsable/responsable.service';
import { Responsable } from '../../../../../../main/webapp/app/entities/responsable/responsable.model';

describe('Component Tests', () => {

    describe('Responsable Management Detail Component', () => {
        let comp: ResponsableDetailComponent;
        let fixture: ComponentFixture<ResponsableDetailComponent>;
        let service: ResponsableService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ResavisionTestModule],
                declarations: [ResponsableDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ResponsableService,
                    JhiEventManager
                ]
            }).overrideTemplate(ResponsableDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ResponsableDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ResponsableService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Responsable(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.responsable).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
