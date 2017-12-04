import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResavisionSharedModule } from '../../shared';
import { ResavisionAdminModule } from '../../admin/admin.module';
import {
    ClientService,
    ClientPopupService,
    ClientComponent,
    ClientDetailComponent,
    ClientDialogComponent,
    ClientPopupComponent,
    ClientDeletePopupComponent,
    ClientDeleteDialogComponent,
    clientRoute,
    clientPopupRoute,
    ClientResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...clientRoute,
    ...clientPopupRoute,
];

@NgModule({
    imports: [
        ResavisionSharedModule,
        ResavisionAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ClientComponent,
        ClientDetailComponent,
        ClientDialogComponent,
        ClientDeleteDialogComponent,
        ClientPopupComponent,
        ClientDeletePopupComponent,
    ],
    entryComponents: [
        ClientComponent,
        ClientDialogComponent,
        ClientPopupComponent,
        ClientDeleteDialogComponent,
        ClientDeletePopupComponent,
    ],
    providers: [
        ClientService,
        ClientPopupService,
        ClientResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResavisionClientModule {}
