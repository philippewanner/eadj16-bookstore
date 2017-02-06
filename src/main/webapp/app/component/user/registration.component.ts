import {Registration} from "../../data/registration";
import {Component} from "@angular/core";
import {UserService} from "../../service/user.service";

@Component({
    templateUrl: "app/component/user/registration.component.html"
})
export class RegistrationComponent {

    registration: Registration = new Registration();
    password: string;
    repeatPassword: string;

    constructor(userService: UserService) {}
}