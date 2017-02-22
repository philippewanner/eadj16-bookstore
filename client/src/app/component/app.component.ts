import {Component} from "@angular/core";
import {UserService} from "../service/user.service";
import {ShoppingCartService} from "../service/shopping-cart.service";

@Component({
    selector: "app",
    templateUrl: "./app.component.html"
})
export class AppComponent {

    constructor(private userService: UserService, private shoppingCartService: ShoppingCartService) {
    }
}