package logica;

import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int id;
    private int idCliente;
    private String fecha;
    private double total;
    private List<DetalleFactura> detalles;

    public Factura(int id, int idCliente, String fecha) {
        this.id = id;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.detalles = new ArrayList<>();
        this.total = 0.0;
    }

    public void agregarDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
        total += detalle.getSubtotal();
    }

    public double calcularTotalConDescuento(double descuento) {
        return total * (1 - descuento / 100);
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
}
