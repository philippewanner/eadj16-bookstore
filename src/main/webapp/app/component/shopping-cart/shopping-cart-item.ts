import {Book} from "../../core/book";
import {BookInfo} from "../../core/book-info";

export class ShoppingCartItem {

    constructor(public book: BookInfo, public quantity: number) {}

    public getTotalPrice(): number {return this.quantity * this.book.price;}
}

