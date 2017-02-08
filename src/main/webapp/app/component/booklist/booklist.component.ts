import {Component, Input, Output, EventEmitter} from '@angular/core';
import {Book} from "../../core/book"
import {Router} from "@angular/router";

@Component({
    selector: 'book-list',
    templateUrl: 'app/component/booklist/booklist.component.html'
})
export class BookListComponent {

    @Input() private books: Array<Book> = [];
    //@Output() private onSelect = new EventEmitter<Book>();

    private elementName: string = "Found books";

    constructor(private router: Router) {
        console.log("constructor BookListComponent")
    }

    public select(book: Book) {
        console.log("call select");

        this.router.navigate((['/book-details',book.isbn]));
    }
}