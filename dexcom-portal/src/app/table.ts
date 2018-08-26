import {PaginationPage, PaginationPropertySort} from './model/pagination';

import * as Rx from "rxjs/Rx";

export interface Table<T> {

    fetchPage(pageNumber:number, pageSize:number, sort:PaginationPropertySort): Rx.Observable<PaginationPage<T>>;

}