import {Component} from "@angular/core";
import {ShoppingCartService} from "../../service/shopping-cart.service";
import {UserService} from "../../service/user.service";

@Component({
    selector: 'order',
    templateUrl: 'app/component/order/order.component.html'
})
export class OrderComponent {

    public ordered: boolean=false;

    constructor(private shoppingCartService:ShoppingCartService, private userService: UserService){}

    public placeOrder(): void {
        console.log("place order");

        this.shoppingCartService.clearShoppingCart();
        this.ordered=true;
    }
}