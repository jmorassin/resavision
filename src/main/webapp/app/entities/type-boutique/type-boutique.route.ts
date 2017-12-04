import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TypeBoutiqueComponent } from './type-boutique.component';
import { TypeBoutiqueDetailComponent } from './type-boutique-detail.component';
import { TypeBoutiquePopupComponent } from './type-boutique-dialog.component';
import { TypeBoutiqueDeletePopupComponent } from './type-boutique-delete-dialog.component';

@Injectable()
export class TypeBoutiqueResolvePagingParams implements Resolve<any> {

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

export const typeBoutiqueRoute: Routes = [
    {
        path: 'type-boutique',
        component: TypeBoutiqueComponent,
        resolve: {
            'pagingParams': TypeBoutiqueResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeBoutique.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'type-boutique/:id',
        component: TypeBoutiqueDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeBoutique.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const typeBoutiquePopupRoute: Routes = [
    {
        path: 'type-boutique-new',
        component: TypeBoutiquePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeBoutique.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'type-boutique/:id/edit',
        component: TypeBoutiquePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeBoutique.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'type-boutique/:id/delete',
        component: TypeBoutiqueDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeBoutique.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
