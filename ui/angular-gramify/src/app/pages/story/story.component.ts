import {Component, Input, OnInit} from '@angular/core';
import Profile from "../../shared/domain/Profile";
import {ProfileService} from "../../services/profile.service";

@Component({
  selector: 'app-story',
  templateUrl: './story.component.html',
  styleUrls: ['./story.component.scss']
})
export class StoryComponent implements OnInit {
  @Input() profile!: Profile;

  constructor(private profileService: ProfileService) {
  }

  ngOnInit(): void {
    this.profile = this.profileService.getProfile()
  }
}
