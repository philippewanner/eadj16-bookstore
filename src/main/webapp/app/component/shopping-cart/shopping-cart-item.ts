import {BookInfo} from "../../core/book-info";

export class ShoppingCartItem {

    constructor(public bookInfo: BookInfo, public quantity: number) {}

    public getTotalPrice(): number {return this.quantity * this.bookInfo.price;}
}

