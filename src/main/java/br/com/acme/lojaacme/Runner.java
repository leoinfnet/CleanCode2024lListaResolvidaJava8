package br.com.acme.lojaacme;

import br.com.acme.lojaacme.modelo.Assinatura;
import br.com.acme.lojaacme.modelo.Cliente;
import br.com.acme.lojaacme.modelo.Pagamento;
import br.com.acme.lojaacme.modelo.Produto;
import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;

public class Runner {
    private static Consumer<Map.Entry<YearMonth, BigDecimal>> println;

    public static void main(String[] args) {
        Cliente darthVader = new Cliente("Darth Vader");
        Cliente hanSolo = new Cliente("Han Solo");
        Cliente lukeSkywalker = new Cliente("Luke Skywalker");

        Produto bach = new Produto("Bach Completo",
                Paths.get("/music/bach.mp3"), new BigDecimal(100));
        Produto dance = new Produto("Dance of Death",
                Paths.get("/music/poderosas.mp3"), new BigDecimal(90));
        Produto bandeira = new Produto("Bandeira Brasil",
                Paths.get("/images/brasil.jpg"), new BigDecimal(50));
        Produto beauty = new Produto("Beleza Americana",
                Paths.get("beauty.mov"), new BigDecimal(150));
        Produto vingadores = new Produto("Os Vingadores",
                Paths.get("/movies/vingadores.mov"), new BigDecimal(200));
        Produto amelie = new Produto("Amelie Poulain",
                Paths.get("/movies/amelie.mov"), new BigDecimal(100));

        LocalDateTime hoje = LocalDateTime.now();
        LocalDateTime ontem = hoje.minusDays(1);
        LocalDateTime mesPassado = hoje.minusMonths(1);

        Pagamento pagamento1 = new Pagamento(asList(bach, dance), darthVader, hoje);
        Pagamento pagamento2 = new Pagamento(asList(beauty, vingadores, bach), lukeSkywalker, hoje);
        Pagamento pagamento3 = new Pagamento(asList(bach, bandeira, amelie), hanSolo, ontem);
        Pagamento pagamento4 = new Pagamento(asList(vingadores), darthVader, mesPassado);
        List<Pagamento> pagamentos = asList(pagamento1,pagamento2,pagamento3,pagamento4);

        //1- Ordenando pela data de Compra
        linha();
        pagamentos.stream().
                sorted(comparing(Pagamento::getData).reversed())
                .sorted(comparing(Pagamento::getData).reversed())
                //sorted(comparing(Pagamento::getData))
                .forEach(System.out::println);
        linha();

         pagamento1.getProdutos().stream().map(Produto::getPreco)
                .reduce(BigDecimal::add)
                 .ifPresent(System.out::println);

        BigDecimal soma = pagamento1.getProdutos().stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Somando todos os pagamentos
        BigDecimal somaDeTodos = pagamentos.stream().map(p -> p.getProdutos().stream()
                        .map(Produto::getPreco)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal reduce = pagamentos.stream()
                .flatMap(p -> p.getProdutos().stream().map(Produto::getPreco))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//Stream<Stream<BigDecimal>> = Stream<BigDecimal>
        Stream<BigDecimal> bigDecimalStream = pagamentos.stream().map(p -> p.getProdutos().stream()
                .map(Produto::getPreco).reduce(BigDecimal.ZERO, BigDecimal::add));

        linha();
        //Quantidade de Cada Produto
        //Map<Produto,Long>
        Map<Produto, Long> collect = pagamentos.stream()
                .flatMap(p -> p.getProdutos().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        collect.entrySet().forEach(System.out::println);

        linha();
        collect.entrySet().stream().max(Map.Entry.comparingByValue())
                .ifPresent(System.out::println);


        //Criando Map de Cliente e Produtos
        //Map<Cliente,List<Produto>>

        Map<Cliente, List<List<Produto>>> clienteParaListaDeProdutos = pagamentos.stream()
                .collect(
                        Collectors
                                .groupingBy(Pagamento::getCliente, Collectors
                                        .mapping(Pagamento::getProdutos, Collectors.toList())));
//Stream<Stream<List<Produto>>> => toList List<List<Produto>>

        Map<String, List<Produto>> collect1 = clienteParaListaDeProdutos.entrySet().stream()
                .collect(
                        Collectors.toMap(p -> p.getKey().nome(), e -> e.getValue().stream().flatMap(List::stream).toList()));
        Map<Cliente, List<Produto>> collect2 = clienteParaListaDeProdutos.entrySet().stream()
                .collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().flatMap(List::stream).toList()));

        linha();
        //Melhor Cliente
        Function<Pagamento,BigDecimal> pagamentoTotal = p-> p.getProdutos().stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        //Map Cliente Valor
        Map<Cliente, BigDecimal> melhoresClientes = pagamentos.stream()
                .collect(Collectors.groupingBy(Pagamento::getCliente,
                        Collectors.reducing(BigDecimal.ZERO, pagamentoTotal, BigDecimal::add)));

        melhoresClientes.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);

        linha();
        //Quanto foi faturado em um mes
        Map<YearMonth, List<Pagamento>> pagamentosPorMes =
                pagamentos.stream()
                        .collect(Collectors.groupingBy(p -> YearMonth.from(p.getData())));
        pagamentosPorMes.entrySet().stream().forEach(System.out::println);

        linha();
        Function<Pagamento, BigDecimal> function = produto -> produto.getProdutos().
                stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        Map<YearMonth, BigDecimal> pagamentosPOrMes = pagamentos.stream().collect(
                Collectors.groupingBy(pagamento -> YearMonth.from(pagamento.getData()),
                        Collectors.reducing(BigDecimal.ZERO,
                                function
                                , BigDecimal::add)));

//        println = System.out::println;
         println = System.out::println;
        pagamentosPOrMes.entrySet().stream().forEach(println);


        linha();
        BigDecimal mensal = new BigDecimal("99.99");
        Assinatura assinatura1 = new Assinatura(mensal, hoje.minusMonths(5), hoje.plusMonths(3), darthVader);
        Assinatura assinatura2 = new Assinatura(mensal, hoje.minusMonths(5), darthVader);
        List<Assinatura> assinaturas = List.of(assinatura1, assinatura2);

        long tempoAssinatura1 = ChronoUnit.MONTHS.between(assinatura1.getInicio(), hoje);
        System.out.println(tempoAssinatura1);
        linha();

        if(assinatura2.getFim() != null){
            long tempoAssinatura2 = ChronoUnit.MONTHS.between(assinatura2.getInicio(), hoje);
            System.out.println(tempoAssinatura2);
        }
        long between = ChronoUnit.MONTHS
                .between(assinatura2.getInicio(), assinatura2.getFim().orElse(LocalDateTime.now()));

        BigDecimal valorTotalDeTodasAssinaturas =
                assinaturas.stream().map(Assinatura::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

    }


    private static void linha() {
        System.out.println("===================\n\n");
    }
}
