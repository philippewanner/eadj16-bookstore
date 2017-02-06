export enum CreditCardType {
    AMERICAN_EXPRESS, MASTER_CARD, VISA
}

export class CreditCard {
    type: CreditCardType;
    number: string;
    expirationMonth: number;
    expirationYear: number;
}