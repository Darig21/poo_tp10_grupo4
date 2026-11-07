package ar.edu.unju.fi.poo.tp10.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String dniCuit;

    @Column(nullable = false)
    private String nombreRazonSocial;

    private String telefono;

    @Column(unique = true, nullable = false)
    private String email;

    private String direccion;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    public Cliente() {}

    public Cliente(String dniCuit, String nombreRazonSocial, String telefono, String email, String direccion, TipoCliente tipoCliente) {
        this.dniCuit = dniCuit;
        this.nombreRazonSocial = nombreRazonSocial;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.tipoCliente = tipoCliente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDniCuit() { return dniCuit; }
    public void setDniCuit(String dniCuit) { this.dniCuit = dniCuit; }
    public String getNombreRazonSocial() { return nombreRazonSocial; }
    public void setNombreRazonSocial(String nombreRazonSocial) { this.nombreRazonSocial = nombreRazonSocial; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }
}
