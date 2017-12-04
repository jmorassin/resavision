import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResavisionSharedModule } from '../../shared';
import { ResavisionAdminModule } from '../../admin/admin.module';
import {
    EmployeService,
    EmployePopupService,
    EmployeComponent,
    EmployeDetailComponent,
    EmployeDialogComponent,
    EmployePopupComponent,
    EmployeDeletePopupComponent,
    EmployeDeleteDialogComponent,
    employeRoute,
    employePopupRoute,
    EmployeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...employeRoute,
    ...employePopupRoute,
];

@NgModule({
    imports: [
        ResavisionSharedModule,
        ResavisionAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        EmployeComponent,
        EmployeDetailComponent,
        EmployeDialogComponent,
        EmployeDeleteDialogComponent,
        EmployePopupComponent,
        EmployeDeletePopupComponent,
    ],
    entryComponents: [
        EmployeComponent,
        EmployeDialogComponent,
        EmployePopupComponent,
        EmployeDeleteDialogComponent,
        EmployeDeletePopupComponent,
    ],
    providers: [
        EmployeService,
        EmployePopupService,
        EmployeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResavisionEmployeModule {}
