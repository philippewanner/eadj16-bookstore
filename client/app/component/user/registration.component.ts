import {Registration} from "../../core/registration";
import {Component} from "@angular/core";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";

@Component({
    selector: 'registration',
    templateUrl: "app/component/user/registration.component.html"
})
export class RegistrationComponent {

    registration: Registration = new Registration();
    password: string;
    repeatPassword: string;

    errorMessage: string;

    constructor(private service: UserService, private router: Router) {
    }

    public onSubmit(): void {
        if (this.password != this.repeatPassword) {
            this.errorMessage = "Passwords are not identical";
            return;
        }

        this.registration.password = this.password;
        this.service.register(this.registration)
            .then(response => {
                this.service.getCustomer(response)
                    .then(response => {
                        if (response != null) {
                            this.service.customer = response;
                            this.router.navigate((["/catalog"]));
                        } else {
                            this.errorMessage = "Unable to register";
                        }
                    });
            })
            .catch(error => this.errorMessage = "Unable to register");
    }
}