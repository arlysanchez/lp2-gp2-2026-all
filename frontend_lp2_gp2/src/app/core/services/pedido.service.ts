import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { VentaDTO } from '../interfaces/pedido.inteface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {
  private http = inject(HttpClient);
  private apiUrl = '/api/v1/pedidos'; //ruta del proxy

  creaPedido(pedido: VentaDTO): Observable<any>{
    return this.http.post(this.apiUrl,pedido);
  }
  
}
