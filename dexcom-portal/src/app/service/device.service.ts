import {Injectable} from '@angular/core';
import {Device} from '../model/device'
import {Stream} from '../model/stream'
import {Log} from '../model/log'
import {PaginationPage, PaginationPropertySort} from '../model/pagination';
import {webServiceEndpoint} from '../commons';
import {Http, Headers, Response, URLSearchParams, RequestOptions} from '@angular/http';
import {Resolve, RouterStateSnapshot, ActivatedRouteSnapshot} from '@angular/router';
import * as Rx from "rxjs/Rx";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/publish';
import { NodeLogs } from '../model/nodeLogs';

@Injectable()
export class DeviceService {

  constructor(private http:Http) { }

  findDevices(page: number, pageSize: number, searchText:string ,sort: PaginationPropertySort): Rx.Observable<PaginationPage<Device>> {
        let params = new URLSearchParams();
        params.set('size', `${pageSize}`);
        params.set('page', `${page}`);
        params.set('searchText',searchText);
        if (sort != null) {
            params.set('sort', `${sort.property},${sort.direction}`);
        }
        let options = new RequestOptions({
            search: params,
            headers:this.getHeaders()
        });
        return this.http.get(`${webServiceEndpoint}/api/device`, options).map(this.extractData);
    }

    getStreams(id: number): Rx.Observable<Stream[]> {
        return this.http.get(`${webServiceEndpoint}/api/device/${id}`,{headers:this.getHeaders()}).map(this.extractData);
    }

    getNodeLogs(id: number): Rx.Observable<NodeLogs> {
        return this.http.get(`${webServiceEndpoint}/api/device/${id}`,{headers:this.getHeaders()}).map(this.extractData);
    }
    // getLogs(id: number): Rx.Observable<Log[]> {
    //     return this.http.get(`${webServiceEndpoint}/api/device/logs/${id}`,{headers:this.getHeaders()}).map(this.extractData);
    // }

    addRange(range): Rx.Observable<number> {
        return this.http.post(`${webServiceEndpoint}/api/device/add_range`,JSON.stringify(range),{headers:this.getHeaders()}).map(this.extractData);
    }

    removeRange(range): Rx.Observable<number> {
        return this.http.post(`${webServiceEndpoint}/api/device/remove_range`,JSON.stringify(range),{headers:this.getHeaders()}).map(this.extractData);
    }
    private extractData(res: Response) {
        let body = res.json();
        return body || {};
    }
    getHeaders(){
        let token = localStorage.getItem("dexcom_token");
        return new Headers({'Content-Type':'application/json',
                          'Authorization':'Bearer '+token});                
    }

}
