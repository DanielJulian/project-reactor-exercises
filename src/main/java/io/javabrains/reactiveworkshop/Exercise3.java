package io.javabrains.reactiveworkshop;

import java.io.IOException;
import java.util.List;

public class Exercise3 {

    public static void main(String[] args) throws IOException {

        // Use ReactiveSources.intNumbersFlux()

        // Get all numbers in the ReactiveSources.intNumbersFlux stream
        // into a List and print the list and its size. In exercise 2 we grabbed one item at a time. Now we want all the events that the flux(publisher) has.
        List<Integer> numbers = ReactiveSources.intNumbersFlux().toStream().toList();
        System.out.println("List is " + numbers);
        System.out.println("Size: " + numbers.size());

        // We dont need the following two lines to keep the thread running because .toStream() is a blocking operation.
        // It will block until the Flux has finished emitting all of its events.
        //System.out.println("Press a key to end");
        //System.in.read();
    }

}
