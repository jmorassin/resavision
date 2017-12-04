import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Responsable } from './responsable.model';
import { ResponsablePopupService } from './responsable-popup.service';
import { ResponsableService } from './responsable.service';

@Component({
    selector: 'jhi-responsable-delete-dialog',
    templateUrl: './responsable-delete-dialog.component.html'
})
export class ResponsableDeleteDialogComponent {

    responsable: Responsable;

    constructor(
        private responsableService: ResponsableService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.responsableService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'responsableListModification',
                content: 'Deleted an responsable'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-responsable-delete-popup',
    template: ''
})
export class ResponsableDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private responsablePopupService: ResponsablePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.responsablePopupService
                .open(ResponsableDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
