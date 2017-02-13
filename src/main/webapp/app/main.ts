import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app.module';

export const BOOKSTORE_REST_URL: string = "http://localhost:8080/bookstore/rest";

platformBrowserDynamic().bootstrapModule(AppModule);