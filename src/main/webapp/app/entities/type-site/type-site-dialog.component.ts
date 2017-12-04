import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TypeSite } from './type-site.model';
import { TypeSitePopupService } from './type-site-popup.service';
import { TypeSiteService } from './type-site.service';

@Component({
    selector: 'jhi-type-site-dialog',
    templateUrl: './type-site-dialog.component.html'
})
export class TypeSiteDialogComponent implements OnInit {

    typeSite: TypeSite;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private typeSiteService: TypeSiteService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.typeSite.id !== undefined) {
            this.subscribeToSaveResponse(
                this.typeSiteService.update(this.typeSite));
        } else {
            this.subscribeToSaveResponse(
                this.typeSiteService.create(this.typeSite));
        }
    }

    private subscribeToSaveResponse(result: Observable<TypeSite>) {
        result.subscribe((res: TypeSite) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TypeSite) {
        this.eventManager.broadcast({ name: 'typeSiteListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-type-site-popup',
    template: ''
})
export class TypeSitePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private typeSitePopupService: TypeSitePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.typeSitePopupService
                    .open(TypeSiteDialogComponent as Component, params['id']);
            } else {
                this.typeSitePopupService
                    .open(TypeSiteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
