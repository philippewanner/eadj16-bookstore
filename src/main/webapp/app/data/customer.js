"use strict";
var address_1 = require("./address");
var credit_card_1 = require("./credit-card");
var Customer = (function () {
    function Customer() {
        this.address = new address_1.Address();
        this.creditCard = new credit_card_1.CreditCard();
    }
    return Customer;
}());
exports.Customer = Customer;
//# sourceMappingURL=customer.js.map