/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ResavisionTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TypeSiteDetailComponent } from '../../../../../../main/webapp/app/entities/type-site/type-site-detail.component';
import { TypeSiteService } from '../../../../../../main/webapp/app/entities/type-site/type-site.service';
import { TypeSite } from '../../../../../../main/webapp/app/entities/type-site/type-site.model';

describe('Component Tests', () => {

    describe('TypeSite Management Detail Component', () => {
        let comp: TypeSiteDetailComponent;
        let fixture: ComponentFixture<TypeSiteDetailComponent>;
        let service: TypeSiteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ResavisionTestModule],
                declarations: [TypeSiteDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TypeSiteService,
                    JhiEventManager
                ]
            }).overrideTemplate(TypeSiteDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TypeSiteDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TypeSiteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TypeSite(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.typeSite).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
