package pe.edu.upeu.lp2_gp2.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.lp2_gp2.dto.DetalleResponseDTO;
import pe.edu.upeu.lp2_gp2.dto.ItemVentaDTO;
import pe.edu.upeu.lp2_gp2.dto.PedidoResponseDTO;
import pe.edu.upeu.lp2_gp2.dto.VentaDTO;
import pe.edu.upeu.lp2_gp2.entity.DetallePedido;
import pe.edu.upeu.lp2_gp2.entity.Pedido;
import pe.edu.upeu.lp2_gp2.entity.Persona;
import pe.edu.upeu.lp2_gp2.entity.Producto;
import pe.edu.upeu.lp2_gp2.exception.InsufficientStockException;
import pe.edu.upeu.lp2_gp2.exception.ResourceNotFoundException;
import pe.edu.upeu.lp2_gp2.repository.PedidoRepository;
import pe.edu.upeu.lp2_gp2.repository.PersonaRepository;
import pe.edu.upeu.lp2_gp2.repository.ProductoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements IPedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final PersonaRepository personaRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProductoRepository productoRepository, PersonaRepository personaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.personaRepository = personaRepository;
    }

    @Override
    @Transactional
    public PedidoResponseDTO realizarPedido(VentaDTO dto) {
        //1. validar el cliente
        Persona persona = personaRepository.findById(dto.idPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontraado"));
        //2. creamos la cabecera del pedido
        Pedido pedido = new Pedido();
        pedido.setPersona(persona);
        pedido.setEstado("pendiente");
        pedido.setFechaPedido(LocalDateTime.now());

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal totalPedido = BigDecimal.ZERO;
        //3. Procesamos cada item del carrito
        for (ItemVentaDTO item : dto.items()) {
          Producto producto = productoRepository.findById(item.idProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto" + item.idProducto() + "NO existe"));
            //validar estock
            if (producto.getStock() < item.cantidad()){
                throw new InsufficientStockException("Stock insufiente: "+producto.getNombre());
            }
            //actualizar el stock en bd
            producto.setStock(producto.getStock() -item.cantidad());
            productoRepository.save(producto);
            //creamos el detalle
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            BigDecimal subTotal= producto.getPrecio().multiply(new BigDecimal((item.cantidad())));
            detalle.setSubtotal(subTotal);

            detalles.add(detalle);
            totalPedido = totalPedido.add(subTotal);

        }
        //Guardar Pedido y detalles
         pedido.setTotal(totalPedido);
        pedido.setDetalle(detalles);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        //convertimos los detalles guardados a DTOs para la respuesta
        List<DetalleResponseDTO> productosResponse = pedidoGuardado.getDetalle().stream()
                .map(d -> new DetalleResponseDTO(
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getSubtotal()
                )).toList();
        //Devolver la respuesta completo
        return new PedidoResponseDTO(
                pedidoGuardado.getIdPedido(),
                persona.getNombre(),
                pedidoGuardado.getTotal(),
                pedidoGuardado.getEstado(),
                pedidoGuardado.getFechaPedido(),
                productosResponse
        );


    }
}
