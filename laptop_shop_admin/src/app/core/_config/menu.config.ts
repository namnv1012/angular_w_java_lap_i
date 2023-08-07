import {environment} from '../../../environments/environment';

export class MenuConfig {
  roleParam = environment.ROLE;
  ADMIN = this.roleParam.ADMIN;
  public defaults: any = {
    aside: {
      self: {},
      items: [
        {
          title: 'Trang chủ',
          page: '/system/dashboard',
          iconSvg: 'home.svg',
          translate: 'MENU.DASHBOARD',
          permission: [this.ADMIN],
        },
        {
          title: 'Quản lý danh mục',
          page: '/system/manages-category',
          iconSvg: 'goicuoc.svg',
          translate: 'MENU.MANAGER_CATEGORY',
          permission: [this.ADMIN],
        },
        {
          title: 'Quản lý nhà sản xuất',
          page: '/system/manages-producer',
          iconSvg: 'goicuoc.svg',
          translate: 'MENU.MANAGER_PRODUCER',
          permission: [this.ADMIN],
        },
        {
          title: 'Quản lý sản phẩm',
          iconSvg: 'Paper.svg',
          translate: 'MENU.MANAGER_PRODUCT',
          permission: [this.ADMIN],
          submenu: [
            {
              title: 'Danh sách sản phẩm',
              page: '/system/manages-product',
              translate: 'MENU.LIST_PRODUCT',
            },
            {
              title: 'Danh sách tồn kho',
              page: '/system/dashboard-quantity-product',
              translate: 'MENU.DASHBOARD_QUANTITY',
            }
          ]
        },
        {
          title: 'Quản lý đơn hàng',
          page: '/system/manages-orders',
          iconSvg: 'dashboard-report.svg',
          translate: 'MENU.MANAGER_ORDERS',
          permission: [this.ADMIN],
        },
        {
          title: 'Quản lý khách hàng',
          page: '/system/manages-user',
          iconSvg: 'hocsinh.svg',
          translate: 'MENU.MANAGER_CUSTOMER',
          permission: [this.ADMIN],
        },
        {
          title: 'Quản lý bảo hành',
          page: '/system/manages-warranty',
          iconSvg: 'Paper.svg',
          translate: 'MENU.MANAGER_WARRANTY',
          permission: [this.ADMIN],
        },
      ],
    },
  };

  public get configs(): any {
    return this.defaults;
  }
}
