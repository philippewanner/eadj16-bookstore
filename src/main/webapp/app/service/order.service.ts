import {Injectable} from "@angular/core";
import {ShoppingCartService} from "./shopping-cart.service";
import {UserService} from "./user.service";
import {OrderRequest} from "../core/order-request";
import {OrderInfo} from "../core/order-info";
import {BOOKSTORE_REST_URL} from "../main";
import {Http, Headers} from "@angular/http";
import {ShoppingCart} from "../component/shopping-cart/shopping-cart";
import {URLSearchParams, QueryEncoder} from '@angular/http';

const ORDER_URL: string = "/orders/";

@Injectable()
export class OrderService {

    constructor(private shoppingCartService: ShoppingCartService, private http: Http, private userService: UserService) {
        console.log("constructor OrderService");
    }

    public placeOrder(): Promise<Array<OrderRequest>> {

        let orderRequest = this.convertShoppingCartToOrderRequest(this.shoppingCartService.getShoppingCart());

        let url: string = BOOKSTORE_REST_URL + ORDER_URL;
        let headers: Headers = new Headers({"Content-Type": "application/json"});
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

    public searchOrdersByCustomerAndYear(customerNr: number, year: number): Promise<Array<OrderInfo>> {

        console.log("OrderService: searchOrdersByCustomerAndYear("+customerNr+","+year+")");

        let url = BOOKSTORE_REST_URL + ORDER_URL;

        let params = new URLSearchParams();
        params.set('number', customerNr.toString());
        params.set('year', year.toString());

        return this.http.get(url, {search: params}).toPromise()
            .then(response => {
                console.log("got any orders");
                return response.json() as Array<OrderInfo>[];
            })
            .catch(error => {
                console.error("OrderService: " + error);
                return Promise.reject(error);
            });
}

    private convertShoppingCartToOrderRequest(shoppingCart: ShoppingCart): OrderRequest {

        let customerNr = this.userService.customer.number;
        let orderRequest: OrderRequest = new OrderRequest(customerNr, shoppingCart.items);

        return orderRequest;
    }
}