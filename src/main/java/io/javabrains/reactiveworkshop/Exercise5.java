package io.javabrains.reactiveworkshop;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

import java.io.IOException;

public class Exercise5 {

    public static void main(String[] args) throws IOException {

        // Use ReactiveSources.intNumberFlux() and ReactiveSources.userMono()

        // Subscribe to a flux using the error and completion hooks
        ReactiveSources.intNumbersFlux().subscribe(
                number -> System.out.println(number),
                err -> System.out.println("We got an error! " + err.getMessage()),
                () -> System.out.println("Flux completed")
        );

        // Subscribe to a flux using an implementation of BaseSubscriber
        // So, instead of passing lambdas to the subscribe method,
        // we can pass a class that knows how to handle the events (And more features)
        ReactiveSources.intNumbersFlux().subscribe(new MySubscriber<>());

        System.out.println("Press a key to end");
        System.in.read();
    }


}


// This way of providing the subscriber to the flux is used when we need to control the rate of flow.
// You will see how in the comments below.
class MySubscriber<T> extends BaseSubscriber<T> {

    public void hookOnSubscribe(Subscription subscription) {
        System.out.println("Subscribe happened");

        // When we create the subscriber in this way, we have to specify
        // how many items this subscriber is willing to handle (for backpressure purposes)
        // So, right after the subscription happens, we signal the publisher: Ok, im ready to handle X items, start sending as soon as you have something.
        // Here, saying request(1) we are saying we are ready to handle 1 item.
        // What will happen is, once the publisher(Flux or mono) has 1 item to publish, the hookOnNext method will be called.
        request(1);
    }

    public void hookOnNext(T value) {
        System.out.println(value.toString() + " received");

        // Lets say our hookOnNext has all the business logic and finishes processing.
        // Now we have to signal the publisher that we are ready to process 1 more item. so once another item is available, hookOnNext will be called
        request(1);
    }

}