import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {
  @Input() acceptedTypes: string = '.xlsx,.xls';
  @Input() maxSizeInMB: number = 10;
  @Input() placeholder: string = 'Selecione um arquivo Excel...';
  @Input() disabled: boolean = false;

  @Output() fileSelected = new EventEmitter<File>();
  @Output() fileError = new EventEmitter<string>();

  selectedFile: File | null = null;
  dragOver: boolean = false;

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.handleFile(input.files[0]);
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.dragOver = true;
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.dragOver = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.dragOver = false;
    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      this.handleFile(event.dataTransfer.files[0]);
    }
  }

  private handleFile(file: File): void {
    if (!this.isValidFileType(file)) {
      this.fileError.emit('Tipo de arquivo não permitido. Use apenas arquivos Excel (.xlsx, .xls)');
      return;
    }
    if (!this.isValidFileSize(file)) {
      this.fileError.emit(`Arquivo muito grande. Tamanho máximo: ${this.maxSizeInMB}MB`);
      return;
    }
    this.selectedFile = file;
    this.fileSelected.emit(file);
  }

  private isValidFileType(file: File): boolean {
    const validTypes = [
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      'application/vnd.ms-excel'
    ];
    return validTypes.includes(file.type);
  }

  private isValidFileSize(file: File): boolean {
    const maxSizeInBytes = this.maxSizeInMB * 1024 * 1024;
    return file.size <= maxSizeInBytes;
  }

  clearFile(): void {
    this.selectedFile = null;
  }
}
