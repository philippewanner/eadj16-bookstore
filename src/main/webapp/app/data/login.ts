export enum UserGroup {
    CUSTOMER, EMPLOYEE
}

export interface Login {
    userName: string;
    password: string;
    group: UserGroup;
}