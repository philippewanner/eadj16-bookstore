import { platformBrowserDynamic } from "@angular/platform-browser-dynamic";
import { enableProdMode } from "@angular/core";
import { environment } from "./environments/environment";
import { AppModule } from "./app/app.module";

export namespace Config {
    export function RestUrl(): string {
        if (environment.production) {
            return "/bookstore/rest";
        }
        return "http://localhost:8080/bookstore/rest";
    }
}

if (environment.production) {
    enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule);
