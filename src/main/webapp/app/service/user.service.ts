import {Injectable} from "@angular/core";
import {Customer} from "../data/customer";

@Injectable()
export class UserService {

    public customer: Customer;
}