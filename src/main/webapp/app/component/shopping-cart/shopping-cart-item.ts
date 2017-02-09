import {Book} from "../../core/book";

export class ShoppingCartItem {

    constructor(public book: Book, public quantity: number) {}

    public getTotalPrice(): number {return this.quantity * this.book.price;}
}

