import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TypeBoutique } from './type-boutique.model';
import { TypeBoutiqueService } from './type-boutique.service';

@Component({
    selector: 'jhi-type-boutique-detail',
    templateUrl: './type-boutique-detail.component.html'
})
export class TypeBoutiqueDetailComponent implements OnInit, OnDestroy {

    typeBoutique: TypeBoutique;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private typeBoutiqueService: TypeBoutiqueService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTypeBoutiques();
    }

    load(id) {
        this.typeBoutiqueService.find(id).subscribe((typeBoutique) => {
            this.typeBoutique = typeBoutique;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTypeBoutiques() {
        this.eventSubscriber = this.eventManager.subscribe(
            'typeBoutiqueListModification',
            (response) => this.load(this.typeBoutique.id)
        );
    }
}
