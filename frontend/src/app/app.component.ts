import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoadingSpinnerComponent } from './shared/components/loading-spinner/loading-spinner.component';
import { NotificationComponent } from './shared/components/notification/notification.component';
import { LoadingService } from './core/services/loading.service';
import { NotificationService } from './core/services/notification.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    LoadingSpinnerComponent,
    NotificationComponent
  ],
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
