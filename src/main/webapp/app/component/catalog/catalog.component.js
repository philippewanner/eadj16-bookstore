"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var catalog_service_1 = require("../../service/catalog.service");
var CatalogComponent = (function () {
    function CatalogComponent(catalogService) {
        this.catalogService = catalogService;
        this.elementName = "Catalog";
        this.searching = "";
        this.keywords = "";
        this.foundBooks = [];
        this.numberFoundBooks = "";
        this.selectedBook = null;
        console.log("constructor CatalogComponent");
        this.keywords = catalogService.lastKeywords;
        if (catalogService.lastBooks) {
            this.foundBooks = catalogService.lastBooks;
        }
    }
    CatalogComponent.prototype.selectBook = function (book) {
        console.log("public selectBook(book: Book) has been called");
        this.selectedBook = book;
    };
    CatalogComponent.prototype.deselectBook = function () {
        this.selectedBook = null;
    };
    CatalogComponent.prototype.search = function () {
        var _this = this;
        this.foundBooks = [];
        this.searching = "searching...";
        if (this.keywords.length === 0) {
            this.numberFoundBooks = "please enter any keywords";
            return;
        }
        //this.foundBooks = this.catalogService.searchBooks(this.keywords);
        this.catalogService.searchBooksAsync(this.keywords).then(function (result) {
            _this.searching = "";
            _this.foundBooks = result;
            if (_this.foundBooks.length > 0) {
                _this.catalogService.lastBooks = _this.foundBooks;
                _this.numberFoundBooks = _this.foundBooks.length + " books found";
            }
            else {
                _this.numberFoundBooks = "no books found";
            }
        });
    };
    CatalogComponent = __decorate([
        core_1.Component({
            selector: 'catalog',
            templateUrl: "app/component/catalog/catalog.component.html"
        }), 
        __metadata('design:paramtypes', [catalog_service_1.CatalogService])
    ], CatalogComponent);
    return CatalogComponent;
}());
exports.CatalogComponent = CatalogComponent;
//# sourceMappingURL=catalog.component.js.map