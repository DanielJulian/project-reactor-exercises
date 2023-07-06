package io.javabrains.reactiveworkshop;

import reactor.core.publisher.Flux;

import java.io.IOException;

public class Exercise2 {

    public static void main(String[] args) throws IOException {

        // Use ReactiveSources.intNumbersFlux() and ReactiveSources.userFlux()

        // Print all numbers in the ReactiveSources.intNumbersFlux stream
        ReactiveSources.intNumbersFlux()
                .subscribe(e -> System.out.println(e)); // With subscribe I say: Whenever the above (intNumbersFlux()) emits an event, do something.

        // Print all users in the ReactiveSources.userFlux stream
        ReactiveSources.userFlux().subscribe(user -> System.out.println(user));

        // The flux is a publisher of events, and then we have 1 or many subscribers.
        Flux<User> userFlux = ReactiveSources.userFlux();
        userFlux.subscribe(user -> System.out.println(user));
        userFlux.subscribe(user -> System.out.println("Another one" + user));

        System.out.println("Press a key to end");
        System.in.read();
    }

}
