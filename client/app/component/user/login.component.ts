import {Component} from "@angular/core";
import {UserService} from "../../service/user.service";
import {Router, ActivatedRoute} from "@angular/router";

@Component({
    selector: "login",
    templateUrl: "app/component/user/login.component.html"
})
export class LoginComponent {

    private email: string;
    private password: string;
    private errorMessage: string;

    constructor(private service: UserService, private router: Router, private activatedRoute: ActivatedRoute) {
        console.log(this.activatedRoute.toString());
    }

    public onSubmit(): void {
        this.service.authenticate(this.email, this.password)
            .then(response => {
                if (response != null) {
                    this.service.getCustomer(response)
                        .then(response => {
                            if (response != null) {
                                this.service.customer = response;

                                if (this.activatedRoute.toString().includes("url:'login") && this.activatedRoute.toString().includes("path:'login")){
                                    console.log("jump to catalog");

                                    this.router.navigate((["/catalog"]));

                                }else{
                                    console.log("stay on route");
                                }
                            } else {
                                this.errorMessage = "Unable to login with this username/password";
                            }
                        });
                } else {
                    this.errorMessage = "Unable to login with this username/password";
                }
            });
    }
}