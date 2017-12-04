import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Ville } from './ville.model';
import { VilleService } from './ville.service';

@Component({
    selector: 'jhi-ville-detail',
    templateUrl: './ville-detail.component.html'
})
export class VilleDetailComponent implements OnInit, OnDestroy {

    ville: Ville;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private villeService: VilleService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVilles();
    }

    load(id) {
        this.villeService.find(id).subscribe((ville) => {
            this.ville = ville;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVilles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'villeListModification',
            (response) => this.load(this.ville.id)
        );
    }
}
