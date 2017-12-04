import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Ville } from './ville.model';
import { VillePopupService } from './ville-popup.service';
import { VilleService } from './ville.service';

@Component({
    selector: 'jhi-ville-delete-dialog',
    templateUrl: './ville-delete-dialog.component.html'
})
export class VilleDeleteDialogComponent {

    ville: Ville;

    constructor(
        private villeService: VilleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.villeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'villeListModification',
                content: 'Deleted an ville'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ville-delete-popup',
    template: ''
})
export class VilleDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private villePopupService: VillePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.villePopupService
                .open(VilleDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
