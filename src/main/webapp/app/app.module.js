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
var router_1 = require("@angular/router");
var login_component_1 = require("./component/user/login.component");
var app_component_1 = require("./component/app.component");
var user_service_1 = require("./service/user.service");
var registration_component_1 = require("./component/user/registration.component");
var user_edit_component_1 = require("./component/user/user-edit.component");
var catalog_component_1 = require("./component/catalog/catalog.component");
var booklist_component_1 = require("./component/booklist/booklist.component");
var catalog_service_1 = require("./service/catalog.service");
var book_details_component_1 = require("./component/book-details/book-details.component");
var shopping_cart_component_1 = require("./component/shopping-cart/shopping-cart.component");
var shopping_cart_service_1 = require("./service/shopping-cart.service");
var order_component_1 = require("./component/order/order.component");
var http_1 = require("@angular/http");
var routes = [
    { path: "login", component: login_component_1.LoginComponent },
    { path: "registration", component: registration_component_1.RegistrationComponent },
    { path: "book-details/:isbn", component: book_details_component_1.BookDetailsComponent },
    { path: "shopping-cart", component: shopping_cart_component_1.ShoppingCartComponent },
    { path: "order", component: order_component_1.OrderComponent },
    { path: "**", component: catalog_component_1.CatalogComponent }
];
var AppModule = (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        core_1.NgModule({
            imports: [platform_browser_1.BrowserModule, forms_1.FormsModule, http_1.HttpModule, router_1.RouterModule.forRoot(routes)],
            declarations: [app_component_1.AppComponent,
                catalog_component_1.CatalogComponent,
                login_component_1.LoginComponent,
                registration_component_1.RegistrationComponent,
                user_edit_component_1.UserEditComponent,
                booklist_component_1.BookListComponent,
                book_details_component_1.BookDetailsComponent,
                shopping_cart_component_1.ShoppingCartComponent,
                order_component_1.OrderComponent],
            providers: [user_service_1.UserService,
                catalog_service_1.CatalogService,
                shopping_cart_service_1.ShoppingCartService],
            bootstrap: [app_component_1.AppComponent]
        }), 
        __metadata('design:paramtypes', [])
    ], AppModule);
    return AppModule;
}());
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map