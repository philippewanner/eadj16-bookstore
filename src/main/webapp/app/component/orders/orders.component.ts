import {Component} from "@angular/core";
import {OrderService} from "../../service/order.service";
import {UserService} from "../../service/user.service";
import {OrderInfo} from "../../core/order-info";

@Component({
    selector: 'orders',
    templateUrl: 'app/component/orders/orders.component.html'
})
export class OrdersComponent {

    private orders = new Array<OrderInfo>();
    private selectedYear = 2017;

    constructor(private orderService: OrderService,
                private userService: UserService) {

        this.getOrdersByCurrentCustomerAndYear();
    }

    public getOrdersByCurrentCustomerAndYear() {

        if (this.userService.customer != null &&
            this.userService.customer.number != null) {

            this.orderService.searchOrdersByCustomerAndYear(this.userService.customer.number, this.selectedYear)
                .then((result: Array<OrderInfo>) => {
                    console.log("here");
                    this.orders = result;
                })
                .catch((error: any) => {
                    console.log("orders error: " + error);
                });
        }
    }
}