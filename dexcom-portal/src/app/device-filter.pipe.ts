import { Pipe, PipeTransform } from '@angular/core';
import { forEach } from '@angular/router/src/utils/collection';

@Pipe({
  name: 'deviceFilter'
})
export class DeviceFilterPipe implements PipeTransform {

  transform(value: any, allValue: any,filterString: string, propName: string): any {
    if(value.length === 0){
      return value;
    }
    if(filterString.length === 0){
      return value;
    }
    return (allValue || []).filter((item) => item[propName].indexOf(filterString)>-1);
  }

}
