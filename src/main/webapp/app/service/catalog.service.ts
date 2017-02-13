import {Injectable} from "@angular/core";
import {Book} from "../core/book";
import {BOOK_DATA} from "../core/book-data";
import {Http, Headers} from "@angular/http";

@Injectable()
export class CatalogService {

    public lastKeywords:string;
    public lastBooks:Book[];

    private baseUrl: String = "http://localhost:8080/bookstore/rest/books";

    constructor(private http: Http) {
        console.log("constructor CatalogService");
    }

    public findBook(isbn: string): Book {

        for (let book of BOOK_DATA) {
            if (book.isbn === isbn) {
                return book;
            }
        }

        throw "book not found";
    }

    public findBookAsync(isbn: string): Promise<Book> {

        return new Promise(function (resolve, reject) {

            let foundBook: Book = null;
            for (let book of BOOK_DATA) {
                if (book.isbn === isbn) {
                    foundBook = book;
                }
            }
            if (foundBook) {
                setTimeout(() => resolve(foundBook), 2000);
            } else {
                reject("book not found");
            }
        });

    }

    public searchBooks(keywords: string): Book[] {

        console.log("CatalogService: searchBooks");

        this.lastKeywords=keywords;

        let foundBooks: Book[] = [];
        let myKeywords: Array<string> = keywords.split(" ");
        for (let book of BOOK_DATA) {
            if (book.containsKeywords(myKeywords)) {
                foundBooks.push(book);
            }
        }

        this.lastBooks=foundBooks;

        return foundBooks;
    }

    public searchBooksAsync(keywords: string): Promise<Book[]> {

        console.log("CatalogService: searchBooksAsync");

        this.lastKeywords=keywords;

        let url = this.baseUrl + "?keywords="+keywords;

        console.log("searchBooksAsync" + url);

        let headers = new Headers({"Accept": "application/json"});

        return this.http.get(url).toPromise()
            .then(response => {
                console.log("got any books");
                return response.json() as Book[];
            })
            .catch(error => {
                console.error("CatalogService: " + error);
                return Promise.reject(error);
            });

        /*return new Promise((resolve, reject) => {

            let foundBooks: Book[] = [];
            let myKeywords: Array<string> = keywords.split(" ");
            for (let book of BOOK_DATA) {
                if (book.containsKeywords(myKeywords)) {
                    foundBooks.push(book);
                }
            }


            setTimeout(() => resolve(foundBooks), 2000);

        });*/
    }
}