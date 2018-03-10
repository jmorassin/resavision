import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { VilleComponent } from './ville.component';
import { VilleDetailComponent } from './ville-detail.component';
import { VilleFormComponent } from './ville-form.component';
import { VillePopupComponent } from './ville-dialog.component';
import { VilleDeletePopupComponent } from './ville-delete-dialog.component';

@Injectable()
export class VilleResolvePagingParams implements Resolve<any> {

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

export const villeRoute: Routes = [
    {
        path: 'ville',
        component: VilleComponent,
        resolve: {
            'pagingParams': VilleResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.ville.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ville/:id',
        component: VilleDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.ville.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'insc_ville',
        component: VilleFormComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.ville.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'insc_ville/:id',
        component: VilleFormComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.ville.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const villePopupRoute: Routes = [
    {
        path: 'ville-new',
        component: VillePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.ville.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ville/:id/edit',
        component: VillePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.ville.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ville/:id/delete',
        component: VilleDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.ville.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
