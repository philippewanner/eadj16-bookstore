export class Book {

    constructor(public isbn: string,
                public title: string = "",
                public authors: string = "",
                public publisher: string = "",
                public publicationYear: number = 1888,
                public binding: BookBinding = BookBinding.UNKNOWN,
                public numberOfPages: number = 0,
                public price: number=0) {
    }

    public containsKeywords(keywords: string[]): boolean{
        let bookString: string =this.isbn + " " + this.title + " " + this.authors + " " + this.publisher;

        let contains: boolean=true;

        for (let word of keywords) {
            contains = contains && (bookString.includes(word));
        }

        return contains;
    }

}

export enum BookBinding {
    HARDCOVER, PAPERBACK, EBOOK, UNKNOWN
}