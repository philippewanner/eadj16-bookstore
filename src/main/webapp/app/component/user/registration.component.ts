import {Registration} from "../../core/registration";
import {Component} from "@angular/core";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";

@Component({
    templateUrl: "app/component/user/registration.component.html"
})
export class RegistrationComponent {

    registration: Registration = new Registration();
    password: string;
    repeatPassword: string;

    errorMessage: string;

    constructor(private service: UserService, private router: Router) {}

    public onSubmit(): void {
        this.service.register(this.registration)
            .then(response => this.router.navigate((["/catalog"])))
            .catch(error => this.errorMessage = "Unable to register");
    }
}