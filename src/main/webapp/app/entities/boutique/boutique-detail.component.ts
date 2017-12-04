import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Boutique } from './boutique.model';
import { BoutiqueService } from './boutique.service';

@Component({
    selector: 'jhi-boutique-detail',
    templateUrl: './boutique-detail.component.html'
})
export class BoutiqueDetailComponent implements OnInit, OnDestroy {

    boutique: Boutique;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private boutiqueService: BoutiqueService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBoutiques();
    }

    load(id) {
        this.boutiqueService.find(id).subscribe((boutique) => {
            this.boutique = boutique;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBoutiques() {
        this.eventSubscriber = this.eventManager.subscribe(
            'boutiqueListModification',
            (response) => this.load(this.boutique.id)
        );
    }
}
