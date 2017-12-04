import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TypeBoutique } from './type-boutique.model';
import { TypeBoutiquePopupService } from './type-boutique-popup.service';
import { TypeBoutiqueService } from './type-boutique.service';
import { TypeSite, TypeSiteService } from '../type-site';
import { Boutique, BoutiqueService } from '../boutique';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-type-boutique-dialog',
    templateUrl: './type-boutique-dialog.component.html'
})
export class TypeBoutiqueDialogComponent implements OnInit {

    typeBoutique: TypeBoutique;
    isSaving: boolean;

    typesites: TypeSite[];

    boutiques: Boutique[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private typeBoutiqueService: TypeBoutiqueService,
        private typeSiteService: TypeSiteService,
        private boutiqueService: BoutiqueService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.typeSiteService.query()
            .subscribe((res: ResponseWrapper) => { this.typesites = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.boutiqueService.query()
            .subscribe((res: ResponseWrapper) => { this.boutiques = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.typeBoutique.id !== undefined) {
            this.subscribeToSaveResponse(
                this.typeBoutiqueService.update(this.typeBoutique));
        } else {
            this.subscribeToSaveResponse(
                this.typeBoutiqueService.create(this.typeBoutique));
        }
    }

    private subscribeToSaveResponse(result: Observable<TypeBoutique>) {
        result.subscribe((res: TypeBoutique) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TypeBoutique) {
        this.eventManager.broadcast({ name: 'typeBoutiqueListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTypeSiteById(index: number, item: TypeSite) {
        return item.id;
    }

    trackBoutiqueById(index: number, item: Boutique) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-type-boutique-popup',
    template: ''
})
export class TypeBoutiquePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private typeBoutiquePopupService: TypeBoutiquePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.typeBoutiquePopupService
                    .open(TypeBoutiqueDialogComponent as Component, params['id']);
            } else {
                this.typeBoutiquePopupService
                    .open(TypeBoutiqueDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
