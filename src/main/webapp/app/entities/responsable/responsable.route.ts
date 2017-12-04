import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ResponsableComponent } from './responsable.component';
import { ResponsableDetailComponent } from './responsable-detail.component';
import { ResponsablePopupComponent } from './responsable-dialog.component';
import { ResponsableDeletePopupComponent } from './responsable-delete-dialog.component';

@Injectable()
export class ResponsableResolvePagingParams implements Resolve<any> {

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

export const responsableRoute: Routes = [
    {
        path: 'responsable',
        component: ResponsableComponent,
        resolve: {
            'pagingParams': ResponsableResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.responsable.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'responsable/:id',
        component: ResponsableDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.responsable.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const responsablePopupRoute: Routes = [
    {
        path: 'responsable-new',
        component: ResponsablePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.responsable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'responsable/:id/edit',
        component: ResponsablePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.responsable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'responsable/:id/delete',
        component: ResponsableDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.responsable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
