import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TypeSite } from './type-site.model';
import { TypeSiteService } from './type-site.service';

@Component({
    selector: 'jhi-type-site-detail',
    templateUrl: './type-site-detail.component.html'
})
export class TypeSiteDetailComponent implements OnInit, OnDestroy {

    typeSite: TypeSite;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private typeSiteService: TypeSiteService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTypeSites();
    }

    load(id) {
        this.typeSiteService.find(id).subscribe((typeSite) => {
            this.typeSite = typeSite;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTypeSites() {
        this.eventSubscriber = this.eventManager.subscribe(
            'typeSiteListModification',
            (response) => this.load(this.typeSite.id)
        );
    }
}
