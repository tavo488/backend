package Minimarket.GlobalExpress_SpringBoot.repository;

import Minimarket.GlobalExpress_SpringBoot.model.Venta;
import Minimarket.GlobalExpress_SpringBoot.dto.VentaHistorialDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    /**
     * CONSULTA 1: venta + usuario (JOIN). Trae solo los campos que necesita el historial:
     * de venta (id, fecha, cliente, método de pago, estado, total) y de usuario (nombre = vendedor).
     * SQL equivalente:
     *   SELECT v.id_venta, v.fecha, v.Cliente, v.metodo_pago, v.estado, v.total, u.nombre
     *   FROM venta v JOIN usuario u ON u.id_usuario = v.id_usuario ORDER BY v.id_venta DESC
     */
    @Query("""
            SELECT new Minimarket.GlobalExpress_SpringBoot.dto.VentaHistorialDto(
                v.idVenta, v.fecha, v.cliente, v.metodoPago, v.estado, v.total, u.nombre)
            FROM Venta v JOIN v.usuario u
            ORDER BY v.idVenta DESC
            """)
    List<VentaHistorialDto> consultarHistorial();

    // Misma consulta, filtrada por estado ("Pagado" o "Cancelado")
    @Query("""
            SELECT new Minimarket.GlobalExpress_SpringBoot.dto.VentaHistorialDto(
                v.idVenta, v.fecha, v.cliente, v.metodoPago, v.estado, v.total, u.nombre)
            FROM Venta v JOIN v.usuario u
            WHERE v.estado = :estado
            ORDER BY v.idVenta DESC
            """)
    List<VentaHistorialDto> consultarHistorialPorEstado(@Param("estado") String estado);

    // Historial: las más recientes primero
    List<Venta> findAllByOrderByIdVentaDesc();

    // Historial filtrado por estado ("Pagado" o "Cancelado")
    List<Venta> findByEstadoOrderByIdVentaDesc(String estado);

    // Ventas de un período: fecha >= inicio y fecha < fin
    List<Venta> findByFechaGreaterThanEqualAndFechaLessThanOrderByFechaAsc(
            LocalDateTime inicio, LocalDateTime fin);
}
