import {ShoppingCartItem} from "../component/shopping-cart/shopping-cart-item";
export class OrderRequest {

    constructor(public customerNr: number,
                public items: Array<ShoppingCartItem>) {
    }

}

/*<orderRequest xsi:noNamespaceSchemaLocation="orders.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <customerNr>1</customerNr>
    <items>
        <bookInfo>
            <isbn>0672337452</isbn>
    <title>Title</title>
    <price>27.00</price>
    </bookInfo>
    <quantity>1</quantity>
    </items>
    <items>
        <bookInfo>
            <isbn>0672337452</isbn>
    <title>Title</title>
    <price>27.00</price>
    </bookInfo>
    <quantity>1</quantity>
    </items>
    </orderRequest>*/