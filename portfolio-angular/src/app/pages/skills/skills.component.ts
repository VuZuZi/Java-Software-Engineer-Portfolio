import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { skills } from '../../shared/portfolio-data';

@Component({
  selector: 'app-skills',
  imports: [CommonModule, RouterLink],
  templateUrl: './skills.component.html',
})
export class SkillsComponent {
  readonly skills = skills;
}
