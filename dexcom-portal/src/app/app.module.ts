import {BrowserModule} from '@angular/platform-browser';
import {NgModule,} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {RouterModule, Routes} from '@angular/router';
import { APP_BASE_HREF } from '@angular/common';

import { AppComponent } from './app.component';
import { DeviceListComponent } from './component/device-list/device-list.component';
import { TablePaginationComponent } from './component/table-pagination/table-pagination.component';
import { DeviceService } from './service/device.service';
import { GraphComponent } from './component/graph/graph.component';
import { NavBarComponent } from './component/nav-bar/nav-bar.component';
import { LoginComponent } from './component/login/login.component';
import {LocationStrategy, HashLocationStrategy} from '@angular/common';

import {LoginService} from './service/login.service';

import { routing } from './app.routing';
import { AgmCoreModule } from '@agm/core';
import { MapComponent } from './component/map/map.component';
import { AddRangeComponent } from './component/add-range/add-range.component';
import { DeviceFilterPipe } from './device-filter.pipe';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
// const appRoutes: Routes = [
//     {path: '', component: DeviceListComponent},
// ];

@NgModule({
  declarations: [
    AppComponent,
    DeviceListComponent,
    TablePaginationComponent,
    GraphComponent,
    NavBarComponent,
    LoginComponent,
    MapComponent,
    AddRangeComponent,
    DeviceFilterPipe
  ],
  imports: [NgbModule.forRoot(),
    BrowserModule,
        FormsModule,
        HttpModule,
        routing,
        AgmCoreModule.forRoot({
          apiKey: 'AIzaSyCLQVyWyVgD1-RMovB0f_PssCIflFHAzNI'
        })
        // RouterModule.forRoot(appRoutes)
  ],
  providers: [LoginService,DeviceService,
              {provide: LocationStrategy, useClass: HashLocationStrategy}],
  bootstrap: [AppComponent]
})
export class AppModule { }
