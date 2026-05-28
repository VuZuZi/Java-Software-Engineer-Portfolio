import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { Project, ProjectPayload } from './project.model';
import { ProjectService } from './project.service';

type ProjectForm = ProjectPayload & { id?: number };

const emptyForm: ProjectForm = {
  title: '',
  description: '',
  imageUrl: '',
  sourceUrl: '',
  liveUrl: '',
  technologies: ''
};

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  projects: Project[] = [];
  form: ProjectForm = { ...emptyForm };
  isLoading = false;
  isSaving = false;
  errorMessage = '';

  constructor(private readonly projectService: ProjectService) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  get isEditing(): boolean {
    return this.form.id !== undefined;
  }

  loadProjects(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.projectService.getProjects()
      .pipe(finalize(() => this.isLoading = false))
      .subscribe({
        next: (projects) => this.projects = projects,
        error: () => this.errorMessage = 'Khong the tai danh sach project. Kiem tra backend o cong 8080.'
      });
  }

  saveProject(): void {
    const payload = this.toPayload();
    if (!payload.title || !payload.description) {
      this.errorMessage = 'Title va description la bat buoc.';
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    const request = this.isEditing
      ? this.projectService.updateProject(this.form.id as number, payload)
      : this.projectService.createProject(payload);

    request
      .pipe(finalize(() => this.isSaving = false))
      .subscribe({
        next: () => {
          this.resetForm();
          this.loadProjects();
        },
        error: () => this.errorMessage = 'Khong the luu project. Vui long kiem tra du lieu hoac backend.'
      });
  }

  editProject(project: Project): void {
    this.form = {
      id: project.id,
      title: project.title,
      description: project.description,
      imageUrl: project.imageUrl ?? '',
      sourceUrl: project.sourceUrl ?? '',
      liveUrl: project.liveUrl ?? '',
      technologies: project.technologies ?? ''
    };
  }

  deleteProject(project: Project): void {
    if (!confirm(`Xoa project "${project.title}"?`)) {
      return;
    }

    this.errorMessage = '';
    this.projectService.deleteProject(project.id)
      .subscribe({
        next: () => this.projects = this.projects.filter((item) => item.id !== project.id),
        error: () => this.errorMessage = 'Khong the xoa project nay.'
      });
  }

  resetForm(): void {
    this.form = { ...emptyForm };
  }

  private toPayload(): ProjectPayload {
    return {
      title: this.form.title.trim(),
      description: this.form.description.trim(),
      imageUrl: this.cleanOptional(this.form.imageUrl),
      sourceUrl: this.cleanOptional(this.form.sourceUrl),
      liveUrl: this.cleanOptional(this.form.liveUrl),
      technologies: this.cleanOptional(this.form.technologies)
    };
  }

  private cleanOptional(value: string | null | undefined): string | null {
    const cleanValue = value?.trim();
    return cleanValue ? cleanValue : null;
  }
}
