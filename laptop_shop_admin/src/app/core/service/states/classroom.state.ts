import {Injectable} from '@angular/core';
import {ClassroomModel} from '../model/classroom.model';
import {Selector, State} from '@ngxs/store';
import {ClassroomService} from '../service-model/classroom.service';

@Injectable()
export class ClassroomStateModel {
  classrooms : ClassroomModel[];
}
@State<ClassroomStateModel>({
  name: 'classroom',
  defaults: {
    classrooms: []
  }
})
@Injectable()
export class ClassroomState{
  constructor(private classroomService: ClassroomService) {
  }

  @Selector()
  static getAllClassroom(state: ClassroomStateModel) {
    return state.classrooms;
  }
}
