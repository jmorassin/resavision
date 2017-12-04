import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BoutiqueComponent } from './boutique.component';
import { BoutiqueDetailComponent } from './boutique-detail.component';
import { BoutiquePopupComponent } from './boutique-dialog.component';
import { BoutiqueDeletePopupComponent } from './boutique-delete-dialog.component';

@Injectable()
export class BoutiqueResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const boutiqueRoute: Routes = [
    {
        path: 'boutique',
        component: BoutiqueComponent,
        resolve: {
            'pagingParams': BoutiqueResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.boutique.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'boutique/:id',
        component: BoutiqueDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.boutique.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const boutiquePopupRoute: Routes = [
    {
        path: 'boutique-new',
        component: BoutiquePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.boutique.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'boutique/:id/edit',
        component: BoutiquePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.boutique.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'boutique/:id/delete',
        component: BoutiqueDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.boutique.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
