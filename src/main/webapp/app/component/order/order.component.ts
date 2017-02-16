import {Component} from "@angular/core";
import {OrderService} from "../../service/order.service";
import {ShoppingCartService} from "../../service/shopping-cart.service";
import {UserService} from "../../service/user.service";

@Component({
    selector: 'order',
    templateUrl: 'app/component/order/order.component.html'
})
export class OrderComponent {

    private ordered: boolean;
    private isUserEditing: boolean;

    constructor(private orderService: OrderService,
                private shoppingCartService: ShoppingCartService,
                private userService: UserService) {
    }

    public order(): void {
        console.log("place the order");

        this.orderService.placeOrder();

        this.shoppingCartService.clearShoppingCart();
        this.ordered = true;
    }

    public editCustomer(): void {
        this.isUserEditing = true;
    }

    public saveCustomer(): void {
        this.isUserEditing = false;
    }
}