package br.com.acme.lojaacme.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
public class Pagamento {
    private List<Produto> produtos;
    private Cliente cliente;
    private LocalDateTime data;

    public Pagamento(List<Produto> produtos, Cliente cliente, LocalDateTime data) {
        this.produtos = Collections.unmodifiableList(produtos);
        this.cliente = cliente;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "produtos=" + produtos +
                ", cliente=" + cliente +
                ", data=" + data +
                '}';
    }
}
