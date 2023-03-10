import {Component, Input} from '@angular/core';
import Profile from "../../shared/domain/Profile";
import {Router} from "@angular/router";

@Component({
  selector: 'app-card-story',
  templateUrl: './card-story.component.html',
  styleUrls: ['./card-story.component.scss']
})
export class CardStoryComponent {
  @Input() isForm: boolean = false;
  @Input() profile!: Profile;

  constructor(private router: Router) {
  }

  navigate(link: string) {
    this.router.navigate([link], {})
  }
}
