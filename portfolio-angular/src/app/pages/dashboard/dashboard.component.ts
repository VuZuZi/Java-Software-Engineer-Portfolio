import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { catchError, of } from 'rxjs';
import { ApiService, ApiProject, ApiUser } from '../../shared/api.service';
import { profile, projects, skills } from '../../shared/portfolio-data';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  profile = {
    name: profile.name,
    email: profile.email,
    avatarUrl: profile.avatarUrl,
  };
  projects = projects;
  stats = {
    projectsCount: projects.length,
    skillsCount: skills.length,
    avgMatch: 86,
  };

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.api.dashboard().pipe(catchError(() => of(null))).subscribe((dashboard) => {
      if (!dashboard) {
        return;
      }

      this.profile = this.mapUser(dashboard.user);
      this.stats = dashboard.stats;
      this.projects = this.mapProjects(dashboard.projects);
    });
  }

  private mapUser(user: ApiUser) {
    return {
      name: user.name || profile.name,
      email: user.email || profile.email,
      avatarUrl: user.picture || profile.avatarUrl,
    };
  }

  private mapProjects(apiProjects: ApiProject[]) {
    if (!apiProjects?.length) {
      return projects;
    }

    return apiProjects.map((project, index) => ({
      name: project.name || projects[index % projects.length].name,
      description: project.description || project.technologies || '',
      image: project.imageUrl || project.logoUrl || projects[index % projects.length].image,
      status: project.status || 'OPEN',
      skills: project.requiredSkills?.map((skill) => skill.name) || [],
    }));
  }
}
