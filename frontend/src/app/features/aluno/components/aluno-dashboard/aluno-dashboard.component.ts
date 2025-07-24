import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AlunoService } from '../../services/aluno.service';
import { Aluno } from '../../models/aluno.model';
import { NotificationService } from '../../../../core/services/notification.service';
import { LoadingService } from '../../../../core/services/loading.service';

interface DashboardStats {
  totalAlunos: number;
  idadeMedia: number;
  mediaGeralNotas: number;
  alunoMaisNovo: Aluno | null;
  alunoMaisVelho: Aluno | null;
  melhorMedia: number;
  piorMedia: number;
}

@Component({
  selector: 'app-aluno-dashboard',
  templateUrl: './aluno-dashboard.component.html',
  styleUrls: ['./aluno-dashboard.component.scss']
})
export class AlunoDashboardComponent implements OnInit, OnDestroy {
  stats: DashboardStats = {
    totalAlunos: 0,
    idadeMedia: 0,
    mediaGeralNotas: 0,
    alunoMaisNovo: null,
    alunoMaisVelho: null,
    melhorMedia: 0,
    piorMedia: 0
  };

  alunos: Aluno[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private alunoService: AlunoService,
    private notificationService: NotificationService,
    private loadingService: LoadingService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadDashboardData(): void {
    this.loadingService.show();

    this.alunoService.listarEstatisticas()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (alunos) => {
          this.alunos = alunos;
          this.calculateStats(alunos);
          this.loadingService.hide();
        },
        error: (error) => {
          this.notificationService.showError('Erro ao carregar dados do dashboard');
          this.loadingService.hide();
          console.error('Erro ao carregar dashboard:', error);
        }
      });
  }

  private calculateStats(alunos: Aluno[]): void {
    if (alunos.length === 0) {
      this.stats = {
        totalAlunos: 0,
        idadeMedia: 0,
        mediaGeralNotas: 0,
        alunoMaisNovo: null,
        alunoMaisVelho: null,
        melhorMedia: 0,
        piorMedia: 0
      };
      return;
    }

    // Total de alunos
    this.stats.totalAlunos = alunos.length;

    // Idade média
    const somaIdades = alunos.reduce((sum, aluno) => sum + aluno.idade, 0);
    this.stats.idadeMedia = Math.round(somaIdades / alunos.length * 100) / 100;

    // Média geral das notas
    const somaMedias = alunos.reduce((sum, aluno) => sum + aluno.mediaNotas, 0);
    this.stats.mediaGeralNotas = Math.round(somaMedias / alunos.length * 100) / 100;

    // Aluno mais novo e mais velho
    const alunosOrdenadosPorIdade = [...alunos].sort((a, b) => a.idade - b.idade);
    this.stats.alunoMaisNovo = alunosOrdenadosPorIdade[0];
    this.stats.alunoMaisVelho = alunosOrdenadosPorIdade[alunosOrdenadosPorIdade.length - 1];

    // Melhor e pior média
    const medias = alunos.map(aluno => aluno.mediaNotas);
    this.stats.melhorMedia = Math.max(...medias);
    this.stats.piorMedia = Math.min(...medias);
  }

  refreshDashboard(): void {
    this.loadDashboardData();
  }
}