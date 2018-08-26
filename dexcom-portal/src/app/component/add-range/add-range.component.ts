import { Component, OnInit } from '@angular/core';
import {Observable}  from 'rxjs/Observable';
import {LoginService} from '../../service/login.service';
import {DeviceService} from '../../service/device.service';
import {DeviceRange} from '../../model/range';
import { Router } from "@angular/router";
import * as Rx from 'rxjs/Rx';
import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'app-add-range',
  templateUrl: './add-range.component.html',
  styleUrls: ['./add-range.component.css']
})
export class AddRangeComponent implements OnInit {
  // private range = {'starting':'', 'ending':''};
  range:DeviceRange = new DeviceRange();
  message:string="";
  constructor(private loginService: LoginService,private deviceService: DeviceService, private router: Router) { }
  ngOnInit() {
  }
  onSubmit() {
    this.message="";
    if(this.validRange()){
      let observable: Rx.Observable<number> = this.deviceService.addRange(this.range);
      observable.//subscribe(res =>this.message = (isNaN(res) ?0:res)+" CGMs are added successfully ");
      subscribe(
        res =>this.message = (isNaN(res) ?0:res)+' CGMs are added successfully',
        () => this.message = 'error!',
        () => console.log('completed!')
      );                       
    }
  }
  onRemove(){
    this.message="";
    if(this.validRange()){
      let observable: Rx.Observable<number> = this.deviceService.removeRange(this.range);
      observable.subscribe(res =>this.message = (isNaN(res) ?0:res)+" CGMs are removed successfully ");
    }
  }


  validRange():boolean{
    if(this.range.startRange)
    {
      if(this.validTXID(this.range.startRange) == false)
      return false;
    }
    if(this.range.listOfDevices){
      var deviceList = this.range.listOfDevices.split(',');
      for (var i = 0; i < deviceList.length; i++) {
        console.log(deviceList[i]);
        if(this.validTXID(deviceList[i]) == false)
          return false;
      }
    }
    return true;

  }
  validTXID(txId:string):boolean{
    if(!txId){
      this.message =  "CGM TX ID is empty";
      return false;
    }
    if(txId.length != 6){
      this.message =  "CGM TX ID length should be 6 ";
      return false;
    }
    else{
      var length = txId.length;
      for (var i = 0; i < length; i++) {
        var ind = "0123456789ABCDEFGHJKLMNPQRSTUWXYZ".indexOf(txId.charAt(i));
        if(ind == -1){
          this.message =  txId.charAt(i)+ " is invalid character";
          return false;
        }
      } 
    }
    return true;
  }

}
