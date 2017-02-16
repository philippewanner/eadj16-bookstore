import {Injectable} from "@angular/core";
import {ShoppingCartService} from "./shopping-cart.service";
import {UserService} from "./user.service";
import {OrderRequest} from "../core/order-request";
import {BOOKSTORE_REST_URL} from "../main";
import {Http, Headers} from "@angular/http";
import {ShoppingCart} from "../component/shopping-cart/shopping-cart";

const ORDER_URL: string = "/orders/";

@Injectable()
export class OrderService {

    constructor(private shoppingCartService: ShoppingCartService, private http: Http, private userService: UserService) {
        console.log("constructor OrderService");
    }

    public placeOrder(): Promise<Array<OrderRequest>> {

        let orderRequest = this.convertShoppingCartToOrderRequest(this.shoppingCartService.getShoppingCart());

        let url: string = BOOKSTORE_REST_URL + ORDER_URL;
        let headers: Headers = new Headers({"Content-Type": "application/xml"});
        return this.http.post(url, orderRequest, {headers}).toPromise()
            .then(response => {
                if (response.status === 201) {
                    console.log("placeOrderResponse="+response.json())
                    return response.json() as number;
                }
            })
            .catch(error => {
                console.log("OrderService: " + error);
                return Promise.reject(error);
            });
    }

    private convertShoppingCartToOrderRequest(shoppingCart: ShoppingCart): OrderRequest {

        let customerNr = this.userService.customer.number;
        let orderRequest: OrderRequest = new OrderRequest(customerNr, shoppingCart.items);

        return orderRequest;
    }
}