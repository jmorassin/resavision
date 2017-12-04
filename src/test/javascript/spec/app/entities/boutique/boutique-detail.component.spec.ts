/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ResavisionTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BoutiqueDetailComponent } from '../../../../../../main/webapp/app/entities/boutique/boutique-detail.component';
import { BoutiqueService } from '../../../../../../main/webapp/app/entities/boutique/boutique.service';
import { Boutique } from '../../../../../../main/webapp/app/entities/boutique/boutique.model';

describe('Component Tests', () => {

    describe('Boutique Management Detail Component', () => {
        let comp: BoutiqueDetailComponent;
        let fixture: ComponentFixture<BoutiqueDetailComponent>;
        let service: BoutiqueService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ResavisionTestModule],
                declarations: [BoutiqueDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BoutiqueService,
                    JhiEventManager
                ]
            }).overrideTemplate(BoutiqueDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BoutiqueDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BoutiqueService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Boutique(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.boutique).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
