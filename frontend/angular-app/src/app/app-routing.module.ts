import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {WelcomeComponent} from "./welcome/welcome.component";
import {RoomsComponent} from "./rooms/rooms.component";

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'rooms', component: RoomsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
