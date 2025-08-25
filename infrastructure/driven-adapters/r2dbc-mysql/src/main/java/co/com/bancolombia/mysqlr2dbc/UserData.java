package co.com.bancolombia.mysqlr2dbc;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "usuario")
public class UserData {
    @Id
    private Long id;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private Double salarioBase;
}
