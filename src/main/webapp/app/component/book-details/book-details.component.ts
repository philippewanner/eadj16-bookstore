import {Component, Input} from '@angular/core';
import {Book} from "../../core/book"
import {ActivatedRoute, Router} from "@angular/router";
import {CatalogService} from "../../service/catalog.service";
import {Location} from "@angular/common";

@Component({
    selector: 'book-details',
    templateUrl: 'app/component/book-details/book-details.component.html'
})
export class BookDetailsComponent {

    ////@In//put()
    private book: Book = null;

    private msg: string = null;

    private elementName: string = "Book details";

    constructor(private route: ActivatedRoute, private catalogService: CatalogService,private router: Router) {
        console.log("constructor BookDetails");

        let isbn = route.snapshot.params['isbn'];

        catalogService.findBookAsync(isbn).then((result: Book) => {

            console.log("BookDetails: set this.book");
            this.book = result;

        })
            .catch((error: any) => {
                this.msg = "Book details not loaded";
                console.log("book error");
            });
    }

    public goBack(): void {
        this.router.navigate((['/catalog']));
    }
}