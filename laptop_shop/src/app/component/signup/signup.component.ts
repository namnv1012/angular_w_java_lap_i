import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {UserModel} from "../../model/user.model";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  signForm: FormGroup = new FormGroup({});
  userCreate: UserModel = new UserModel();
  checkPass = false;
  typePass = 'password';

  constructor(private formBuilder: FormBuilder, private toastr: ToastrService,
              private router: Router, private authService: AuthService) {
    this.buildForm();
  }

  ngOnInit(): void {
  }

  buildForm() {
    this.signForm = this.formBuilder.group({
      email: new FormControl('', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]),
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      passwordConfirm: new FormControl('', [Validators.required]),
      phone: new FormControl('', [Validators.required])
    })
  }

  submit() {
    this.userCreate.login = this.f['username'].value;
    this.userCreate.password = this.f['password'].value;
    this.userCreate.email = this.f['email'].value;
    this.userCreate.phoneNumber = this.f['phone'].value;

    this.authService.createAccount(this.userCreate).subscribe(res => {
      if (res != null) {
        this.toastr.success("Tạo tài khoản thành công!");
        this.router.navigate(['/']);
      } else {
        this.toastr.error("Tạo tài khoản thất bại!");
      }
    }, error => {
      this.toastr.error("Tạo tài khoản thất bại!");
    })
  }

  get f() {
    return this.signForm.controls;
  }

  checkPassConfirm() {
    const passNew = this.f['password'].value;
    const passwordConfirm = this.f['passwordConfirm'].value;
    if (passNew === '') {
      this.checkPass = false;
    } else {
      if (passwordConfirm !== '') {
        if (passNew !== passwordConfirm) {
          this.checkPass = true;
        } else {
          this.checkPass = false;
        }
      }
    }
  }

  changeTypePassConfirm() {
    if (this.typePass === 'password') {
      this.typePass = 'text';
    } else {
      this.typePass = 'password';
    }
    console.log(this.typePass);
  }
}
