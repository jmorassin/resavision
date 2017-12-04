import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Boutique } from './boutique.model';
import { BoutiquePopupService } from './boutique-popup.service';
import { BoutiqueService } from './boutique.service';
import { Ville, VilleService } from '../ville';
import { TypeBoutique, TypeBoutiqueService } from '../type-boutique';
import { Responsable, ResponsableService } from '../responsable';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-boutique-dialog',
    templateUrl: './boutique-dialog.component.html'
})
export class BoutiqueDialogComponent implements OnInit {

    boutique: Boutique;
    isSaving: boolean;

    villes: Ville[];

    typeboutiques: TypeBoutique[];

    responsables: Responsable[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private boutiqueService: BoutiqueService,
        private villeService: VilleService,
        private typeBoutiqueService: TypeBoutiqueService,
        private responsableService: ResponsableService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.villeService.query()
            .subscribe((res: ResponseWrapper) => { this.villes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.typeBoutiqueService.query()
            .subscribe((res: ResponseWrapper) => { this.typeboutiques = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.responsableService.query()
            .subscribe((res: ResponseWrapper) => { this.responsables = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.boutique, this.elementRef, field, fieldContentType, idInput);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.boutique.id !== undefined) {
            this.subscribeToSaveResponse(
                this.boutiqueService.update(this.boutique));
        } else {
            this.subscribeToSaveResponse(
                this.boutiqueService.create(this.boutique));
        }
    }

    private subscribeToSaveResponse(result: Observable<Boutique>) {
        result.subscribe((res: Boutique) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Boutique) {
        this.eventManager.broadcast({ name: 'boutiqueListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackVilleById(index: number, item: Ville) {
        return item.id;
    }

    trackTypeBoutiqueById(index: number, item: TypeBoutique) {
        return item.id;
    }

    trackResponsableById(index: number, item: Responsable) {
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
    selector: 'jhi-boutique-popup',
    template: ''
})
export class BoutiquePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boutiquePopupService: BoutiquePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.boutiquePopupService
                    .open(BoutiqueDialogComponent as Component, params['id']);
            } else {
                this.boutiquePopupService
                    .open(BoutiqueDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
