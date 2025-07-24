import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/alunos',
    pathMatch: 'full'
  },
  {
    path: 'alunos',
    loadChildren: () => import('./features/aluno/aluno.module').then(m => m.AlunoModule)
  },
  {
    path: '**',
    redirectTo: '/alunos'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    enableTracing: false,
    useHash: false
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }