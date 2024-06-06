package br.com.acme.lojaacme.modelo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.nio.file.Path;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class Produto {
    private String nome;
    private Path path;
    private BigDecimal preco;
}
