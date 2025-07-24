import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlunoService } from '../../services/aluno.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { Aluno } from '../../models/aluno.model';

import { FileUploadComponent } from '../../../../shared/components/file-upload/file-upload.component';
import { LoadingSpinnerComponent } from '../../../../shared/components/loading-spinner/loading-spinner.component';

@Component({
  selector: 'app-aluno-import',
  standalone: true,
  imports: [
    CommonModule,             // *ngIf, *ngFor, pipe number, etc.
    FileUploadComponent,       // <app-file-upload>
    LoadingSpinnerComponent    // <app-loading-spinner>
  ],
  templateUrl: './aluno-import.component.html',
  styleUrls: ['./aluno-import.component.scss']
})
export class AlunoImportComponent implements OnInit {
  importResults: Aluno[] = [];
  isProcessing = false;

  constructor(
    private alunoService: AlunoService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {}

  onFileUploaded(file: File): void {
    this.isProcessing = true;
    this.alunoService.importarExcel(file).subscribe({
      next: () => {
        this.notificationService.showSuccess("Alunos importados com sucesso!");
        this.isProcessing = false;
        this.refreshList();
      },
      error: (error) => {
        const msg = typeof error?.error === 'string' ? error.error : 'Erro ao processar importação';
        this.notificationService.showError(msg);
        this.isProcessing = false;
      }
    });
  }

  onFileUploadError(msg: string): void {
    this.notificationService.showError(msg);
  }

  refreshList(): void {
    this.alunoService.listarEstatisticas().subscribe({
      next: (alunos) => {
        this.importResults = alunos;
      },
      error: () => {
        this.notificationService.showError('Erro ao carregar alunos');
      }
    });
  }
}
