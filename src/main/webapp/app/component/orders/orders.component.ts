import {Component, Input} from "@angular/core";
import {OrderService} from "../../service/order.service";
import {UserService} from "../../service/user.service";
import {OrderInfo} from "../../core/order-info";

@Component({
    selector: 'orders',
    templateUrl: 'app/component/orders/orders.component.html',
    styleUrls: ['app/component/orders/orders.component.css']
})
export class OrdersComponent {

    private orders = new Array<OrderInfo>();

    @Input()
    private selectedYear = 2017;

    constructor(private orderService: OrderService,
                private userService: UserService) {

        this.getOrdersByCurrentCustomerAndYear(this.selectedYear);
    }

    public getOrdersByCurrentCustomerAndYear(year: number) {

        if (this.userService.customer != null &&
            this.userService.customer.number != null) {

            this.orderService.searchOrdersByCustomerAndYear(this.userService.customer.number, year)
                .then((result: Array<OrderInfo>) => {
                    console.log("here");
                    this.orders = result;
                })
                .catch((error: any) => {
                    console.log("orders error: " + error);
                });
        }
    }

    public onClick(event: any) {
        console.log("clicked on year input");

        this.getOrdersByCurrentCustomerAndYear(this.selectedYear)
    }

    public onCancel(orderNr: number){

        console.log("clicked on cancel order number "+orderNr);

        this.getOrdersByCurrentCustomerAndYear(this.selectedYear)
    }

    public isValid(orderInfo: OrderInfo): boolean{
        return orderInfo.status === "PROCESSING";
    }

    public setStyles(isVisible: boolean) {

        let styles = {
            // CSS property names
            'font-style':  isVisible ? 'italic' : 'normal',
            'opacity': isVisible ? 1: 0.4
        };
        return styles;
    }
}