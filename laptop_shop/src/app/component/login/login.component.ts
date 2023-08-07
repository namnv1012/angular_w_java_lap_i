import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {LoginService} from "../../service/login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{

  login: FormGroup = new FormGroup({});

  constructor(private fb: FormBuilder, private toastr: ToastrService, private router: Router, private loginService: LoginService ){
    this.buildForm();
  }

  ngOnInit(): void {
  }

  buildForm(){
    this.login = this.fb.group({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    })
  }
  submit(){
    const username = this.f['username'].value;
    const password = this.f['password'].value;
    this.loginService.login(username, password).subscribe(res =>{
      if(res !== null){
        localStorage.setItem('userLogin', JSON.stringify(res.currentUser));
        sessionStorage.setItem('token', res.token);
        this.router.navigate(['/']);
        this.toastr.success('Đăng nhập thành công!');
        setTimeout(function (){
          window.location.reload();
        }, 1000)
      }else{
        this.toastr.error('Đăng nhập thất bại!')
      }
    }, error => {
      this.toastr.error('Tài khoản và mật khẩu không chính xác!');
    })
  }
  get f() {
    return this.login.controls;
  }

  routerSignup(){
    this.router.navigate(['/signup'])
  }
  routerForgotPassword(){
    this.router.navigate(['/forgot-password'])
  }
}
