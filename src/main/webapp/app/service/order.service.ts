import {Injectable} from "@angular/core";
import {ShoppingCartService} from "./shopping-cart.service";
import {OrderRequest} from "../core/order-request";

@Injectable()
export class OrderService {

    constructor(private shoppingCartService: ShoppingCartService) {
        console.log("constructor OrderService");
    }

    public order(orderRequest: OrderRequest){

    }
}