import {Injectable} from "@angular/core";
import {Book} from "../core/book";
import {BOOK_DATA} from "../core/book-data";

@Injectable()
export class CatalogService {

    public lastKeywords:string;
    public lastBooks:Book[];

    constructor() {
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

        return new Promise((resolve, reject) => {

            let foundBooks: Book[] = [];
            let myKeywords: Array<string> = keywords.split(" ");
            for (let book of BOOK_DATA) {
                if (book.containsKeywords(myKeywords)) {
                    foundBooks.push(book);
                }
            }


            setTimeout(() => resolve(foundBooks), 2000);

        });
    }
}