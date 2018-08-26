import { Component, OnInit } from '@angular/core';
import {Observable}  from 'rxjs/Observable';
import {LoginService} from '../../service/login.service';
import { Router } from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  private model = {'username':'', 'password':''};
  private currentUserName;
  message:string='';
  
  constructor (private loginService: LoginService, private router: Router){
    this.currentUserName=localStorage.getItem("dexcom_currentUserName");
  }
  ngOnInit() {
  }

  
  onSubmit() {
    this.loginService.sendCredential(this.model).subscribe(
      data => {
                localStorage.setItem("dexcom_token", JSON.parse(JSON.stringify(data))._body);
                this.currentUserName=this.model.username;
                localStorage.setItem("dexcom_currentUserName", this.model.username);
                this.model.username='';
                this.model.password='';
                this.router.navigate(["devices"]);
              },
      () => this.message = 'invalid username or password'
    );

  }
}
