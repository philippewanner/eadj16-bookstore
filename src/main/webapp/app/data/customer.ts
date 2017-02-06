import {Address} from "./address";
import {CreditCard} from "./credit-card";

export class Customer {

    number: number;
    firstName: string;
    lastName: string;
    email: string;

    address: Address = new Address();
    creditCard: CreditCard = new CreditCard();

    version: number;
}