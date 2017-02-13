import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {Routes, RouterModule} from "@angular/router";
import {LoginComponent} from "./component/user/login.component";
import {AppComponent} from "./component/app.component";
import {UserService} from "./service/user.service";
import {RegistrationComponent} from "./component/user/registration.component";
import {UserEditComponent} from "./component/user/user-edit.component";
import {CatalogComponent} from "./component/catalog/catalog.component";
import {BookListComponent} from "./component/booklist/booklist.component";
import {CatalogService} from "./service/catalog.service";
import {BookDetailsComponent} from "./component/book-details/book-details.component";
import {ShoppingCartComponent} from "./component/shopping-cart/shopping-cart.component";
import {ShoppingCartService} from "./service/shopping-cart.service";
import {OrderComponent} from "./component/order/order.component";
import {HttpModule} from "@angular/http";

const routes: Routes = [
    {path: "login", component: LoginComponent},
    {path: "registration", component: RegistrationComponent},
    {path: "book-details/:isbn", component: BookDetailsComponent},
    {path: "shopping-cart", component: ShoppingCartComponent},
    {path: "order", component: OrderComponent},
    {path: "**", component: CatalogComponent}
];

@NgModule({
    imports: [BrowserModule, FormsModule, RouterModule.forRoot(routes), HttpModule],
    declarations: [AppComponent,
        CatalogComponent,
        LoginComponent,
        RegistrationComponent,
        UserEditComponent,
        BookListComponent,
        BookDetailsComponent,
        ShoppingCartComponent,
        OrderComponent],
    providers: [UserService,
        CatalogService,
        ShoppingCartService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
