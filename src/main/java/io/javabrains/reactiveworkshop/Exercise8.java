package io.javabrains.reactiveworkshop;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.io.IOException;

public class Exercise8 {


    // An error is -by default- a terminal event, meaning that the flux where the error occurs will stop asking for more events.
    // So, if we have FluxA emitting to FluxB(Or a subscriber), and an error occurs on FluxB, fluxA will continue to emit, but FluxB wont listen anymore.
    // When an error occurs, the onError method of the subscriber is called.

    // I said by default because that can be prevented as we will see in the second point.

    public static void main(String[] args) throws IOException {

        // Use ReactiveSources.intNumbersFluxWithException()

        // Print values from intNumbersFluxWithException and print a message when error happens
        //  ReactiveSources.intNumbersFluxWithException()
        //          .subscribe(System.out::println, err -> System.out.println("An error occurred: " + err));

        // Another way is to use the DoOnError flux:
        ReactiveSources.intNumbersFluxWithException()
                .doOnError(err -> System.out.println("Error! " + err)) // This flux takes an action when an error occurs, but it doesn't swallow the error.
                // Meaning, an error occurs -> doOnError takes some action, but then,
                //  it will pass the error along to the next flux
                .subscribe(System.out::println);

        // Print values from intNumbersFluxWithException and continue on errors
        ReactiveSources.intNumbersFluxWithException()
                .onErrorContinue((err, item) -> System.out.println("Error! " + err)) // On error continue does swallow the error (It wont emit it to the next flux)
                // And it won't stop emitting future events.
                .subscribe(System.out::println);

        // Print values from intNumbersFluxWithException and when errors
        // happen, replace with a fallback sequence of -1 and -2
        ReactiveSources.intNumbersFluxWithException().
                onErrorResume(err -> Flux.just(-1, -2)).
                subscribe(item -> System.out.println(item));


        // There is also a doFinally, that will always execute regardless if there was an error or not. Just like the finally after the try catch block.
        ReactiveSources.intNumbersFluxWithException().
                doFinally(signalType -> {
                    if (signalType == SignalType.ON_COMPLETE) {
                        System.out.println("All good!");
                    } else if (signalType == SignalType.ON_ERROR) {
                        System.out.println("Wooops!!");
                    }
                }).
                subscribe(item -> System.out.println(item));

        System.out.println("Press a key to end");
        System.in.read();
    }

}
