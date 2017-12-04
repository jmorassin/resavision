import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Responsable } from './responsable.model';
import { ResponsablePopupService } from './responsable-popup.service';
import { ResponsableService } from './responsable.service';
import { User, UserService } from '../../shared';
import { Boutique, BoutiqueService } from '../boutique';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-responsable-dialog',
    templateUrl: './responsable-dialog.component.html'
})
export class ResponsableDialogComponent implements OnInit {

    responsable: Responsable;
    isSaving: boolean;

    users: User[];

    boutiques: Boutique[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private responsableService: ResponsableService,
        private userService: UserService,
        private boutiqueService: BoutiqueService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.boutiqueService
            .query({filter: 'responsable-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.responsable.boutique || !this.responsable.boutique.id) {
                    this.boutiques = res.json;
                } else {
                    this.boutiqueService
                        .find(this.responsable.boutique.id)
                        .subscribe((subRes: Boutique) => {
                            this.boutiques = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.responsable.id !== undefined) {
            this.subscribeToSaveResponse(
                this.responsableService.update(this.responsable));
        } else {
            this.subscribeToSaveResponse(
                this.responsableService.create(this.responsable));
        }
    }

    private subscribeToSaveResponse(result: Observable<Responsable>) {
        result.subscribe((res: Responsable) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Responsable) {
        this.eventManager.broadcast({ name: 'responsableListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackBoutiqueById(index: number, item: Boutique) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-responsable-popup',
    template: ''
})
export class ResponsablePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private responsablePopupService: ResponsablePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.responsablePopupService
                    .open(ResponsableDialogComponent as Component, params['id']);
            } else {
                this.responsablePopupService
                    .open(ResponsableDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
