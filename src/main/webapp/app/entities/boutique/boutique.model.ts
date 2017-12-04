import { BaseEntity } from './../../shared';

export class Boutique implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public logoContentType?: string,
        public logo?: any,
        public url?: string,
        public numTelephone?: string,
        public mail?: string,
        public adresse1?: string,
        public adresse2?: string,
        public employes?: BaseEntity[],
        public ville?: BaseEntity,
        public typeBoutiques?: BaseEntity[],
        public responsable?: BaseEntity,
    ) {
    }
}
