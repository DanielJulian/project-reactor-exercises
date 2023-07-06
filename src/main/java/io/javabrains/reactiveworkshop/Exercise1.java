package io.javabrains.reactiveworkshop;

public class Exercise1 {

    public static void main(String[] args) {

        // Use StreamSources.intNumbersStream() and StreamSources.userStream()

        // Print all numbers in the intNumbersStream stream
        StreamSources.intNumbersStream().forEach(System.out::print);

        System.out.println();

        // Print numbers from intNumbersStream that are less than 5
        StreamSources.intNumbersStream().filter(x -> x < 5).forEach(System.out::print);

        System.out.println();

        // Print the second and third numbers in intNumbersStream that's greater than 5
        StreamSources.intNumbersStream().filter(x -> x > 5).skip(1).limit(2).forEach(System.out::print);

        System.out.println();

        //  Print the first number in intNumbersStream that's greater than 5.
        //  If nothing is found, print -1
        StreamSources.intNumbersStream().filter(x -> x > 5).findFirst().ifPresentOrElse(System.out::print, () -> System.out.print(-1));

        System.out.println();

        // Print first names of all users in userStream
        StreamSources.userStream().forEach(u -> System.out.println(u.getFirstName()));
        StreamSources.userStream().map(user -> user.getFirstName()).forEach(System.out::print); // Another way. The map replaces the value in the stream

        System.out.println();

        // Print first names in userStream for users that have IDs from number stream
        StreamSources.intNumbersStream()
                // When the right side gives the actual value, just use Map.
                // In this case, if we use map, we wont get a value, we will get another stream.
                // In order to get a value out of the stream, we use flatmap
                .flatMap(id -> StreamSources.userStream().filter(user -> user.getId() == id))
                .map(user -> user.getFirstName())
                .forEach(System.out::print);

        // Another option is the following
        StreamSources.userStream().filter(u -> StreamSources.intNumbersStream().anyMatch(i -> i == u.getId()))
                .forEach(System.out::println);
    }

}











