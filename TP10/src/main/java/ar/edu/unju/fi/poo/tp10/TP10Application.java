package ar.edu.unju.fi.poo.tp10;

import ar.edu.unju.fi.poo.tp10.model.Cliente;
import ar.edu.unju.fi.poo.tp10.model.CuponDescuento;
import ar.edu.unju.fi.poo.tp10.model.ModalidadPago;
import ar.edu.unju.fi.poo.tp10.model.Producto;
import ar.edu.unju.fi.poo.tp10.model.TipoCliente;
import ar.edu.unju.fi.poo.tp10.model.Venta;
import ar.edu.unju.fi.poo.tp10.repository.CuponRepository;
import ar.edu.unju.fi.poo.tp10.service.ClienteService;
import ar.edu.unju.fi.poo.tp10.service.ProductoService;
import ar.edu.unju.fi.poo.tp10.service.VentaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class TP10Application {

    private static ClienteService clienteService;
    private static ProductoService productoService;
    private static VentaService ventaService;
    private static CuponRepository cuponRepository;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TP10Application.class, args);

        clienteService = context.getBean(ClienteService.class);
        productoService = context.getBean(ProductoService.class);
        ventaService = context.getBean(VentaService.class);
        cuponRepository = context.getBean(CuponRepository.class);

        ejecutarMenu(context);
    }

    private static void ejecutarMenu(ApplicationContext context) {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();
            try {
                switch (opcion) {
                    case "1" -> registrarCliente();
                    case "2" -> buscarClientePorDni();
                    case "3" -> buscarClientesPorNombre();
                    case "4" -> registrarProducto();
                    case "5" -> listarProductos();
                    case "6" -> filtrarProductos();
                    case "7" -> consultarStockCritico();
                    case "8" -> crearCupon();
                    case "9" -> registrarVentaContado();
                    case "10" -> registrarVentaFinanciada();
                    case "11" -> historialCliente();
                    case "12" -> ventasPorPeriodo();
                    case "13" -> ventasPorModalidad();
                    case "0" -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: número inválido.");
            } catch (DateTimeParseException e) {
                System.out.println("Error: fecha inválida (usá formato AAAA-MM-DD).");
            } catch (Exception e) {
                System.out.println("Error [" + e.getClass().getSimpleName() + "]: " + e.getMessage());
            }
        }

        sc.close();
        System.out.println("Cerrando aplicación...");
        System.exit(SpringApplication.exit(context));
    }

    // ==================== Menú ====================

    private static void mostrarMenu() {
        System.out.println("\n========== MENÚ PRINCIPAL ==========");
        System.out.println("--- Clientes ---");
        System.out.println(" 1. Registrar cliente");
        System.out.println(" 2. Buscar cliente por DNI/CUIT");
        System.out.println(" 3. Buscar clientes por nombre");
        System.out.println("--- Productos ---");
        System.out.println(" 4. Registrar producto");
        System.out.println(" 5. Listar todos los productos");
        System.out.println(" 6. Filtrar productos (categoría, marca, rango de precio)");
        System.out.println(" 7. Consultar stock crítico");
        System.out.println("--- Cupones ---");
        System.out.println(" 8. Crear cupón de descuento");
        System.out.println("--- Ventas ---");
        System.out.println(" 9. Registrar venta de contado");
        System.out.println("10. Registrar venta financiada (plan 6 cuotas)");
        System.out.println("11. Historial de ventas de un cliente");
        System.out.println("12. Ventas por período");
        System.out.println("13. Ventas por modalidad de pago");
        System.out.println(" 0. Salir");
        System.out.print("Opción: ");
    }

    // ==================== Clientes ====================

    private static void registrarCliente() {
        String dni = leerDniCuit();

        System.out.println("Tipo: 1. PARTICULAR   2. EMPRESA");
        TipoCliente tipo = leerEntero("Tipo", 1, 2) == 2 ? TipoCliente.EMPRESA : TipoCliente.PARTICULAR;

        String nombre = leerNombre(tipo);
        String telefono = leerTelefono();
        String email = leerEmail();
        String direccion = leerTextoNoVacio("Dirección");

        Cliente c = clienteService.guardarCliente(
                new Cliente(dni, nombre, telefono, email, direccion, tipo));
        System.out.println("Cliente guardado:");
        imprimirCliente(c);
    }

    private static void buscarClientePorDni() {
        imprimirCliente(clienteService.buscarPorDniCuit(leerDniCuit()));
    }

    private static void buscarClientesPorNombre() {
        List<Cliente> lista = clienteService.buscarPorNombre(leerTexto("Nombre (parcial)"));
        if (lista.isEmpty()) {
            System.out.println("Sin resultados.");
            return;
        }
        lista.forEach(TP10Application::imprimirCliente);
    }

    // ==================== Productos ====================

    private static void registrarProducto() {
        String nombre = leerTextoNoVacio("Nombre");
        String descripcion = leerTextoNoVacio("Descripción");
        String marca = leerTextoNoVacio("Marca");
        String categoria = leerTextoNoVacio("Categoría");
        Integer stock = leerEntero("Stock", 0, 1_000_000);
        Double precio = leerDouble("Precio base", 0.01, 100_000_000);

        Producto p = productoService.guardarProducto(
                new Producto(nombre, descripcion, marca, categoria, stock, precio));
        System.out.println("Producto guardado:");
        imprimirProducto(p);
    }

    private static void listarProductos() {
        // No hay findAll en el service: stock <= Integer.MAX_VALUE trae todos
        List<Producto> lista = productoService.consultarStockCritico(Integer.MAX_VALUE);
        if (lista.isEmpty()) {
            System.out.println("No hay productos.");
            return;
        }
        lista.forEach(TP10Application::imprimirProducto);
    }

    private static void filtrarProductos() {
        System.out.println("(El filtro exige TODOS los campos, tal como está tu repositorio)");
        String categoria = leerTextoNoVacio("Categoría");
        String marca = leerTextoNoVacio("Marca");
        Double min = leerDouble("Precio mínimo", 0, 100_000_000);
        Double max = leerDouble("Precio máximo", min, 100_000_000);

        List<Producto> lista = productoService.filtrarProductos(categoria, marca, min, max);
        if (lista.isEmpty()) {
            System.out.println("Sin resultados.");
            return;
        }
        lista.forEach(TP10Application::imprimirProducto);
    }

    private static void consultarStockCritico() {
        List<Producto> lista = productoService.consultarStockCritico(leerEntero("Umbral de stock", 0, 1_000_000));
        if (lista.isEmpty()) {
            System.out.println("Sin productos en stock crítico.");
            return;
        }
        lista.forEach(TP10Application::imprimirProducto);
    }

    // ==================== Cupones ====================

    private static void crearCupon() {
        String codigo = leerConPatron("Código (letras y números)", "^[A-Za-z0-9_-]{3,20}$",
                "Entre 3 y 20 caracteres: letras, números, guion o guion bajo.");
        Double porcentaje = leerDouble("Porcentaje de descuento (1-100)", 1, 100);
        LocalDate vencimiento = leerFecha("Fecha de vencimiento");

        CuponDescuento c = cuponRepository.save(
                new CuponDescuento(codigo, porcentaje, vencimiento, true));
        System.out.println("Cupón creado: " + c.getCodigo() + " (" + c.getPorcentajeDescuento()
                + "%, vence " + c.getFechaVencimiento() + ")");
    }

    // ==================== Ventas ====================

    private static void registrarVentaContado() {
        Long clienteId = clienteService.buscarPorDniCuit(leerDniCuit()).getId();
        Map<Long, Integer> items = leerCarrito();
        System.out.print("Código de cupón (Enter para ninguno): ");
        String cupon = sc.nextLine().trim();

        Venta v = ventaService.registrarVentaContado(clienteId, items, cupon.isEmpty() ? null : cupon);
        System.out.println("Venta registrada:");
        imprimirVenta(v, true);
    }

    private static void registrarVentaFinanciada() {
        Long clienteId = clienteService.buscarPorDniCuit(leerDniCuit()).getId();
        Map<Long, Integer> items = leerCarrito();
        Double recargo = leerDouble("Porcentaje de recargo (0-100)", 0, 100);

        Venta v = ventaService.registrarVentaFinanciadaPlan6(clienteId, items, recargo);
        System.out.println("Venta financiada registrada:");
        imprimirVenta(v, true);
    }

    private static void historialCliente() {
        Long clienteId = clienteService.buscarPorDniCuit(leerDniCuit()).getId();
        List<Venta> lista = ventaService.consultarHistorialCliente(clienteId);
        if (lista.isEmpty()) {
            System.out.println("El cliente no tiene ventas.");
            return;
        }
        lista.forEach(v -> imprimirVenta(v, false));
    }

    private static void ventasPorPeriodo() {
        LocalDate desde = leerFecha("Desde");
        LocalDate hasta;
        while (true) {
            hasta = leerFecha("Hasta");
            if (!hasta.isBefore(desde)) break;
            System.out.println("  ✗ La fecha 'Hasta' no puede ser anterior a 'Desde'.");
        }

        List<Venta> lista = ventaService.consultarVentasPorPeriodo(
                desde.atStartOfDay(), LocalDateTime.of(hasta, LocalTime.MAX));
        if (lista.isEmpty()) {
            System.out.println("Sin ventas en ese período.");
            return;
        }
        lista.forEach(v -> imprimirVenta(v, false));
    }

    private static void ventasPorModalidad() {
        System.out.println("1. CONTADO   2. FINANCIADA_PLAN_6");
        ModalidadPago modalidad = leerEntero("Modalidad", 1, 2) == 1
                ? ModalidadPago.CONTADO
                : ModalidadPago.FINANCIADA_PLAN_6;

        List<Venta> lista = ventaService.consultarVentasPorModalidad(modalidad);
        if (lista.isEmpty()) {
            System.out.println("Sin ventas con esa modalidad.");
            return;
        }
        lista.forEach(v -> imprimirVenta(v, false));
    }

    private static Map<Long, Integer> leerCarrito() {
        Map<Long, Integer> items = new LinkedHashMap<>();
        System.out.println("Cargá productos (ID 0 para terminar):");
        while (true) {
            long id = leerLong("ID de producto", 0);
            if (id == 0) break;
            int cantidad = leerEntero("Cantidad", 1, 100_000);
            items.merge(id, cantidad, Integer::sum);
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío.");
        }
        return items;
    }

    // ==================== Lectura de datos ====================

    private static String leerTexto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return sc.nextLine().trim();
    }

    private static String leerTextoNoVacio(String etiqueta) {
        while (true) {
            String v = leerTexto(etiqueta);
            if (!v.isEmpty()) return v;
            System.out.println("  ✗ No puede estar vacío.");
        }
    }

    private static String leerConPatron(String etiqueta, String regex, String mensajeError) {
        while (true) {
            String v = leerTexto(etiqueta);
            if (v.matches(regex)) return v;
            System.out.println("  ✗ " + mensajeError);
        }
    }

    /** DNI (7-8 dígitos) o CUIT/CUIL (11 dígitos). Devuelve solo los números. */
    private static String leerDniCuit() {
        while (true) {
            String v = leerTexto("DNI/CUIT (solo números)").replace("-", "").replace(".", "");
            if (v.matches("\\d{7,8}") || v.matches("\\d{11}")) return v;
            System.out.println("  ✗ Debe tener 7-8 dígitos (DNI) u 11 dígitos (CUIT).");
        }
    }

    private static String leerNombre(TipoCliente tipo) {
        if (tipo == TipoCliente.PARTICULAR) {
            return leerConPatron("Nombre y apellido",
                    "^[\\p{L}][\\p{L} .'-]*$",
                    "Solo letras y espacios (sin números).");
        }
        // Las razones sociales pueden llevar números, pero al menos una letra
        return leerConPatron("Razón social",
                "^(?=.*\\p{L}).+$",
                "Debe contener al menos una letra.");
    }

    private static String leerEmail() {
        return leerConPatron("Email",
                "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$",
                "Email inválido (ej: nombre@dominio.com).");
    }

    private static String leerTelefono() {
        return leerConPatron("Teléfono",
                "^\\+?[0-9 ()-]{6,20}$",
                "Solo números, espacios, guiones o paréntesis (6 a 20 caracteres).");
    }

    private static int leerEntero(String etiqueta, int min, int max) {
        while (true) {
            try {
                int n = Integer.parseInt(leerTexto(etiqueta));
                if (n >= min && n <= max) return n;
                System.out.println("  ✗ Debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Ingresá un número entero válido.");
            }
        }
    }

    private static long leerLong(String etiqueta, long min) {
        while (true) {
            try {
                long n = Long.parseLong(leerTexto(etiqueta));
                if (n >= min) return n;
                System.out.println("  ✗ Debe ser mayor o igual a " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Ingresá un número entero válido.");
            }
        }
    }

    private static double leerDouble(String etiqueta, double min, double max) {
        while (true) {
            try {
                double n = Double.parseDouble(leerTexto(etiqueta).replace(',', '.'));
                if (n >= min && n <= max) return n;
                System.out.println("  ✗ Debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Ingresá un número válido (ej: 1500.50).");
            }
        }
    }

    private static LocalDate leerFecha(String etiqueta) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(etiqueta + " (AAAA-MM-DD)"));
            } catch (DateTimeParseException e) {
                System.out.println("  ✗ Fecha inválida. Ejemplo: 2026-12-31");
            }
        }
    }

    // ==================== Impresión ====================

    private static void imprimirCliente(Cliente c) {
        System.out.printf("[%d] %s | %s | %s | %s | %s%n",
                c.getId(), c.getNombreRazonSocial(), c.getDniCuit(),
                c.getEmail(), c.getTelefono(), c.getTipoCliente());
    }

    private static void imprimirProducto(Producto p) {
        System.out.printf("[%d] %s | %s | %s | stock: %d | $%.2f%n",
                p.getId(), p.getNombre(), p.getMarca(), p.getCategoria(),
                p.getStock(), p.getPrecioBase());
    }

    private static void imprimirVenta(Venta v, boolean conDetalle) {
        System.out.printf("Venta #%d | %s | %s | %s | %s | subtotal: $%.2f | total: $%.2f%n",
                v.getId(), v.getCodigoComprobante(), v.getFecha(),
                v.getCliente().getNombreRazonSocial(), v.getModalidadPago(),
                v.getSubtotal(), v.getTotal());

        // detalles y cuotas son LAZY: solo se pueden leer fuera de transacción
        // en la venta recién creada (ya está cargada en memoria)
        if (conDetalle) {
            v.getDetalles().forEach(d -> System.out.printf("   - %s x%d @ $%.2f = $%.2f%n",
                    d.getProducto().getNombre(), d.getCantidad(),
                    d.getPrecioBaseAplicado(), d.getSubtotal()));
            v.getCuotas().forEach(c -> System.out.printf("   Cuota %d: $%.2f vence %s [%s]%n",
                    c.getNumeroCuota(), c.getImporte(), c.getFechaVencimiento(), c.getEstado()));
        }
    }
}
