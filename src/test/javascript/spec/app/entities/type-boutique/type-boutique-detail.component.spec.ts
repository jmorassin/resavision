/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ResavisionTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TypeBoutiqueDetailComponent } from '../../../../../../main/webapp/app/entities/type-boutique/type-boutique-detail.component';
import { TypeBoutiqueService } from '../../../../../../main/webapp/app/entities/type-boutique/type-boutique.service';
import { TypeBoutique } from '../../../../../../main/webapp/app/entities/type-boutique/type-boutique.model';

describe('Component Tests', () => {

    describe('TypeBoutique Management Detail Component', () => {
        let comp: TypeBoutiqueDetailComponent;
        let fixture: ComponentFixture<TypeBoutiqueDetailComponent>;
        let service: TypeBoutiqueService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ResavisionTestModule],
                declarations: [TypeBoutiqueDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TypeBoutiqueService,
                    JhiEventManager
                ]
            }).overrideTemplate(TypeBoutiqueDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TypeBoutiqueDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TypeBoutiqueService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TypeBoutique(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.typeBoutique).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
