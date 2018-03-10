import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription, Observable } from 'rxjs/Rx';
import { Ville } from './ville.model';
import { VilleService } from './ville.service';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-ville-form',
    templateUrl: './ville-form.component.html'
})
export class VilleFormComponent implements OnInit, OnDestroy {

    ville: Ville;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    enCreation: boolean;
    enModification: boolean;
    enConsultation: boolean;

    constructor(
        private eventManager: JhiEventManager,
        private villeService: VilleService,
        private jhiAlertService: JhiAlertService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
      this.enCreation = false;
      this.enModification = true;
      this.enConsultation = false;
      this.subscription = this.route.params.subscribe((params) => {
            if (params['id'] !== '0') {
              this.load(params['id']);
              this.enConsultation = true;
              this.enModification = false;
            } else {
              console.log('on innitialise la ville');
              this.ville = {};
              this.ville.id = 0;
              this.enCreation = true;
            }
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
    swithForm() {
      this.load(this.ville.id);
      this.enModification = !this.enModification;
      this.enConsultation = !this.enConsultation;
    }

    nextState() {
       console.log('nextState');
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

    save() {
        this.enCreation = true;
        if (this.ville.id !== undefined) {
            this.subscribeToSaveResponse(
                this.villeService.update(this.ville));
        } else {
            this.subscribeToSaveResponse(
                this.villeService.create(this.ville));
        }
    }

    private subscribeToSaveResponse(result: Observable<Ville>) {
        result.subscribe((res: Ville) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Ville) {
        this.ville.id = result.id;
        this.enCreation = false;
        this.swithForm();
    }

    private onSaveError() {
        this.enCreation = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
  
}