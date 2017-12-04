import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Employe } from './employe.model';
import { EmployePopupService } from './employe-popup.service';
import { EmployeService } from './employe.service';
import { User, UserService } from '../../shared';
import { Boutique, BoutiqueService } from '../boutique';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-employe-dialog',
    templateUrl: './employe-dialog.component.html'
})
export class EmployeDialogComponent implements OnInit {

    employe: Employe;
    isSaving: boolean;

    users: User[];

    boutiques: Boutique[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private employeService: EmployeService,
        private userService: UserService,
        private boutiqueService: BoutiqueService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.boutiqueService.query()
            .subscribe((res: ResponseWrapper) => { this.boutiques = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.employe.id !== undefined) {
            this.subscribeToSaveResponse(
                this.employeService.update(this.employe));
        } else {
            this.subscribeToSaveResponse(
                this.employeService.create(this.employe));
        }
    }

    private subscribeToSaveResponse(result: Observable<Employe>) {
        result.subscribe((res: Employe) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Employe) {
        this.eventManager.broadcast({ name: 'employeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackBoutiqueById(index: number, item: Boutique) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-employe-popup',
    template: ''
})
export class EmployePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employePopupService: EmployePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.employePopupService
                    .open(EmployeDialogComponent as Component, params['id']);
            } else {
                this.employePopupService
                    .open(EmployeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
