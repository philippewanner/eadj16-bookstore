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
var http_1 = require("@angular/http");
require("rxjs/add/operator/toPromise");
var main_1 = require("../main");
var REGISTRATIONS_URL = "/registrations/";
var CUSTOMER_URL = "/customers/";
var UserService = (function () {
    function UserService(http) {
        this.http = http;
    }
    UserService.prototype.authenticate = function (email, password) {
        var url = main_1.BOOKSTORE_REST_URL + REGISTRATIONS_URL + email;
        var headers = new http_1.Headers({ "password": password });
        return this.http.get(url, headers).toPromise()
            .then(function (response) { return response.ok; })
            .catch(function (error) {
            console.log("UserService: " + error);
            return false;
        });
    };
    UserService.prototype.register = function (registration) {
        var url = main_1.BOOKSTORE_REST_URL + REGISTRATIONS_URL;
        var headers = new http_1.Headers({ "Content-Type": "application/json" });
        return this.http.post(url, registration, headers).toPromise()
            .then(function (response) {
            if (response.status === 201) {
                return response.json();
            }
        })
            .catch(function (error) {
            console.log("UserService: " + error);
            return Promise.reject(error);
        });
    };
    UserService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], UserService);
    return UserService;
}());
exports.UserService = UserService;
//# sourceMappingURL=user.service.js.map