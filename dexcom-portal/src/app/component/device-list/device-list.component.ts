import {Component, OnInit} from '@angular/core';
import {Response} from '@angular/http';
import {Router} from '@angular/router';
import * as Rx from 'rxjs/Rx';

import 'rxjs/add/operator/switchMap';

import {PaginationPage, PaginationPropertySort} from '../../model/pagination';
import {Stream} from '../../model/stream';
import {Table} from '../../table';
import {showLoading, hideLoading, doNothing} from '../../commons'
import {DeviceService} from '../../service/device.service';
import {Device} from '../../model/device';
import { isNumeric } from '@angular/common/src/pipes/number_pipe';

@Component({
  selector: 'app-device-list',
  templateUrl: './device-list.component.html',
  styleUrls: ['./device-list.component.css']
})
export class DeviceListComponent  implements OnInit, Table<Device>  {
    showGWInfo:boolean = false;
    hide:boolean = true;
    devicePage: PaginationPage<Device>;
    self: Table<Device>;
    selectedDevice: Device;
    selectedDeviceId: number;
    logs: Stream[];
    isRowChanged:boolean=false;
    selectedRow : number;
    currentPage: number;
    filteredTXID = '';
    
    constructor(private deviceService: DeviceService, private router: Router) {
    }

    allDevicePages: PaginationPage<Device>;
    interval:any;

    ngOnInit() {

        
        this.loadContent(0,10,null); 
        this.refreshContent();
       }
    

    refreshContent ()
    {
        
        let observable: Rx.Observable<PaginationPage<Device>>;
        this.interval = setInterval(() => 
        {
            observable= this.deviceService.findDevices(this.currentPage, 10,this.filteredTXID, null);
            observable.subscribe(devicePage => this.devicePage = devicePage);
        }, 5000);
    }

    loadContent(pageNumber: number, pageSize: number, sort: PaginationPropertySort): Rx.Observable<PaginationPage<Device>> 
    {
        let observable: Rx.Observable<PaginationPage<Device>>;
        observable= this.deviceService.findDevices(pageNumber, pageSize,this.filteredTXID, sort);
        this.currentPage = pageNumber;
        observable.subscribe(devicePage => this.devicePage = devicePage);
        this.self = this;
        return observable;
    }
    ngOnDestroy() {
        if (this.interval) {
            clearInterval(this.interval);
        }
    }
    fetchPage(pageNumber: number, pageSize: number, sort: PaginationPropertySort): Rx.Observable<PaginationPage<Device>> {
        return this.loadContent(pageNumber,pageSize,sort); ;
    }

    onSearchKeyPress(){
        this.loadContent(0,10,null);
    }

//    onSelect(device:Device){
//     this.selectedDevice = device;
    
//     let observable: Rx.Observable<Stream[]> = this.deviceService.getLogs(device.id);
//     showLoading();
//     observable.subscribe(logs => {this.logs = logs;
//         console.log("on select "+this.logs[1] );
//     });
//   }
  
  getDateWithTimeZone(strDate:string){
    if (strDate)
    {
        //remove milliseconds from date format to be suitable for all browsers data format
        if(strDate.indexOf('.') > 0 )
            strDate = strDate.substr(0,strDate.indexOf('.')).replace(/-/g , "/");
        else
            strDate = strDate.replace(/-/g , "/");
        let date = new Date(strDate);
        let timeZoneOffset = new Date().getTimezoneOffset();
        date.setMinutes(date.getMinutes()-timeZoneOffset);
        return date;    
    }
    return null;
    }

    getProgressValue(device:any){
        if(device.state == '0')
            return 100;
        if(Number(device.progress/device.totalEgvs))
            return Math.floor((device.progress/device.totalEgvs)*100);
        return 0;
    }
}
