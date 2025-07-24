import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Notification, NotificationType } from '../../shared/models/notification.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationSubject = new BehaviorSubject<Notification | null>(null);
  public notification$ = this.notificationSubject.asObservable();

  showSuccess(message: string): void {
    this.show({ type: NotificationType.SUCCESS, message, duration: 3000 });
  }

  showError(message: string): void {
    this.show({ type: NotificationType.ERROR, message, duration: 5000 });
  }

  showWarning(message: string): void {
    this.show({ type: NotificationType.WARNING, message, duration: 4000 });
  }

  private show(notification: Notification): void {
    this.notificationSubject.next(notification);
    
    if (notification.duration) {
      setTimeout(() => this.clear(), notification.duration);
    }
  }

  clear(): void {
    this.notificationSubject.next(null);
  }
}