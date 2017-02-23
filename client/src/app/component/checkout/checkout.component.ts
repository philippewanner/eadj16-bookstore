import {Component} from "@angular/core";
import {OrderService} from "../../service/order.service";
import {ShoppingCartService} from "../../service/shopping-cart.service";
import {UserService} from "../../service/user.service";
import {OrderInfo} from "../../core/order-info";
import {ShoppingCartItem} from "../shopping-cart/shopping-cart-item";

@Component({
    selector: 'order',
    templateUrl: './checkout.component.html'
})
export class CheckoutComponent {

    private ordered: boolean;
    private isUserEditing: boolean;

    private orderedItems:Array<ShoppingCartItem>=[];

    constructor(private orderService: OrderService,
                private shoppingCartService: ShoppingCartService,
                private userService: UserService) {
    }

    public order(): void {
        console.log("place the order");

        this.orderService.placeOrder();

        this.orderedItems = this.shoppingCartService.getShoppingCart().items;

        this.shoppingCartService.clearShoppingCart();
        this.ordered = true;
    }

    public editCustomer(): void {
        this.isUserEditing = true;
    }

    public saveCustomer(): void {
        this.userService.updateCustomer(this.userService.customer)
          .then(response => {
                if (response != null) {
                    this.userService.customer = response;
                    this.isUserEditing = false;
                } else {
                    alert("Unable to save customer");
                }
          })
          .catch(error => alert("Unable to save customer"));
    }
}