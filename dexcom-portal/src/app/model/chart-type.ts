export class  ChartFactory{
    //Top Charts (For CGM Readin)
    getYellowAreaChart(data:any){
        return  {
            type: "rangeSplineArea",
            markerType: "none", 
            axisYType: "secondary",
            color: "#F5EB96",
            showInLegend: true, 
            legendText: "[70-180]mg/dl",
            // xValueType: "dateTime",
            // xValueFormatString:"DD MMM HH:mm:ss",
            dataPoints:  data
        };
    }
    getGreenAreaChart(data:any){
        return  {
            type: "rangeSplineArea",
            markerType: "none", 
            axisYType: "secondary",
            color: "#78AA5A",
            showInLegend: true, 
            legendText: "[80-140]mg/dl",
            // xValueType: "dateTime",
            // xValueFormatString:"DD MMM HH:mm:ss",
            dataPoints: data
        };
    }
    getBGChart(data:any){
        return  {
            type: "line",
            markerType: "none",
            color:"black",
            showInLegend: true, 
            legendText: "BG Target",
            // xValueType: "dateTime",
            // xValueFormatString:"DD MMM HH:mm",
            dataPoints: data
        };
    }      
    getCGMChart(data:any){
        return  {
            type: "scatter",
            markerSize: 6,
           axisYType: "secondary",
            color:"#3C55A0",//blue
            showInLegend: true, 
            legendText: "CGM",
            // xValueType: "string",
            valueFormatString:function (e) {

                const hours = Math.floor(e.value/360);
                const carry = e.value%360;
        
                const minutes =  Math.floor(carry/60);
                const carry2 = carry%60
        
                const sec = carry2;
                return hours+':'+minutes+':'+sec;
            },
            dataPoints: data
        };
    } 
    
    getBGDotChart(data:any){
        return  {
            type: "scatter",
            markerSize: 10,
            // axisYType: "secondary",
            color:"#ff4d4d",//red
            showInLegend: true, 
            legendText: "BG",
            // xValueType: "dateTime",
            // xValueFormatString:"DD MMM HH:mm:ss",
            dataPoints: data
        };
    } 

    //Bottom Charts (For Insulin Delivery)
    getInsulinDeliveyChart(data:any){
        return  {
            type: "stepLine", 
            markerType: "none",
            showInLegend: true, 
            legendText: "insulin delivery",
            color: "#CD5546" ,//red
            // xValueType: "dateTime",
            // xValueFormatString:"DD MMM HH:mm:ss",
            dataPoints: data
        };
    }
    getMealBolusChart(data:any){
        return  {
            type: "column",
            axisYType: "secondary",
            color: "#1E327D",//blue
            showInLegend: true, 
            legendText: "Meal Bolus",
            lineThickness: 30,
            // xValueType: "dateTime",
            // xValueFormatString:"DD MMM HH:mm:ss",
            dataPoints: data
        };
    }      
    getBasalRateChart(data:any){
        return  {
            type: "line",
            lineDashType: "dash",
            axisYType: "secondary",
            color: "#6EAA5F",//green
            showInLegend: true, 
            legendText: "Basal Rate",
            lineThickness: 4,
            // xValueType: "dateTime",
            // xValueFormatString:"YYYY DD MMM HH:mm:ss",
            dataPoints: data
        };
    } 

}