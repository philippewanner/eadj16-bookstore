import {Component} from "@angular/core";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";

@Component({
    selector: "login",
    templateUrl: "app/component/user/login.component.html"
})
export class LoginComponent {

    private email: string;
    private password: string;
    private errorMessage: string;

    constructor(private service: UserService, private router: Router) {}

    public onSubmit(): void {
        this.service.authenticate(this.email, this.password)
            .then(response => {
                if (response) {
                    this.router.navigate((["/catalog"]));
                } else {
                    this.errorMessage = "Unable to login with this username/password";
                }
            });
    }
}