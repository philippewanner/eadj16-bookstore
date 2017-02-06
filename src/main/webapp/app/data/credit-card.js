"use strict";
(function (CreditCardType) {
    CreditCardType[CreditCardType["AMERICAN_EXPRESS"] = 0] = "AMERICAN_EXPRESS";
    CreditCardType[CreditCardType["MASTER_CARD"] = 1] = "MASTER_CARD";
    CreditCardType[CreditCardType["VISA"] = 2] = "VISA";
})(exports.CreditCardType || (exports.CreditCardType = {}));
var CreditCardType = exports.CreditCardType;
var CreditCard = (function () {
    function CreditCard() {
    }
    return CreditCard;
}());
exports.CreditCard = CreditCard;
//# sourceMappingURL=credit-card.js.map