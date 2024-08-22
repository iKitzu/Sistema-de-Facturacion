package logica;

public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private int estrato;
    private double totalGastado;

    public Cliente(int id, String nombre, String apellido, int estrato, double totalGastado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estrato = estrato;
        this.totalGastado = totalGastado;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEstrato() {
        return estrato;
    }

    public void setEstrato(int estrato) {
        this.estrato = estrato;
    }

    public double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(double totalGastado) {
        this.totalGastado = totalGastado;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Nombre: %s %s, Estrato: %d, Total Gastado: %.2f",
                id, nombre, apellido, estrato, totalGastado);
    }
}
