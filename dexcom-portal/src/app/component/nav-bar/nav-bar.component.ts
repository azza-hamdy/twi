import { Component, OnInit } from '@angular/core';
import {LoginService} from '../../service/login.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  myLocalStorage;
  isIn = false;   // store state
  currentTab : string = "none";
  toggleState() { // click handler
      let bool = this.isIn;
      this.isIn = bool === false ? true : false; 
    }
  constructor (private loginService:LoginService) {
    this.myLocalStorage=localStorage;
  }

  ngOnInit() {
  }

  onClick() {
    if (this.loginService.checkLogin()) {
      this.loginService.logout();
    }
  }
}
