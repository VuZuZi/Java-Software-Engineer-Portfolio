import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { catchError, of } from 'rxjs';
import { ApiService } from '../../shared/api.service';
import { skills } from '../../shared/portfolio-data';

@Component({
  selector: 'app-skills',
  imports: [CommonModule, RouterLink],
  templateUrl: './skills.component.html',
})
export class SkillsComponent implements OnInit {
  skills = skills;

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.api.skills().pipe(catchError(() => of([]))).subscribe((apiSkills) => {
      if (!apiSkills.length) {
        return;
      }

      this.skills = apiSkills.map((skill) => ({
        name: skill.name,
        category: skill.category || 'Khác',
        percent: skill.proficiencyPercent || 0,
        years: skill.yearsExperience || 0,
        icon: skill.icon || 'fas fa-cog',
      }));
    });
  }
}
