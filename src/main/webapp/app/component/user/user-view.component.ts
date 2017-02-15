import {Component, Input} from "@angular/core";
import {Customer} from "../../core/customer";

@Component({
    selector: "user-view",
    templateUrl: "app/component/user/user-view.component.html"
})
export class UserViewComponent {

    @Input()
    customer: Customer;

    constructor() {
    }
}