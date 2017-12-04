import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ResavisionTypeSiteModule } from './type-site/type-site.module';
import { ResavisionTypeBoutiqueModule } from './type-boutique/type-boutique.module';
import { ResavisionVilleModule } from './ville/ville.module';
import { ResavisionBoutiqueModule } from './boutique/boutique.module';
import { ResavisionResponsableModule } from './responsable/responsable.module';
import { ResavisionEmployeModule } from './employe/employe.module';
import { ResavisionClientModule } from './client/client.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ResavisionTypeSiteModule,
        ResavisionTypeBoutiqueModule,
        ResavisionVilleModule,
        ResavisionBoutiqueModule,
        ResavisionResponsableModule,
        ResavisionEmployeModule,
        ResavisionClientModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResavisionEntityModule {}
