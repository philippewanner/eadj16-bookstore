import {Injectable} from "@angular/core";
import {Customer} from "../core/customer";
import {Http, Headers} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Registration} from "../core/registration";
import {Config} from "../../main";

const REGISTRATIONS_URL: string = "/registrations/";
const CUSTOMER_URL: string = "/customers/";

@Injectable()
export class UserService {

    public customer: Customer;

    constructor(private http: Http) {
    }

    public authenticate(email: string, password: string): Promise<number> {
        let url: string = Config.RestUrl() + REGISTRATIONS_URL + email;
        let headers: Headers = new Headers({"password": password});
        return this.http.get(url, {headers}).toPromise()
            .then(response => {
                return response.json() as number;
            })
            .catch(error => {
                console.log("UserService: " + error);
                return null;
            });
    }

    public register(registration: Registration): Promise<number> {
        let url: string = Config.RestUrl() + REGISTRATIONS_URL;
        let headers: Headers = new Headers({"Content-Type": "application/json"});
        return this.http.post(url, registration, {headers}).toPromise()
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

    public getCustomer(number: number): Promise<Customer> {
        let url: string = Config.RestUrl() + CUSTOMER_URL + number;
        let headers: Headers = new Headers({"Accept": "application/json"});
        return this.http.get(url, {headers}).toPromise()
            .then(response => {
                if (response.ok) {
                    return response.json() as Customer;
                }
                return null;
            })
            .catch(error => {
                console.log("UserService: " + error);
                return Promise.reject(error);
            });
    }

    public get isloggedIn(): boolean {
        return this.customer != null;
    }
}
