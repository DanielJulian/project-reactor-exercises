package io.javabrains.reactiveworkshop;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class OperatorsExercise {

    public static void main(String[] args) throws IOException, InterruptedException {

        mapOperator();
        flatMapOperator();
        concatMapOperator();
        flatMapSequentialOperator();
        flatMapMany();
        filterOperator();
        transformOperator();
        defaultOnEmptyOperator();
        concatAndConcatWithOperator();
        zipOperator();
        doOnOperator();
        errorHandlerOperators();

        System.in.read();
    }


    public static void mapOperator() {
        System.out.println("*** Map operator ***");
        Flux.range(1, 3)
                .map(number -> number + 5)
                .subscribe(System.out::print);

        System.out.println();
    }


    public static void flatMapOperator() throws InterruptedException {
        System.out.println("*** FlatMap operator ***");

        Flux.range(1, 15)
                .flatMap(item -> Flux.just(item).delayElements(Duration.ofMillis(1)))
                .subscribe(x -> System.out.print(x + " "));

        Thread.sleep(1000);
        System.out.println();
    }

    public static void concatMapOperator() throws InterruptedException {
        System.out.println("*** ConcatMap operator ***");

        Flux.range(1, 15)
                .concatMap(item -> Flux.just(item).delayElements(Duration.ofMillis(1)))
                .subscribe(x -> System.out.print(x + " "));

        Thread.sleep(1000);
        System.out.println();
    }

    // ConcatMap waits for the current emitted observable to complete and flatMap doesn't. (Thats why concat map keeps the order)
    // FlatMap execute its task as soon as an event is emitted, and the task execution order is not guaranteed.
    // Just make sure that the observables(flux) you emit to concatMap can complete,
    // otherwise the whole flow will get stuck waiting for the current observable to complete to concatenate the next one.
    // FlatMapSequential Subscribes to all emitted events like flatMap, but preserves the order by queuing elements received out of order.

    // To sum up
    // When we don't care about the subscription order and we also don't care about the publishing order, use FlatMap
    // When we don't care about the subscription order but we do care about the publishing order, use FlatMapSequential
    // When we care about subscription order and we care about the publishing order, use concatMap

    public static void flatMapSequentialOperator() throws InterruptedException {
        System.out.println("*** FlatMapSequential operator ***");

        Flux.range(1, 15)
                .flatMapSequential(item -> Flux.just(item).delayElements(Duration.ofMillis(1)))
                .subscribe(x -> System.out.print(x + " "));

        Thread.sleep(1000);
        System.out.println();
    }

    // When we want to create a Flux from a Mono
    public static void flatMapMany() throws InterruptedException {
        System.out.println("*** FlatMapMany operator ***");

        Mono.just("Danny").flatMapMany(s -> Flux.just(s.split("a"))).subscribe(System.out::println);
        // The equivalent of the above without using flatMapMany would be something like this
        //Mono.just("Danny").flatMap(s -> Mono.just(List.of(s.split("a")))).subscribe(System.out::println);


        Thread.sleep(1000);
        System.out.println();
    }

    public static void filterOperator() {
        System.out.println("*** Filter operator ***");

        Flux.fromIterable(List.of("One", "Two", "Three"))
                .filter(number -> number.startsWith("T"))
                .subscribe(System.out::print);

        System.out.println();
    }

    // Transform from one type to the other using a functional interface
    public static void transformOperator() {
        System.out.println("*** Transform operator ***");

        Function<Flux<String>, Flux<String>> filterData = data -> data.filter(str -> str.length() > 3);

        Flux.fromIterable(List.of("One", "Two", "Three"))
                .transform(filterData)
                // .filter(str -> str.length() > 3) We replaced this line of code with the transform(filterData)
                .subscribe(System.out::print);

        System.out.println();
    }

    // When the upper stream emits the Complete event, but no other event was emitted (no value), we can handle that
    // defaultIfEmpty blocks until the publisher sends the Complete event. After that, it checks if something else was published or not
    public static void defaultOnEmptyOperator() throws InterruptedException {
        System.out.println("*** DefaultOnEmpty operator ***");

        Flux.fromIterable(List.of("One", "Two", "Three"))
                .filter(str -> str.length() > 100)
                .defaultIfEmpty("Nothing came through...")
                .subscribe(System.out::print);

        System.out.println();
        Thread.sleep(500);
    }

    //TODO Check switch if empty

    // Concats waits until the first subscription is complete before moving to the next one, and so on. It consumes the fluxes sequentially.
    // Works for Mono's and Fluxes
    public static void concatAndConcatWithOperator() throws InterruptedException {
        System.out.println("*** Concat operator ***");

        var flux1 = Flux.just("Danny", "Julian");
        var flux2 = Flux.just("Julian", "Danny");

        Flux.concat(flux1, flux2).log().subscribe();

        System.out.println();
        Thread.sleep(500);

        System.out.println("*** ConcatWith operator ***");

        flux1.concatWith(flux2).log().subscribe();

        System.out.println();
        Thread.sleep(500);
    }

    // Merge and mergeWith is the same as concatWith
    // BUT merge subscribes eagerly to all the publishers, so the order depends on which publisher returns first.
    // So, if you care about the order of invocation, use concat, else use merge.
    // There is also MergeSequential, which subscribes to all publishers at the same time, but orders the output.


    // Zip waits for each source to emit one element and combines these elements.
    // The Zip operator will continue combining the outputs of sources until any of the sources completes.
    //
    public static void zipOperator() throws InterruptedException {
        System.out.println("*** ZIP operator ***");

        Flux<String> f1 = Flux.just("f11", "f12");
        Flux<String> f2 = Flux.just("f21", "f22");
        Flux<String> f3 = Flux.just("f31", "f32");

        // zip will emit 2 events combining the elements
        Flux.zip(f1, f2, f3).log().map(x -> {
            String result = x.getT1()+","+x.getT2()+","+x.getT3();
            System.out.println("Zip output "+result);
            return result;
        }).subscribe();


        Thread.sleep(500);

        // We zip fluxes of different lengths
        f3 = Flux.just("f31");
        // zip will only emit 1 event because f3 completes before f1 and f2
        Flux.zip(f1, f2, f3).map(x -> {
            String result = x.getT1()+","+x.getT2()+","+x.getT3();
            System.out.println("Zip output "+result);
            return result;
        }).subscribe();

        Thread.sleep(500);


        // zipWith will emit 2 events combining the elements
        f1.zipWith(f2).log().map(x -> {
            String result = x.getT1()+","+x.getT2();
            System.out.println("ZipWith output "+result);
            return result;
        }).subscribe();

        System.out.println();
        Thread.sleep(500);
    }


    // We can use doOn* callback operators to perform custom actions without modifying the elements in the sequence.
    // The doOn Callbacks allow us to peek into the events emitted by the Publisher (Mono or Flux).
    // So whatever happens inside the doOn* wont affect the main pipeline.
    public static void doOnOperator() throws InterruptedException {
        System.out.println("*** DoOn operator ***");

        Flux.fromIterable(List.of("One", "Two", "Three"))
                .doOnNext(x -> System.out.println("On next " + x))
                .doOnSubscribe(x -> System.out.println("Just subscribed " + x))
                .doOnComplete(() -> System.out.println("Completed!"))
                .subscribe(x -> System.out.println("Subscribe " + x));

        System.out.println();
        Thread.sleep(500);
    }



    public static void errorHandlerOperators() throws InterruptedException {
        System.out.println("*** Error operators ***");

        System.out.println("OnErrorReturn");

        Flux.just("Hey", "Danny")
                .concatWith(Flux.error( new RuntimeException("An error occurred!!")))
                .onErrorReturn("Responding with default value")
                .subscribe(System.out::println);

        System.out.println();
        Thread.sleep(500);

        System.out.println("OnErrorContinue");

        Flux.just("Hey", "Danny")
                .map(e -> {
                    if (e.equals("Danny")) {
                        throw new RuntimeException("Error!!");
                    }
                    return e.toUpperCase();
                })
                .onErrorContinue( (e, f) -> System.out.println("e: " + e + "  f: " + f)) // Drops the event that caused the error and continues processing the pipeline
                .subscribe(System.out::println);

        System.out.println();
        Thread.sleep(500);

        System.out.println("OnErrorMap");

        Flux.just("Hey", "Danny")
                .map(e -> {
                    if (e.equals("Danny")) {
                        throw new RuntimeException("Error!!");
                    }
                    return e.toUpperCase();
                })
                .onErrorMap(throwable -> { // Map an exception to another exception.
                    System.out.println("Error: " + throwable);
                    return new IllegalStateException("throwing new exception");
                })
                .subscribe(System.out::println);

        System.out.println();
        Thread.sleep(500);


        System.out.println("doOnError");

        Flux.just("Hey", "Danny")
                .map(e -> {
                    if (e.equals("Danny")) {
                        throw new RuntimeException("Error!!");
                    }
                    return e.toUpperCase();
                })
                .doOnError(throwable -> { // doOnError will be executed when an error is thrown and hasn't been caught. If the error has already been caught beforehand, it will not be executed.
                    System.out.println("Error: " + throwable);
                })
                .subscribe(System.out::println);

        System.out.println();
        Thread.sleep(500);
    }

}
