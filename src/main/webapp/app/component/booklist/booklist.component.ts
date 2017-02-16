import {Component, Input, Output, EventEmitter, OnInit} from '@angular/core';
import {Book} from "../../core/book"
import {Router} from "@angular/router";
import {CatalogService} from "../../service/catalog.service";
import {ShoppingCartService} from "../../service/shopping-cart.service";
import {BookInfo} from "../../core/book-info";
import {Pager} from "../../core/pager";

@Component({
    selector: 'book-list',
    templateUrl: 'app/component/booklist/booklist.component.html'
})
export class BookListComponent implements OnInit{

    @Input() private books: Array<Book> = [];
    //@Output() private onSelect = new EventEmitter<Book>();

    private pagedBooks: Array<Book>;

    private elementName: string = "Found books";
    private previous:string="<<";
    private next:string=">>";

    private pager:Pager;

    constructor(private router: Router,private shoppingCartService: ShoppingCartService) {
        console.log("constructor BookListComponent");

    }

    public ngOnInit():void{
        this.elementName ="Found " + this.books.length + " books";
        this.pager=new Pager(this.books);
        this.nextPage();
    }

    public previousPage():void{
        this.pagedBooks=this.pager.getPrevious();
    }

    public nextPage():void{
        this.pagedBooks=this.pager.getNext();
    }

    public select(book: BookInfo):void {
        console.log("call select");

        this.router.navigate((['/book-details',book.isbn]));
    }

    public addBookToCart(book: BookInfo):void{
        console.log("add book to cart");

        this.shoppingCartService.addBook(book, 1);
    }
}