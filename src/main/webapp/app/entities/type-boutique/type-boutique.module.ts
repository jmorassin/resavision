import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResavisionSharedModule } from '../../shared';
import {
    TypeBoutiqueService,
    TypeBoutiquePopupService,
    TypeBoutiqueComponent,
    TypeBoutiqueDetailComponent,
    TypeBoutiqueDialogComponent,
    TypeBoutiquePopupComponent,
    TypeBoutiqueDeletePopupComponent,
    TypeBoutiqueDeleteDialogComponent,
    typeBoutiqueRoute,
    typeBoutiquePopupRoute,
    TypeBoutiqueResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...typeBoutiqueRoute,
    ...typeBoutiquePopupRoute,
];

@NgModule({
    imports: [
        ResavisionSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TypeBoutiqueComponent,
        TypeBoutiqueDetailComponent,
        TypeBoutiqueDialogComponent,
        TypeBoutiqueDeleteDialogComponent,
        TypeBoutiquePopupComponent,
        TypeBoutiqueDeletePopupComponent,
    ],
    entryComponents: [
        TypeBoutiqueComponent,
        TypeBoutiqueDialogComponent,
        TypeBoutiquePopupComponent,
        TypeBoutiqueDeleteDialogComponent,
        TypeBoutiqueDeletePopupComponent,
    ],
    providers: [
        TypeBoutiqueService,
        TypeBoutiquePopupService,
        TypeBoutiqueResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResavisionTypeBoutiqueModule {}
