import {Component, OnInit, Input, OnChanges, SimpleChanges, Output, EventEmitter} from '@angular/core';
import { Device } from '../../model/device';
import {Table} from '../../table';
import {showLoading, hideLoading, doNothing} from "../../commons"
import * as Rx from "rxjs/Rx";
import {Response} from '@angular/http';
import {Router} from '@angular/router';
import 'rxjs/add/operator/switchMap';
@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit,OnChanges {
  title: string;
  @Input() lat: number ;
  @Input() lng: number ;
  hide:boolean = true;
  selectedEvent = {};
  index;
  iconURL:string = "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
  message:string = "loading...";
  showPolyLine:number = 1;
  buttonMarkers:string="Hide Markers";
  buttonPolyLine:string="Hide PolyLine";
  devices:Device[];
  //selected device id to retrieve it's data
  @Input() selectedDeviceId: number;
  @Input() deviceType: string;
  
  constructor(private router: Router) {
  }

  ngOnChanges(changes:SimpleChanges) {
    }
    ngOnInit(): any {    
    }

    
}
