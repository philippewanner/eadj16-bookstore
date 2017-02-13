export class BookInfo {

    constructor(public isbn: string,
                public title: string = "",
                public price: number=0) {
    }

    public containsKeywords(keywords: string[]): boolean{
        let bookString: string =this.isbn + " " + this.title;

        let contains: boolean=true;

        for (let word of keywords) {
            contains = contains && (bookString.includes(word));
        }

        return contains;
    }

}