export interface Log {
    id : number;
    logTime: number,
    cgmTxId: string,
    flags: number,
    estimatedGlucoseValue: number,
    bloodGlucose: number,
    bloodGlucoseTime: number,
    seqNo: number,
}