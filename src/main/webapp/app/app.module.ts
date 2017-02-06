import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {Routes, RouterModule} from "@angular/router";
import {LoginComponent} from "./component/login.component";
import {AppComponent} from "./component/app.component";

const routes: Routes = [
    {path: "**", component: LoginComponent}
];

@NgModule({
    imports: [BrowserModule, FormsModule, RouterModule.forRoot(routes)],
    declarations: [AppComponent, LoginComponent],
    bootstrap: [AppComponent]
})
export class AppModule {
}