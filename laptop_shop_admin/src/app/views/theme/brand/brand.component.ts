import {AfterViewInit, Component, OnInit} from '@angular/core';
import {LayoutConfigService, ToggleOptions} from '../../../core/_base/layout';
import {SchoolServices} from '../../pages/system/school/school.service';
import {HtmlClassService} from '../html-class.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-brand',
  templateUrl: './brand.component.html',
})
export class BrandComponent implements OnInit, AfterViewInit {
  headerLogo = '';
  brandClasses = '';
  asideSelfMinimizeToggle = true;
  a = false;
  toggleOptions: ToggleOptions = {
    target: 'kt_body',
    targetState: 'aside-minimize',
    toggleState: 'active'
  };

  constructor(private layoutConfigService: LayoutConfigService, public htmlClassService: HtmlClassService, private schoolSv: SchoolServices) {
  }

  ngOnInit(): void {
    this.headerLogo = this.getAsideLogo();
    this.brandClasses = this.htmlClassService.getClasses('brand', true).toString();
    this.asideSelfMinimizeToggle = this.layoutConfigService.getConfig('aside.self.minimize.toggle');
  }

  ngAfterViewInit(): void {
  }

  getAsideLogo() {
    let result = 'logo-light.png';
    const brandSelfTheme = this.layoutConfigService.getConfig('brand.self.theme') || '';
    if (brandSelfTheme === 'light') {
      result = 'logo-dark.png';
    }
    return `./assets/media/logos/${result}`;
  }

  toggleAsideClick() {
    document.body.classList.toggle('aside-minimize');
    this.a = !this.a;
    this.schoolSv.sideBar.next(this.a);
    if (document.getElementById('logo_brand').classList.contains('d-none').valueOf()) {
      document.getElementById('logo_brand').classList.remove('d-none');
    } else {
      document.getElementById('logo_brand').classList.add('d-none');
    }
  }
}
