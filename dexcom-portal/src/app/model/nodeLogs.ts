import {Stream} from '../model/stream';
import {Log} from '../model/log';
export class NodeLogs{
    deviceId:number;
    deviceEGVLogs: Stream[];
    deviceBGLogs: Log[];
}