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

const routes: Routes = [
    {path: "login", component: LoginComponent},
    {path: "registration", component: RegistrationComponent},
    {path: "**", component: CatalogComponent}
];

@NgModule({
    imports: [BrowserModule, FormsModule, RouterModule.forRoot(routes)],
    declarations: [AppComponent, CatalogComponent, LoginComponent, RegistrationComponent, UserEditComponent, BookListComponent],
    providers: [UserService],
    bootstrap: [AppComponent]
})
export class AppModule {
}