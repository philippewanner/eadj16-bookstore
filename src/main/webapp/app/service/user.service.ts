import {Injectable} from "@angular/core";
import {Customer} from "../core/customer";
import {Http, Headers} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {BOOKSTORE_REST_URL} from "../main";
import {Registration} from "../core/registration";

const REGISTRATIONS_URL: string = "/registrations/";
const CUSTOMER_URL: string = "/customers/";

@Injectable()
export class UserService {

    public customer: Customer;

    constructor(private http: Http) {
    }

    public authenticate(email: string, password: string): Promise<boolean> {
        let url: string = BOOKSTORE_REST_URL + REGISTRATIONS_URL + email;
        let headers: Headers = new Headers({"password": password});
        return this.http.get(url, headers).toPromise()
            .then(response => response.ok)
            .catch(error => {
                console.log("UserService: " + error);
                return false;
            });
    }

    public register(registration: Registration): Promise<number> {
        let url: string = BOOKSTORE_REST_URL + REGISTRATIONS_URL;
        let headers: Headers = new Headers({"Content-Type": "application/json"});
        return this.http.post(url, registration, headers).toPromise()
            .then(response => {
                if (response.status === 201) {
                    return response.json() as number;
                }
            })
            .catch(error => {
                console.log("UserService: " + error);
                return Promise.reject(error);
            });
    }
}