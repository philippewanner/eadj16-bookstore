import {Component, Input} from "@angular/core";
import {Customer} from "../../core/customer";
import {CreditCard} from "../../core/credit-card";
import {CreditCardType} from "../../core/credit-card-type";

@Component({
    selector: "user-edit",
    templateUrl: "./user-edit.component.html"
})
export class UserEditComponent {

    creditCard: CreditCard = new CreditCard();
    creditCardTypes: CreditCardType[] = [];

    minYear: number = new Date().getFullYear();
    maxYear: number = new Date().getFullYear() + 10;

    creditCardPattern: string = "^5[1-5][0-9]{14}$";

    @Input()
    customer: Customer;

    constructor() {
        this.creditCardTypes.push(new CreditCardType("MASTER_CARD", "MasterCard"));
        this.creditCardTypes.push(new CreditCardType("VISA", "Visa"));
    }
}