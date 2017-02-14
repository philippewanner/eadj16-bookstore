import {Component, OnInit, Input} from '@angular/core';
import {ShoppingCart} from "./shopping-cart";
import {ShoppingCartItem} from "./shopping-cart-item";
import {ShoppingCartService} from "../../service/shopping-cart.service";

@Component({
    selector: 'shopping-cart',
    templateUrl: 'app/component/shopping-cart/shopping-cart.component.html'
})
export class ShoppingCartComponent {

    @Input()
    private isOrdering: boolean;

    private shoppingCart: ShoppingCart = new ShoppingCart();

    private elementName: string = "Shopping Cart";


    constructor(private shoppingCartService: ShoppingCartService) {
        this.shoppingCart = this.shoppingCartService.getShoppingCart();
    }


    public handleQuantityChange(item: ShoppingCartItem) {
        if (item.quantity < 0) {
            item.quantity = 0;
        }

        //this.shoppingCartService.addBook(item.book, item.quantity);
        this.shoppingCart = this.shoppingCartService.getShoppingCart();

        this.shoppingCart.updateTotalPrice();
    }

    public deleteBook(item: ShoppingCartItem): void {
        console.log("deleteBook " + item.book.title);

        this.shoppingCartService.removeBook(item.book);

        this.shoppingCart = this.shoppingCartService.getShoppingCart();
        this.shoppingCart.updateTotalPrice();
    }

}