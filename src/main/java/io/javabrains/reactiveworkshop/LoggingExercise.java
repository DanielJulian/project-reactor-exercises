package io.javabrains.reactiveworkshop;

import java.io.IOException;

public class LoggingExercise {


    public static void main(String[] args) throws IOException {
        ReactiveSources.
                intNumbersFlux()
                // .log()
                .filter(number -> number > 5)
                .log() // Its not the same if I add the log here or in line 11.
                .subscribe(System.out::println);


        System.out.println("Press a key to end");
        System.in.read();
    }

}
