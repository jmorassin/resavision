import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResavisionSharedModule } from '../../shared';
import {
    VilleService,
    VillePopupService,
    VilleComponent,
    VilleDetailComponent,
    VilleFormComponent,
    VilleDialogComponent,
    VillePopupComponent,
    VilleDeletePopupComponent,
    VilleDeleteDialogComponent,
    villeRoute,
    villePopupRoute,
    VilleResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...villeRoute,
    ...villePopupRoute,
];

@NgModule({
    imports: [
        ResavisionSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        VilleComponent,
        VilleDetailComponent,
        VilleFormComponent,
        VilleDialogComponent,
        VilleDeleteDialogComponent,
        VillePopupComponent,
        VilleDeletePopupComponent,
    ],
    entryComponents: [
        VilleComponent,
        VilleDialogComponent,
        VillePopupComponent,
        VilleDeleteDialogComponent,
        VilleDeletePopupComponent,
    ],
    providers: [
        VilleService,
        VillePopupService,
        VilleResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResavisionVilleModule {}
