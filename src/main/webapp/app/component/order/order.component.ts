import {Component} from "@angular/core";
import {OrderService} from "../../service/order.service";
import {ShoppingCartService} from "../../service/shopping-cart.service";

@Component({
    selector: 'order',
    templateUrl: 'app/component/order/order.component.html'
})
export class OrderComponent {

    public ordered: boolean=false;

    constructor(private orderService: OrderService, private shoppingCartService: ShoppingCartService){
    }

    public placeOrder(): void {
        console.log("place order");

        this.shoppingCartService.clearShoppingCart();
        this.ordered=true;
    }


}