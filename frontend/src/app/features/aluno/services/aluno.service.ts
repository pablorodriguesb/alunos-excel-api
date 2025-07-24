import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { AlunoDTO } from '../models/aluno.model';

@Injectable({
  providedIn: 'root'
})
export class AlunoService {
  private readonly API_URL = `${environment.apiUrl}/api/alunos`;

  constructor(private http: HttpClient) {}

  /**
   * Importa arquivo Excel - integra com POST /api/alunos/importar
   * @param file - Arquivo Excel (.xlsx)
   */
  importarExcel(file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<void>(`${this.API_URL}/importar`, formData);
  }

  /**
   * Lista estatísticas dos alunos - integra com GET /api/alunos/estatisticas
   * @returns Lista ordenada por idade (conforme backend)
   */
  listarEstatisticas(): Observable<AlunoDTO[]> {
    return this.http.get<AlunoDTO[]>(`${this.API_URL}/estatisticas`);
  }

  /**
   * Exporta planilha Excel - integra com GET /api/alunos/exportar
   * @returns Blob do arquivo Excel
   */
  exportarExcel(): Observable<Blob> {
    const headers = new HttpHeaders({
      'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });

    return this.http.get(`${this.API_URL}/exportar`, {
      headers,
      responseType: 'blob' 
    });
  }

  /**
   * Helper para trigger do download do arquivo exportado
   * @param blob - Blob retornado da API
   * @param filename - Nome do arquivo (default: alunos.xlsx)
   */
  downloadFile(blob: Blob, filename: string = 'alunos.xlsx'): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}