import { BaseEntity } from './../../shared';

export class TypeBoutique implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public typeSite?: BaseEntity,
        public listBoutiques?: BaseEntity[],
    ) {
    }
}
