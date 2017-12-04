import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TypeBoutique } from './type-boutique.model';
import { TypeBoutiquePopupService } from './type-boutique-popup.service';
import { TypeBoutiqueService } from './type-boutique.service';

@Component({
    selector: 'jhi-type-boutique-delete-dialog',
    templateUrl: './type-boutique-delete-dialog.component.html'
})
export class TypeBoutiqueDeleteDialogComponent {

    typeBoutique: TypeBoutique;

    constructor(
        private typeBoutiqueService: TypeBoutiqueService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.typeBoutiqueService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'typeBoutiqueListModification',
                content: 'Deleted an typeBoutique'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-type-boutique-delete-popup',
    template: ''
})
export class TypeBoutiqueDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private typeBoutiquePopupService: TypeBoutiquePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.typeBoutiquePopupService
                .open(TypeBoutiqueDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
