import { BaseEntity } from './../../shared';

export class Ville implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public codePostal?: string,
    ) {
    }
}
