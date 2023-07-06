package io.javabrains.reactiveworkshop;

import java.io.IOException;

public class Exercise9 {

    // This exercise explores how to convert a Flux to a Mono
    public static void main(String[] args) throws IOException {

        // Use ReactiveSources.intNumbersFlux()

        // Print size of intNumbersFlux after the last item returns
        ReactiveSources.intNumbersFlux()
                .count() // it returns a Mono<Long> (Non Blocking) when the flux emits a terminal event (completion event)
                .subscribe(System.out::println);

        // Collect all items of intNumbersFlux into a single list and print it
        ReactiveSources.intNumbersFlux().collectList().subscribe(System.out::println);

        // Transform to a sequence of sums of adjacent two numbers
        ReactiveSources.intNumbersFlux().
                buffer(2). // buffer(2) basically means: every 2 events emmitted from intNumbersFlux, 1 event will emitted from the buffer.
                // The event emmitted by buffer is a list of the buffered events.
                        map(list -> list.get(0) + list.get(1)).
                subscribe(System.out::println);

        System.out.println("Press a key to end");
        System.in.read();
    }

}
