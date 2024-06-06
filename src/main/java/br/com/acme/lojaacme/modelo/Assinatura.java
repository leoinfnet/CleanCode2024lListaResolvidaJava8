package br.com.acme.lojaacme.modelo;

import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Data
public class Assinatura {
    private BigDecimal mensalidade;
    private LocalDateTime inicio;
    private Optional<LocalDateTime> fim;
    private Cliente cliente;

    public Assinatura(BigDecimal mensalidade, LocalDateTime inicio, LocalDateTime fim, Cliente cliente) {
        this.mensalidade = mensalidade;
        this.inicio = inicio;
        this.fim = Optional.of(fim);
        this.cliente = cliente;
    }
    public Assinatura(BigDecimal mensalidade, LocalDateTime inicio, Cliente cliente) {
        this.mensalidade = mensalidade;
        this.inicio = inicio;
        this.fim = Optional.empty();
        this.cliente = cliente;
    }
    public BigDecimal getValorTotal(){
        return getMensalidade().multiply(tempoEntreAssinaturas());
    }
    private BigDecimal tempoEntreAssinaturas() {
        return new BigDecimal(ChronoUnit.MONTHS.between(getInicio(), getFim().orElse(LocalDateTime.now())));
    }
}
