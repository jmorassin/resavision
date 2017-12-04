import { BaseEntity, User } from './../../shared';

export const enum Civilite {
    'MME',
    'MELLE',
    'MR'
}

export class Client implements BaseEntity {
    constructor(
        public id?: number,
        public civilite?: Civilite,
        public checkTel?: boolean,
        public chekMail?: boolean,
        public user?: User,
    ) {
        this.checkTel = false;
        this.chekMail = false;
    }
}
