import {Routes} from '@angular/router';
import {DashboardComponent} from '../../views/pages/system/dashboard/dashboard.component';

// @ts-ignore
import {IconsComponent} from '../../pages/icons/icons.component';
// @ts-ignore
import {MapsComponent} from '../../pages/maps/maps.component';
// @ts-ignore
import {TablesComponent} from '../../pages/tables/tables.component';
// @ts-ignore
import {ManagentUserComponent} from 'src/app/pages/managent-user/managent-user.component';
import {UserProfileComponent} from '../../views/partials/layout';

export const AdminLayoutRoutes: Routes = [
  {path: 'dashboard', component: DashboardComponent},
  {path: 'user-profile', component: UserProfileComponent},
  {path: 'tables', component: TablesComponent},
  {path: 'icons', component: IconsComponent},
  {path: 'maps', component: MapsComponent},
  {path: 'managent-user', component: ManagentUserComponent}
];
