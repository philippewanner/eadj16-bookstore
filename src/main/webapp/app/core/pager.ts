import {Book} from "./book";
export class Pager {

    public hasNext: boolean = true;
    public hasPrevious: boolean = false;

    public currentNextPosition: number = 0;
    public currentPreviousPosition: number = 0;

    constructor(private items: Array<Book>) {

    }

    public getNext(itemCount: number = 10): Array<Book> {

        let page: Array<Book> = [];

        let idx: number;

        this.currentPreviousPosition = this.currentNextPosition;

        for (idx = this.currentNextPosition; (idx < (this.currentNextPosition + itemCount) && idx < this.items.length); idx++) {
            page.push(this.items[idx]);
        }

        this.currentNextPosition = idx;

        this.hasNext = this.currentNextPosition < this.items.length;
        this.hasPrevious = this.currentPreviousPosition > 0;

        return page;
    }

    public getPrevious(itemCount: number = 10): Array<Book> {

        let page: Array<Book> = [];

        this.currentPreviousPosition -= itemCount;

        let idx: number;

        for (idx = this.currentPreviousPosition; (idx < (this.currentPreviousPosition + itemCount) && idx < this.items.length); idx++) {
            page.push(this.items[idx]);
        }

        this.currentNextPosition = idx;

        this.hasNext = true;
        this.hasPrevious = this.currentPreviousPosition > 0;

        return page;
    }
}