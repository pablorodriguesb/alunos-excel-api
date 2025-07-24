import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AlunoService } from '../../services/aluno.service';
import { Aluno } from '../../models/aluno.model';
import { NotificationService } from '../../../../core/services/notification.service';
import { LoadingService } from '../../../../core/services/loading.service';

@Component({
  selector: 'app-aluno-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './aluno-list.component.html',
  styleUrls: ['./aluno-list.component.scss']
})
export class AlunoListComponent implements OnInit, OnDestroy {
  alunos: Aluno[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private alunoService: AlunoService,
    private notificationService: NotificationService,
    private loadingService: LoadingService
  ) {}

  ngOnInit(): void {
    this.loadAlunos();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadAlunos(): void {
    this.loadingService.show();

    this.alunoService.listarEstatisticas()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (alunos) => {
          this.alunos = alunos;
          this.loadingService.hide();
        },
        error: (error) => {
          this.notificationService.showError('Erro ao carregar lista de alunos');
          this.loadingService.hide();
          console.error('Erro ao carregar alunos:', error);
        }
      });
  }

  refreshList(): void {
    this.loadAlunos();
  }

  // trackBy para *ngFor no template (evita warning e melhora performance)
  trackByIdentificacao(index: number, aluno: Aluno) {
    return aluno.identificacao;
  }
}
