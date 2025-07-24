import { Routes } from '@angular/router';
import { AlunoDashboardComponent } from './features/aluno/components/aluno-dashboard/aluno-dashboard.component';
import { AlunoListComponent } from './features/aluno/components/aluno-list/aluno-list.component';
import { AlunoImportComponent } from './features/aluno/components/aluno-import/aluno-import.component';
import { AlunoExportComponent } from './features/aluno/components/aluno-export/aluno-export.component';

export const routes: Routes = [
  { path: '', redirectTo: '/alunos', pathMatch: 'full' },
  { path: 'alunos', component: AlunoListComponent },
  { path: 'alunos/dashboard', component: AlunoDashboardComponent },
  { path: 'alunos/importar', component: AlunoImportComponent },
  { path: 'alunos/exportar', component: AlunoExportComponent },
  { path: '**', redirectTo: '/alunos' } 
];
