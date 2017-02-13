import {Component} from "@angular/core";
import {Book} from "../../core/book";
import {CatalogService} from "../../service/catalog.service";
import {BookInfo} from "../../core/book-info";
@Component({
    selector: 'catalog',
    templateUrl: "app/component/catalog/catalog.component.html"
})
export class CatalogComponent {
    private elementName: string = "Catalog";

    private searching: string = "";

    public keywords: string = "";

    public foundBooks: Array<BookInfo> = [];

    public numberFoundBooks: string = "";

    public selectedBook: Book = null;

    constructor(private catalogService: CatalogService) {
        console.log("constructor CatalogComponent");

        this.keywords = catalogService.lastKeywords;

        if (catalogService.lastBooks) {
            this.foundBooks = catalogService.lastBooks;
        }
    }

    public selectBook(book: Book) {
        console.log("public selectBook(book: Book) has been called");
        this.selectedBook = book;
    }

    public deselectBook() {
        this.selectedBook = null;
    }

    public search(): void {
        this.foundBooks = [];
        this.numberFoundBooks="";

        this.searching = "searching...";

        if (this.keywords.length === 0) {
            this.numberFoundBooks = "please enter any keywords";
            return;
        }

        this.catalogService.searchBooksAsync(this.keywords).then((result: Book[]) => {

            this.searching = "";

            this.foundBooks = result;

            if (this.foundBooks.length > 0) {

                this.catalogService.lastBooks = this.foundBooks;

                this.numberFoundBooks = this.foundBooks.length + " books found";
            } else {
                this.numberFoundBooks = "no books found";
            }

        });
    }
}