import {ShoppingCartItem} from "../component/shopping-cart/shopping-cart-item";
export class OrderRequest {

    constructor(public customerNr: number,
                public items: Array<ShoppingCartItem>) {
    }

}