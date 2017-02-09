import {Component, Input, Output, EventEmitter} from '@angular/core';
import {Book} from "../../core/book"
import {Router} from "@angular/router";
import {CatalogService} from "../../service/catalog.service";
import {ShoppingCartService} from "../../service/shopping-cart.service";

@Component({
    selector: 'book-list',
    templateUrl: 'app/component/booklist/booklist.component.html'
})
export class BookListComponent {

    @Input() private books: Array<Book> = [];
    //@Output() private onSelect = new EventEmitter<Book>();

    private elementName: string = "Found books";

    constructor(private router: Router,private shoppingCartService: ShoppingCartService) {
        console.log("constructor BookListComponent")
    }

    public select(book: Book):void {
        console.log("call select");

        this.router.navigate((['/book-details',book.isbn]));
    }

    public addBookToCart(book: Book):void{
        console.log("add book to cart");

        this.shoppingCartService.addBook(book, 1);
    }
}