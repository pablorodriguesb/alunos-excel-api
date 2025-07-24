import { Component, OnInit } from '@angular/core';
import { LoadingService } from './core/services/loading.service';
import { NotificationService } from './core/services/notification.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'Sistema de Gerenciamento de Alunos';
  isLoading$: Observable<boolean>;

  constructor(
    private loadingService: LoadingService,
    private notificationService: NotificationService
  ) {
    this.isLoading$ = this.loadingService.loading$;
  }

  ngOnInit(): void {
    console.log('Sistema de Gerenciamento de Alunos iniciado');
  }
}
