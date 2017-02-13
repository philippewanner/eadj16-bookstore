"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var customer_1 = require("../../core/customer");
var credit_card_1 = require("../../core/credit-card");
var credit_card_type_1 = require("../../core/credit-card-type");
var UserEditComponent = (function () {
    function UserEditComponent() {
        this.creditCard = new credit_card_1.CreditCard();
        this.creditCardTypes = [];
        this.minYear = new Date().getFullYear();
        this.maxYear = new Date().getFullYear() + 10;
        this.creditCardPattern = "^5[1-5][0-9]{14}$";
        console.log("log1");
        this.creditCardTypes.push(new credit_card_type_1.CreditCardType("MASTER_CARD", "MasterCard"));
        this.creditCardTypes.push(new credit_card_type_1.CreditCardType("VISA", "Visa"));
        console.log("log2");
    }
    __decorate([
        core_1.Input(), 
        __metadata('design:type', customer_1.Customer)
    ], UserEditComponent.prototype, "customer", void 0);
    UserEditComponent = __decorate([
        core_1.Component({
            selector: "user-edit",
            templateUrl: "app/component/user/user-edit.component.html"
        }), 
        __metadata('design:paramtypes', [])
    ], UserEditComponent);
    return UserEditComponent;
}());
exports.UserEditComponent = UserEditComponent;
//# sourceMappingURL=user-edit.component.js.map