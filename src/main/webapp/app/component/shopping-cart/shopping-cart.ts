import {ShoppingCartItem} from "./shopping-cart-item";
import {Book} from "../../core/book"
import {BookInfo} from "../../core/book-info";

export class ShoppingCart {

    public totalPrize: number;
    public items: Array<ShoppingCartItem> = [];

    constructor() { this.updateTotalPrice();   }

    public addBook(book: BookInfo, quantity: number = 1) {
        let newItem: ShoppingCartItem = new ShoppingCartItem(book, quantity);

        let added: Boolean = false;

        for (let entry of this.items) {
            if (entry.bookInfo.isbn === newItem.bookInfo.isbn) {
                entry.quantity += newItem.quantity;
                added = true;
                break;
            }
        }

        if (!added) {
            this.items.push(newItem);
        }

        this.updateTotalPrice();
    }

    public removeBook(book: BookInfo) {
        let index: number = 0;
        for (index = 0; index < this.items.length; index++) {
            if (this.items[index].bookInfo.isbn === book.isbn) {

                this.items.splice(index,1);
                break;
            }
        }

        this.updateTotalPrice();
    }

    public updateTotalPrice() {
        let tempPrice: number;
        tempPrice = 0;
        for (let entry of this.items) {
            tempPrice += entry.getTotalPrice();
        }

        console.log("updateTotalPrice to " + tempPrice);

        this.totalPrize = tempPrice;
    }
}
