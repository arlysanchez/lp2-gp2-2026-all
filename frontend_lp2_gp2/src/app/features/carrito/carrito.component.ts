import { Component, inject } from '@angular/core';
import { CartService } from '../../core/services/cart.service';
import { AlertService } from '../../core/services/alert.service';
import { Router, RouterLink } from '@angular/router';
import { ItemVentaDTO, VentaDTO } from '../../core/interfaces/pedido.inteface';
import { PedidoService } from '../../core/services/pedido.service';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.css'
})
export class CarritoComponent {
  public cartService = inject(CartService);
  private pedidoService = inject(PedidoService);
  private alertService = inject(AlertService);
  private router = inject(Router);


async pagar(){
  //1. verificar que el usuario este logueado
  const token = localStorage.getItem('token_ecommerce');
  if(!token){
    this.alertService.error('Debes iniciar Sesion para realizar el pedido', 'No autorizado');
    return;
  }
  const confirmado = await this.alertService.confirm('Desea comfirmar su compra?');
  if(!confirmado) return;
  //2. obtener el Id de la persona logueada
  const idPersona = Number(localStorage.getItem('user_id_persona'));

// Mapear los items del carrito al formato DTO del backend
const itemsEnvio : ItemVentaDTO[] = this.cartService.getItems().map(item =>({
  idProducto : item.producto.id!,
  cantidad: item.cantidad
}));
//4. Contruir el objeto final
const pedidoFinal: VentaDTO={
  idPersona: idPersona,
  items:itemsEnvio
};
//5. enviar al back
this.pedidoService.creaPedido(pedidoFinal).subscribe({
  next: (res)=>{
    this.alertService.success('Tu pedido ha sido procesado con éxito','Compra Finalizada');
    this.cartService.vaciarCarrito();
    this.router.navigate(['/home']);
  },error: (err) =>{
    const msg = err.error.message || 'No se pudo procesar el pedido';
    this.alertService.error(msg,'Error en el pedido');
  }
});

}
}
