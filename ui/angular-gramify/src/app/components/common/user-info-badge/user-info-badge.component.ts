import {Component, Input} from '@angular/core';
import Profile from "../../../shared/domain/Profile";

@Component({
  selector: 'app-user-info-badge',
  templateUrl: './user-info-badge.component.html',
  styleUrls: ['./user-info-badge.component.scss']
})
export class UserInfoBadgeComponent {
  @Input() profile!: Profile;
  @Input() hasVisibility: boolean = false;

}
