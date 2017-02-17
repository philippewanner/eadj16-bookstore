import {Component} from "@angular/core";
import {OrderService} from "../../service/order.service";
import {ShoppingCartService} from "../../service/shopping-cart.service";
import {UserService} from "../../service/user.service";
import {OrderInfo} from "../../core/order-info";

@Component({
    selector: 'order',
    templateUrl: 'app/component/checkout/checkout.component.html'
})
export class CheckoutComponent {

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

        //let orderInfos: Array<OrderInfo> = this.orderService.searchOrdersByCustomerAndYear(this.userService.customer.number, 2017);
        //console.log(orderInfos);
    }

    public editCustomer(): void {
        this.isUserEditing = true;
    }

    public saveCustomer(): void {
        this.isUserEditing = false;
    }
}