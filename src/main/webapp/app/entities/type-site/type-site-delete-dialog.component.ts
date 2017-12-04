import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TypeSite } from './type-site.model';
import { TypeSitePopupService } from './type-site-popup.service';
import { TypeSiteService } from './type-site.service';

@Component({
    selector: 'jhi-type-site-delete-dialog',
    templateUrl: './type-site-delete-dialog.component.html'
})
export class TypeSiteDeleteDialogComponent {

    typeSite: TypeSite;

    constructor(
        private typeSiteService: TypeSiteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.typeSiteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'typeSiteListModification',
                content: 'Deleted an typeSite'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-type-site-delete-popup',
    template: ''
})
export class TypeSiteDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private typeSitePopupService: TypeSitePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.typeSitePopupService
                .open(TypeSiteDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
