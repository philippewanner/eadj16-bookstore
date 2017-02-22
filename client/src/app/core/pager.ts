import {BookInfo} from "./book-info";
export class Pager {

    private readonly pageItemCount: number = 10;

    // zero based index
    public currentPage: number = -1;

    // 1...x
    public pagesCount: number = 0;

    private pages: Array<Array<BookInfo>>;

    constructor(private items: Array<BookInfo>) {

        let pageIdx: number = 0;
        this.pagesCount = this.getPagesCount();

        this.pages = new Array<Array<BookInfo>>();

        for (let pageIdx: number = 0; pageIdx < this.pagesCount; pageIdx++) {

            this.pages[pageIdx] = new Array<BookInfo>();

            let offset: number = pageIdx * this.pageItemCount;

            for (let idx: number = offset; (idx < offset + this.pageItemCount) && (idx < this.items.length); idx++) {
                this.pages[pageIdx].push(this.items[idx]);
            }
        }
    }

    public getNext(): Array<BookInfo> {

        this.currentPage++;
        if (this.currentPage >= this.pagesCount) {
            this.currentPage--;
        }

        return this.pages[this.currentPage];
    }

    public getPrevious(): Array<BookInfo> {

        this.currentPage--;
        if (this.currentPage < 0) {
            this.currentPage = 0;
        }

        return this.pages[this.currentPage];
    }

    private getPagesCount() {
        let remainder: number = this.items.length % this.pageItemCount;

        if (remainder === 0) {
            return this.items.length / this.pageItemCount;
        } else {
            return (this.items.length - remainder) / this.pageItemCount + 1;
        }
    }
}