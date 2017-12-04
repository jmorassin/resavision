import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResavisionSharedModule } from '../../shared';
import {
    BoutiqueService,
    BoutiquePopupService,
    BoutiqueComponent,
    BoutiqueDetailComponent,
    BoutiqueDialogComponent,
    BoutiquePopupComponent,
    BoutiqueDeletePopupComponent,
    BoutiqueDeleteDialogComponent,
    boutiqueRoute,
    boutiquePopupRoute,
    BoutiqueResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...boutiqueRoute,
    ...boutiquePopupRoute,
];

@NgModule({
    imports: [
        ResavisionSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BoutiqueComponent,
        BoutiqueDetailComponent,
        BoutiqueDialogComponent,
        BoutiqueDeleteDialogComponent,
        BoutiquePopupComponent,
        BoutiqueDeletePopupComponent,
    ],
    entryComponents: [
        BoutiqueComponent,
        BoutiqueDialogComponent,
        BoutiquePopupComponent,
        BoutiqueDeleteDialogComponent,
        BoutiqueDeletePopupComponent,
    ],
    providers: [
        BoutiqueService,
        BoutiquePopupService,
        BoutiqueResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResavisionBoutiqueModule {}
