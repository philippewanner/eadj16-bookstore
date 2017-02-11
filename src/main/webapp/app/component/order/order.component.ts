import {Component} from "@angular/core";
import {ShoppingCartService} from "../../service/shopping-cart.service";

@Component({
    selector: 'order',
    templateUrl: 'app/component/order/order.component.html'
})
export class OrderComponent {

    constructor(private shoppingCartService:ShoppingCartService){}
}