import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TypeSiteComponent } from './type-site.component';
import { TypeSiteDetailComponent } from './type-site-detail.component';
import { TypeSitePopupComponent } from './type-site-dialog.component';
import { TypeSiteDeletePopupComponent } from './type-site-delete-dialog.component';

@Injectable()
export class TypeSiteResolvePagingParams implements Resolve<any> {

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

export const typeSiteRoute: Routes = [
    {
        path: 'type-site',
        component: TypeSiteComponent,
        resolve: {
            'pagingParams': TypeSiteResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeSite.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'type-site/:id',
        component: TypeSiteDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeSite.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const typeSitePopupRoute: Routes = [
    {
        path: 'type-site-new',
        component: TypeSitePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeSite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'type-site/:id/edit',
        component: TypeSitePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeSite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'type-site/:id/delete',
        component: TypeSiteDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'resavisionApp.typeSite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
