import { BaseEntity, User } from './../../shared';

export const enum Civilite {
    'MME',
    'MELLE',
    'MR'
}

export class Responsable implements BaseEntity {
    constructor(
        public id?: number,
        public civilite?: Civilite,
        public checkTel?: boolean,
        public chekMail?: boolean,
        public user?: User,
        public boutique?: BaseEntity,
    ) {
        this.checkTel = false;
        this.chekMail = false;
    }
}
