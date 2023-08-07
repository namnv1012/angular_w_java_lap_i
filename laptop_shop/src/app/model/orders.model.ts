import {CartModel} from "./cart.model";

export class OrdersModel {
  id: number | undefined;
  name: string | undefined;
  phone: string | undefined;
  email: string | undefined;
  address: string | undefined;
  note: string | undefined;
  lstOrderItem: Array<CartModel> | undefined;
}
