import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute, Router} from "@angular/router";
import {OwlOptions} from "ngx-owl-carousel-o";
import {CheckoutService} from '../../service/checkout.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  lstProductNew: any[] = [];
  lstProductSale: any[] = [];
  listSlide: any[] = [
    {
      id: 1,
      src: "assets/images/slide_laptop/laptop1.jpg"
    },
    {
      id: 2,
      src: "assets/images/slide_laptop/laptop2.jpg"
    },
    {
      id: 3,
      src: "assets/images/slide_laptop/laptop3.jpg"
    }
  ];

  customOptions: OwlOptions = {
    loop: true,
    autoplay: true,
    center: true,
    dots: false,
    autoHeight: true,
    autoWidth: true,
    navSpeed: 200,
    navText: ['', ''],
    nav: true,
    responsive: {
      0: {
        items: 1,
      },
      600: {
        items: 1,
      },
      1000: {
        items: 1,
      }
    }
  }

  constructor(private customerService: CustomerService, private toastr: ToastrService,
              private router: Router, private _route: ActivatedRoute, private checkoutService: CheckoutService) {
  }

  ngOnInit(): void {
    this.loadData();
    this._route.queryParams.subscribe(async (params) => {
      let query = '';
      Object.keys(params).forEach(key => {
        query = `${key} =${params[key]}&`
      })
      this.checkoutService.verifi(query).subscribe(res =>{
        console.log(res);
      })
    })
  }

  loadData() {
    this.customerService.getDataHome().subscribe(res => {
      console.log("res",res);
      this.lstProductNew = res.lstProductNew;
      this.lstProductSale = res.lstProductSale;
      console.log("List pro",this.lstProductSale);
    })
  }

  routerProductDetail(id: number) {
    this.router.navigate(['/product', {id}]);
  }

  convertPrice(priceSell: number) {
    return Number(priceSell).toLocaleString('en-US');
  }
}
