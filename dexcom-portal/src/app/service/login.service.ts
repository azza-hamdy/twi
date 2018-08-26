import {Injectable} from '@angular/core';
import {PaginationPage, PaginationPropertySort} from '../model/pagination';
import {webServiceEndpoint} from '../commons';
import {Http, Headers, Response, URLSearchParams, RequestOptions} from '@angular/http';
import {Resolve, RouterStateSnapshot, ActivatedRouteSnapshot} from '@angular/router';
import * as Rx from "rxjs/Rx";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/publish';
@Injectable()
export class LoginService {

  constructor (private http: Http) {}

  sendCredential(model) {
    let tokenUrl1 = `${webServiceEndpoint}/user/login`;
    let headers1 = new Headers({'Content-Type': 'application/json'});
    return this.http.post(tokenUrl1, JSON.stringify(model), {headers: headers1});
  }

  logout() {
    localStorage.setItem("dexcom_token","");
    localStorage.setItem("dexcom_currentUserName", "");
    alert("You just logged out.");
  }

  checkLogin() {
    if (localStorage.getItem("dexcom_currentUserName")!=null && localStorage.getItem("dexcom_currentUserName")!='' && localStorage.getItem("dexcom_token")!=null && localStorage.getItem("dexcom_token")!='') {
      return true;
    } else {
      return false;
    }
  }

}
