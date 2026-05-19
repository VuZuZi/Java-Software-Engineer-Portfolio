import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { profile, projects, skills } from '../../shared/portfolio-data';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  readonly profile = profile;
  readonly projects = projects;
  readonly stats = {
    projectsCount: projects.length,
    skillsCount: skills.length,
    avgMatch: 86,
  };
}
