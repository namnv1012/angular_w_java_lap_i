import {Directive, Input} from '@angular/core'
import {NG_VALIDATORS, AbstractControl, Validators} from '@angular/forms'
import * as _ from 'lodash';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[ktSystems]',
  providers: [{provide: NG_VALIDATORS, useExisting: SystemsDirective, multi: true}]

})
export class SystemsDirective implements Validators {
  @Input() ktSystems: number;
  @Input() update;
  @Input() listAllTeacher: any [];
  test;

  constructor() {
  }

  validate(control: AbstractControl): Validators {
    if (control.value && this.ktSystems > 0) {
      if (control.value.length > this.ktSystems) {
        return {ktSystems: 'error'};
      }
      if (_.size(this.listAllTeacher)) {
        this.test = this.listAllTeacher.filter(item => item.code === control.value)[0];
      }
      if (this.test && !this.update) {
        return {errorCB: 'error'};
      }
    }
    return null;
  }
}
