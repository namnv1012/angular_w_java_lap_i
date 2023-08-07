import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";
import {ActivatedRoute, Router} from "@angular/router";
import {SearchProductModel} from "../../model/search-product.model";
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
})
export class ShopComponent implements OnInit {
  lstProduct: any = [];
  lstProducer: any = [];
  producer: any;
  _pageSize = 12;
  _page = 1;
  queryParam: any;
  searchProduct: SearchProductModel = new SearchProductModel();

  constructor(private customerService: CustomerService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private changeDetectorRef: ChangeDetectorRef,
              private toastr: ToastrService) {
    this.searchProduct.name = '';
    this.searchProduct.status = 1;
    this.activatedRoute.queryParams.subscribe(param => {
      this.queryParam = param;
    });
  }

  ngOnInit(): void {
    if (this.queryParam.productName !== undefined && this.queryParam.productName !== null) {
      this.searchProduct.name = this.queryParam.productName;
    }
    this.getAllProducer();
    this.getListProduct();
  }

  getListProduct() {
    this.customerService.getListProduct().subscribe(res => {
      this.lstProduct = res;
      this.changeDetectorRef.detectChanges();
    })
  }

  routerProductDetail(id: number) {
    this.router.navigate(['/product'], {
      queryParams: {
        id: id
      }
    })
  }

  getAllProducer() {
    this.customerService.getAllProducer().subscribe(res => {
      this.lstProducer = res;
      this.changeDetectorRef.detectChanges();
    })
  }

  convertPrice(priceSell: any) {
    return Number(priceSell).toLocaleString('en-US')
  }

  findByProducer() {
    this.customerService.getListProductByProducerCode(this.producer).subscribe(res => {
      if (res.length === 0) {
        this.toastr.error("Không tìm thấy dữ liệu phù hợp!");
      }
      this.lstProduct = res;
      this.changeDetectorRef.detectChanges();
    })
  }

  changeProducer(code: string) {
    this.producer = code;
  }
}
