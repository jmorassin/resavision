import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResavisionSharedModule } from '../../shared';
import { ResavisionAdminModule } from '../../admin/admin.module';
import {
    ResponsableService,
    ResponsablePopupService,
    ResponsableComponent,
    ResponsableDetailComponent,
    ResponsableDialogComponent,
    ResponsablePopupComponent,
    ResponsableDeletePopupComponent,
    ResponsableDeleteDialogComponent,
    responsableRoute,
    responsablePopupRoute,
    ResponsableResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...responsableRoute,
    ...responsablePopupRoute,
];

@NgModule({
    imports: [
        ResavisionSharedModule,
        ResavisionAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ResponsableComponent,
        ResponsableDetailComponent,
        ResponsableDialogComponent,
        ResponsableDeleteDialogComponent,
        ResponsablePopupComponent,
        ResponsableDeletePopupComponent,
    ],
    entryComponents: [
        ResponsableComponent,
        ResponsableDialogComponent,
        ResponsablePopupComponent,
        ResponsableDeleteDialogComponent,
        ResponsableDeletePopupComponent,
    ],
    providers: [
        ResponsableService,
        ResponsablePopupService,
        ResponsableResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResavisionResponsableModule {}
