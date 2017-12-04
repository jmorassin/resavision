import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Responsable } from './responsable.model';
import { ResponsableService } from './responsable.service';

@Component({
    selector: 'jhi-responsable-detail',
    templateUrl: './responsable-detail.component.html'
})
export class ResponsableDetailComponent implements OnInit, OnDestroy {

    responsable: Responsable;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private responsableService: ResponsableService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInResponsables();
    }

    load(id) {
        this.responsableService.find(id).subscribe((responsable) => {
            this.responsable = responsable;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInResponsables() {
        this.eventSubscriber = this.eventManager.subscribe(
            'responsableListModification',
            (response) => this.load(this.responsable.id)
        );
    }
}
