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
var platform_browser_1 = require("@angular/platform-browser");
var forms_1 = require("@angular/forms");
var catalog_component_1 = require("./component/catalog.component");
var book_details_component_1 = require("./component/book-details.component");
var book_list_component_1 = require("./component/book-list.component");
var CatalogService_1 = require("./service/CatalogService");
var home_component_1 = require("./component/home.component");
var router_1 = require("@angular/router");
var app_omponent_1 = require("./component/app.omponent");
var routes = [
    { path: "catalog", component: catalog_component_1.CatalogComponent },
    { path: "**", component: home_component_1.HomeComponent }
];
var AppModule = (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        core_1.NgModule({
            imports: [platform_browser_1.BrowserModule, forms_1.FormsModule, router_1.RouterModule.forRoot(routes)],
            declarations: [app_omponent_1.AppComponent, book_details_component_1.BookDetailsComponent, book_list_component_1.BookListComponent, catalog_component_1.CatalogComponent, home_component_1.HomeComponent],
            providers: [CatalogService_1.CatalogService],
            bootstrap: [app_omponent_1.AppComponent]
        }), 
        __metadata('design:paramtypes', [])
    ], AppModule);
    return AppModule;
}());
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map