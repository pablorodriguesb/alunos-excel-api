import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root' // Singleton pattern - única instância
})
export class LoadingService {
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  show(): void {
    setTimeout(() => {
      this.loadingSubject.next(true);
    }, 0);
  }

  hide(): void {
    setTimeout(() => {
      this.loadingSubject.next(false);
    }, 0);
  }
}