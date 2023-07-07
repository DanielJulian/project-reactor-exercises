package io.javabrains.reactiveworkshop;

import org.junit.Test;
import reactor.test.StepVerifier;

public class JUnitExercise {

    @Test
    public void test() {
        StepVerifier.create(ReactiveSources.intNumbersFlux())
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .expectComplete()
                .verify();

    }

}
