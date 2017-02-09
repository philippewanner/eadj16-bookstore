import {Injectable} from "@angular/core";
import {ShoppingCart} from "../component/shopping-cart/shopping-cart";
import {Book} from "../core/book";

@Injectable()
export class ShoppingCartService {

    private shoppingCart: ShoppingCart = new ShoppingCart();

    constructor() {
        console.log("constructor ShoppingCartService");
    }

    public addBook(book: Book, quantity: number = 1): void {
        this.shoppingCart.addBook(book, quantity);
    }

    public removeBook(book: Book): void {
        this.shoppingCart.removeBook(book);
    }

    public getShoppingCart(): ShoppingCart {
        return this.shoppingCart;
    }

    public clearShoppingCart(): void {
        this.shoppingCart = new ShoppingCart();
    }
}