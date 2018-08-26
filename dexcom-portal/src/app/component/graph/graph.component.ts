import {Component, OnInit, Input, OnChanges, SimpleChanges, Output, EventEmitter} from '@angular/core';
import {Stream} from '../../model/stream';
import {Log} from '../../model/log';
import {NodeLogs} from '../../model/nodeLogs';
import {ChartFactory} from '../../model/chart-type';
import {Table} from '../../table';
import {showLoading, hideLoading, doNothing} from "../../commons"
import * as Rx from "rxjs/Rx";
import {Response} from '@angular/http';
import {Router} from '@angular/router';

import 'rxjs/add/operator/switchMap';


import {DeviceService} from '../../service/device.service';

declare var CanvasJS: any;

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css']
})
export class GraphComponent implements OnInit,OnChanges {

    constructor(private deviceService: DeviceService, private router: Router) { }
    myVar: boolean;
    cgmReading:       {x:number , y:number}[]=[];
    greanArea:        {x:number , y:any[]}[]=[];
    yellowArea:       {x:number , y:any[]}[]=[];
    BGReading:        {x:number , y:number}[]=[];
    
    nodeLogs: NodeLogs=null;
    streams: Stream[]=null;
    logs: Log[]=null;
    hide:boolean = false;
    message:string = "loading...";
    chartFactory:ChartFactory=new ChartFactory;
    intervalMsg:string="";

    @Input() selectedDeviceId: number;
    currentDeviceId: number;
  
    ngOnChanges(changes:SimpleChanges) {
        this.reset();
        this.currentDeviceId = this.selectedDeviceId;
        
        let streamObservable: Rx.Observable<NodeLogs> = this.deviceService.getNodeLogs(this.selectedDeviceId);
        showLoading();
        streamObservable.subscribe(logs => {
            console.log('this.selectedDeviceId'+this.selectedDeviceId);
            console.log('this.nodeLogs.deviceId'+logs.deviceId);
                                        if(this.selectedDeviceId == logs.deviceId){
                                            this.nodeLogs = logs;
                                            this.streams = logs.deviceEGVLogs;
                                            this.logs = logs.deviceBGLogs;
                                            this.fillChart();     
                                        }
                                    }
                             );
        
    }
    ngOnInit(): any {   
    }
    
    getTimeZone(timeStamp){
        let date = new Date(timeStamp*1000);
        return date;    
    }

    fillChart(){
        if(this.streams){
            this.reset();
            this.hide=false; // to enable showing the chart in html

            //in case no data for this cgm just hide the charts
            if(this.streams.length == 0){
                this.hide=true;
                this.message="There is no data for this CGM";
            }

            this.streams.forEach(log => {
                this.yellowArea.push({x: log.logTime , y: [70, 180]});
                this.greanArea.push({x: log.logTime , y: [80, 140]});
                this.cgmReading.push({x: log.logTime,y:log.estimatedGlucoseValue});
        });
        this.logs.forEach(log => {
            if(log.bloodGlucoseTime > 0)
                this.BGReading.push({x: log.bloodGlucoseTime,y:log.bloodGlucose});

    });
            //--------------- Chart 1 ---------------//
            var chart1 = new CanvasJS.Chart("cgmReadingChart", {
            zoomEnabled:true,
            animationEnabled: false,
			// rangeChanged: syncHandler,
            title: {
                text: "CGM Reading Chart"
            },
            toolTip: {
                contentFormatter: function (e) {
                var content = "";
                for (var i = 0; i < e.entries.length; i++){
                    var totalSeconds = e.entries[i].dataPoint.x;

                    const days = Math.floor(totalSeconds/(3600*24));
                    const carryDay = totalSeconds%(3600*24);

                    const hours = Math.floor(carryDay/3600);
                    const carryHour = carryDay%3600;
            
                    const minutes =  Math.floor(carryHour/60);
                    const sec = Math.floor(carryHour%60);                 
                    
                    content = days +' Days - '+ hours+':'+minutes+':'+sec +' - EGV:'+e.entries[i].dataPoint.y; 
            
                    return content;  
                }       
               
                return content;
              }
            },
            legend: 
            {
                dockInsidePlotArea: true,
                verticalAlign: "top",
                horizontalAlign: "center",
                fontSize: 15,              
            },
            axisX: {
			    labelAngle: -50,
                gridThickness: 1 ,
                labelFormatter: function (e) {

                    const days = Math.floor(e.value/(3600*24));
                    const carryDay = e.value%(3600*24);

                    const hours = Math.floor(carryDay/3600);
                    const carryHour = carryDay%3600;
            
                    const minutes =  Math.floor(carryHour/60);
                    const sec = Math.floor(carryHour%60);
            
                    return days +' Days - '+ hours+':'+minutes+':'+sec;
                },
                  includeZero: false
                // interval: 5,
                // intervalType: "minute"
		    },
            axisY: {
             title:"BG / CGM [mg / dl]",
             titleMaxWidth:300,
                minimum: 0,
                maximum: 400,
			    labelAngle: -60,
                includeZero: true,
                gridThickness: 1,
                interval:50
		    },
            axisY2: {
                title:"-",
                titleFontColor: "white",
                minimum: 0,
                maximum: 400,
                lineThickness:0,
                tickThickness:0,
                includeZero: true,
                valueFormatString:" ",//space
                interval:50
		    },
            data: [ 
                this.chartFactory.getYellowAreaChart(this.yellowArea),
                this.chartFactory.getGreenAreaChart(this.greanArea),
                this.chartFactory.getBGDotChart(this.BGReading),
                this.chartFactory.getCGMChart(this.cgmReading)
            ],
        });
        // render the chart just in case it has a data 
        if(this.streams.length > 0){
            chart1.render();
        }
      }
      else{
          this.hide=true;
          this.message="There is no data for this CGM";
        }
    }
    reset(){
        this.nodeLogs = null;
        this.myVar = false;
        this.cgmReading=[];
        this.greanArea=[];
        this.yellowArea=[];
        this.BGReading=[];
        this.hide=true;
        this.message="loading...";
    }
}
