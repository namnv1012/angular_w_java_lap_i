export class ProductModel {
  id: number | undefined;
  code: string | undefined;
  producerCode: string | undefined;
  name: string | undefined;
  imageUrl: any;
  quantity: number | undefined; /* số lượng sản phẩm */
  price: number | undefined;
  content: string | undefined;
  synopsis: string | undefined; /* tóm tắt */
  screenSize: string | undefined; /* kích thước */
  resolution: string | undefined; /* độ phân giải */
  os: string | undefined;
  cpu: string | undefined;
  gpu: string | undefined;
  ram: string | undefined;
  rom: string | undefined;
  batteryCapacity: string | undefined; /* Dung lượng pin*/
  weight: string | undefined;
  networdConnect: string | undefined; /* Kết nối mạng */
  imageUrl2: any;
  imageUrl3: any;
  sale: number | undefined;
  priceSale: number | undefined;
}
