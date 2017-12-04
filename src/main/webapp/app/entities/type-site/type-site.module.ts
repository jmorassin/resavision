import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResavisionSharedModule } from '../../shared';
import {
    TypeSiteService,
    TypeSitePopupService,
    TypeSiteComponent,
    TypeSiteDetailComponent,
    TypeSiteDialogComponent,
    TypeSitePopupComponent,
    TypeSiteDeletePopupComponent,
    TypeSiteDeleteDialogComponent,
    typeSiteRoute,
    typeSitePopupRoute,
    TypeSiteResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...typeSiteRoute,
    ...typeSitePopupRoute,
];

@NgModule({
    imports: [
        ResavisionSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TypeSiteComponent,
        TypeSiteDetailComponent,
        TypeSiteDialogComponent,
        TypeSiteDeleteDialogComponent,
        TypeSitePopupComponent,
        TypeSiteDeletePopupComponent,
    ],
    entryComponents: [
        TypeSiteComponent,
        TypeSiteDialogComponent,
        TypeSitePopupComponent,
        TypeSiteDeleteDialogComponent,
        TypeSiteDeletePopupComponent,
    ],
    providers: [
        TypeSiteService,
        TypeSitePopupService,
        TypeSiteResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResavisionTypeSiteModule {}
