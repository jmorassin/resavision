import { BaseEntity } from './../../shared';

export class TypeSite implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public listTypeBoutiques?: BaseEntity[],
    ) {
    }
}
