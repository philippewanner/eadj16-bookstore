import {Component, Input} from "@angular/core";
import {Customer} from "../../data/customer";

@Component({
    selector: "user-edit",
    templateUrl: "app/component/user/user-edit.component.html"
})
export class UserEditComponent {

    @Input()
    customer: Customer;
}