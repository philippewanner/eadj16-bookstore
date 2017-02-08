import {Component} from "@angular/core";
import {UserService} from "../service/user.service";

@Component({
    selector: "app",
    templateUrl: "app/component/app.component.html"
})
export class AppComponent {

    constructor(public userService: UserService) {}
}