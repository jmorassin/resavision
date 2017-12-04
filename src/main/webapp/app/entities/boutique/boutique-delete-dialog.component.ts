import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Boutique } from './boutique.model';
import { BoutiquePopupService } from './boutique-popup.service';
import { BoutiqueService } from './boutique.service';

@Component({
    selector: 'jhi-boutique-delete-dialog',
    templateUrl: './boutique-delete-dialog.component.html'
})
export class BoutiqueDeleteDialogComponent {

    boutique: Boutique;

    constructor(
        private boutiqueService: BoutiqueService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.boutiqueService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'boutiqueListModification',
                content: 'Deleted an boutique'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-boutique-delete-popup',
    template: ''
})
export class BoutiqueDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boutiquePopupService: BoutiquePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.boutiquePopupService
                .open(BoutiqueDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
