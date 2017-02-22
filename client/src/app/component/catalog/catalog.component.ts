import {Component} from "@angular/core";
import {Book} from "../../core/book";
import {CatalogService} from "../../service/catalog.service";
import {BookInfo} from "../../core/book-info";
@Component({
    selector: 'catalog',
    templateUrl: "./catalog.component.html"
})
export class CatalogComponent {
    private elementName: string = "Catalog";

    private searching: string = "";

    public keywords: string = "";

    public foundBooks: Array<BookInfo> = [];

    public selectedBook: Book = null;

    public keywordsRequired: String="";

    public noBooksFound:string="";

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

        this.searching = "searching...";

        this.noBooksFound="";

        if (this.keywords.length === 0) {
            this.keywordsRequired = "please enter any keywords";
            return;
        }

        this.keywordsRequired = "";

        this.catalogService.searchBooksAsync(this.keywords).then((result: Book[]) => {

            this.searching = "";

            this.foundBooks = result;

            if (this.foundBooks.length > 0) {

                this.catalogService.lastBooks = this.foundBooks;


            }else{this.noBooksFound="no books found";}

        });
    }
}