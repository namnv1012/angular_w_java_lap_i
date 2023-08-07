import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {CategoryService} from '../../../../../core/service/service-model/category.service';
import {ProducerService} from '../../../../../core/service/service-model/producer.service';
import {ProductService} from '../../../../../core/service/service-model/product.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ProductModel} from '../../../../../core/service/model/product.model';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-create-update-product',
  templateUrl: './create-update-product.component.html',
  styleUrls: ['./create-update-product.component.scss']
})
export class CreateUpdateProductComponent implements OnInit {

  form: FormGroup;
  action = 'create';
  lstCategory: any = [];
  lstProducer: any = [];
  fileProduct;
  product: ProductModel = new ProductModel();
  // upload;
  imageFile: File;
  imageName: any;
  image1: File;
  imageUrl = null;
  // upload2;
  imageFile2: File;
  imageName2: any;
  image2: File;
  imageUrl2 = null;
  // upload3;
  imageFile3: File;
  imageName3: any;
  image3: File;
  imageUrl3 = null;
  queryParam;
  productId;
  lstImage: any = [];
  loading = false;
  constructor(private fb: FormBuilder,
              private toaStr: ToastrService,
              private categoryService: CategoryService,
              private producerService: ProducerService,
              private productService: ProductService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private changeDetectorRef: ChangeDetectorRef) {
    this.activatedRoute.queryParams.subscribe(param => {
      this.queryParam = param;
    });
    this.buildForm();
  }

  ngOnInit(): void {
    this.productId = this.queryParam.id;
    this.getListCategory();
    this.getListProducer();
    if (this.productId !== null && this.productId !== undefined) {
      this.action = 'update'
      this.loadData();
    }
  }

  buildForm() {
    this.form = this.fb.group({
      code: new FormControl('', [Validators.required, Validators.maxLength(50)], this.productService.validateCode()),
      name: new FormControl('', [Validators.required, Validators.maxLength(250)]),
      categoryCode: new FormControl(null, [Validators.required]),
      producerCode: new FormControl(null, [Validators.required]),
      price: new FormControl('', [Validators.required]),
      sale: new FormControl(0, [Validators.required]),
      content: new FormControl(''),
      synopsis: new FormControl(''),
      screenSize: new FormControl('', [Validators.required]),
      resolution: new FormControl('', [Validators.required]),
      os: new FormControl('', [Validators.required]),
      cpu: new FormControl('', [Validators.required]),
      gpu: new FormControl('', [Validators.required]),
      ram: new FormControl('', [Validators.required]),
      rom: new FormControl('', [Validators.required]),
      batteryCapacity: new FormControl('', [Validators.required]),
      weight: new FormControl('', [Validators.required]),
      networdConnect: new FormControl('', [Validators.required]),
      guarantee: new FormControl('', [Validators.required]),
      imgProduct1: new FormControl(''),
      imgProduct2: new FormControl(''),
      imgProduct3: new FormControl(''),
    })
  }

  submit() {
    this.loading = true;
    this.product.code = this.getControl.code.value;
    this.product.name = this.getControl.name.value;
    this.product.categoryCode = this.getControl.categoryCode.value;
    this.product.producerCode = this.getControl.producerCode.value;
    this.product.synopsis = this.getControl.synopsis.value;
    this.product.content = this.getControl.content.value;
    this.product.screenSize = this.getControl.screenSize.value;
    this.product.resolution = this.getControl.resolution.value;
    this.product.os = this.getControl.os.value;
    this.product.cpu = this.getControl.cpu.value;
    this.product.gpu = this.getControl.gpu.value;
    this.product.ram = this.getControl.ram.value;
    this.product.rom = this.getControl.rom.value;
    this.product.batteryCapacity = this.getControl.batteryCapacity.value;
    this.product.weight = this.getControl.weight.value;
    this.product.networdConnect = this.getControl.networdConnect.value;
    this.product.price = this.getControl.price.value;
    this.product.sale = this.getControl.sale.value;
    this.product.guarantee = this.getControl.guarantee.value;
    const formData = new FormData();
    const rqStr = JSON.stringify(this.product);
    formData.append('productDTO', new Blob([rqStr], {type: 'application/json'}));
    if (this.imageFile !== null || this.imageFile !== undefined) {
      formData.append('imgProduct1', this.imageFile);
    }
    if (this.imageFile2 !== null || this.imageFile2 !== undefined) {
      formData.append('imgProduct2', this.imageFile2);
    }
    if (this.imageFile3 !== null || this.imageFile3 !== undefined) {
      formData.append('imgProduct3', this.imageFile3);
    }
    this.productService.saveProduct(formData).subscribe(res => {
      this.loading = false;
      if (res !== null) {
        if (this.action === 'create') {
          this.toaStr.success('Thêm mới sản phẩm thành công')
        } else {
          this.toaStr.success('Cập nhật sản phẩm thành công');
        }
        this.cancel();
        return;
      } else {
        if (this.action === 'create') {
          this.toaStr.error('Thêm mới sản phẩm thất bại')
        } else {
          this.toaStr.error('Cập nhật sản phẩm thất bại');
        }
      }
    }, error => {
      this.loading = false;
    });
  }

  cancel() {
    this.router.navigate(['/system/manages-product'])
  }

  get getControl() {
    return this.form.controls;
  }

  getListCategory() {
    this.categoryService.getAllByStatusActive().subscribe(res => {
      this.lstCategory = res;
      this.changeDetectorRef.detectChanges();
    })
  }

  getListProducer() {
    this.producerService.getAllByStatusActive().subscribe(res => {
      this.lstProducer = res;
      this.changeDetectorRef.detectChanges();
    })
  }

  onFileChange($event: any) {
    this.fileProduct = $event.target.files[0];
  }

  processUploadImage1(imageInput) {
    this.image1 = imageInput.files[0];
    if (this.image1.type === 'image/jpeg' || this.image1.type === 'image/png') {
      this.imageFile = imageInput.files[0];
      this.imageName = this.image1.name;
      const reader = new FileReader();
      reader.readAsDataURL(this.imageFile);
      reader.onload = (_event) => {
        this.imageUrl = reader.result;
        this.changeDetectorRef.detectChanges();
      }
    } else {
      this.toaStr.error('File ảnh 1 vượt quá dung lượng!');
      return;
    }
  }

  processUploadImage2(imageInput) {
    this.image2 = imageInput.files[0];
    if (this.image2.type === 'image/jpeg' || this.image2.type === 'image/png') {
      this.imageFile2 = imageInput.files[0];
      this.imageName2 = this.image2.name;
      const reader = new FileReader();
      reader.readAsDataURL(this.imageFile2);
      reader.onload = (_event) => {
        this.imageUrl2 = reader.result;
        this.changeDetectorRef.detectChanges();
      }
    } else {
      this.toaStr.error('File ảnh 2 không đúng định dạng!');
      return;
    }
  }

  processUploadImage3(imageInput) {
    this.image3 = imageInput.files[0];
    if (this.image3.type === 'image/jpeg' || this.image3.type === 'image/png') {
      this.imageFile3 = imageInput.files[0];
      this.imageName3 = this.image3.name;
      const reader = new FileReader();
      reader.readAsDataURL(this.imageFile3);
      reader.onload = (_event) => {
        this.imageUrl3 = reader.result;
        this.changeDetectorRef.detectChanges();
      }
    } else {
      this.toaStr.error('File ảnh 3 không đúng định dạng!');
      return;
    }
  }

  loadData() {
    this.productService.findById(this.productId).subscribe(res => {
      this.product = res;
      // tslint:disable-next-line:forin
      for (const controlName in this.form.controls) {
        this.form.get(controlName).setValue(this.product[controlName]);
      }
      this.form.controls.code.setErrors({codeExitsts: false});
      console.log(this.form);
      this.imageUrl = this.product.imageUrl;
      this.imageUrl2 = this.product.imageUrl2;
      this.imageUrl3 = this.product.imageUrl3;
      this.changeDetectorRef.detectChanges();
    })
  }
}
