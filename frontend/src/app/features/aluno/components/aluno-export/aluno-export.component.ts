import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AlunoService } from '../../services/aluno.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { LoadingService } from '../../../../core/services/loading.service';

@Component({
  selector: 'app-aluno-export',
  templateUrl: './aluno-export.component.html',
  styleUrls: ['./aluno-export.component.scss']
})
export class AlunoExportComponent implements OnInit, OnDestroy {
  totalAlunos: number = 0;
  isExporting: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(
    private alunoService: AlunoService,
    private notificationService: NotificationService,
    private loadingService: LoadingService
  ) {}

  ngOnInit(): void {
    this.loadAlunosCount();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadAlunosCount(): void {
    this.alunoService.listarEstatisticas()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (alunos) => {
          this.totalAlunos = alunos.length;
        },
        error: (error) => {
          console.error('Erro ao carregar contagem de alunos:', error);
        }
      });
  }

  exportarExcel(): void {
    if (this.totalAlunos === 0) {
      this.notificationService.showWarning('Não há dados para exportar');
      return;
    }

    this.isExporting = true;
    this.loadingService.show();

    this.alunoService.exportarExcel()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (blob) => {
          this.downloadFile(blob);
          this.notificationService.showSuccess('Arquivo exportado com sucesso!');
          this.isExporting = false;
          this.loadingService.hide();
        },
        error: (error) => {
          this.notificationService.showError('Erro ao exportar arquivo');
          this.isExporting = false;
          this.loadingService.hide();
          console.error('Erro na exportação:', error);
        }
      });
  }

  private downloadFile(blob: Blob): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    
    const today = new Date();
    const dateStr = today.toISOString().split('T')[0];
    link.download = `alunos_estatisticas_${dateStr}.xlsx`;
    
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }

  refreshCount(): void {
    this.loadAlunosCount();
  }
}