## Reactive programming with Project Reactor

### What is Reactive programming?

Reactive Programming is a declarative programming paradigm that is based on the idea of asynchronous event processing
and data streams.

Reactive programming allows the creation of reactive systems which make use of asynchronous communication & processing.
These systems define a set of rules for asynchronous stream processing with non-blocking back pressure.

### Topics explored

- Blocking(Streams) vs Reactive(Mono and Flux)
- Error handling
- Operators to create and chain Fluxes and Monos

### Fundamental Design Pattern in reactive programming

Although reactive programming is not object oriented programming, there is one design pattern from OOP
that explains the essentials of Reactive Programming: The Observer pattern.

The Observer pattern is a design pattern where the subject maintains a list of its dependents, called observers,
and notifies them automatically of any state changes, usually by calling one of their methods.

### Fundamental Concepts

#### Reactive Objects

- Stream: A series of items all available at once. Its native of Java. (This is not reactive)
- Flux: A series of items over time.
- Mono: Represents an asynchronous item, exactly one item, that may or may not be published in the future.

A Flux is 0 or n items. Mono is 0 or 1 items.
We don't have this distinction in streams.

#### Backpressure

A Flux has backpressure control: When you are publishing something, and someone is subscribed and doing some processing,
but the publishing is happening too fast for the subscriber to keep up, the Flux paradigm
has a mechanism where the subscriber can tell the publisher to slow down.

#### Communication Objects

What things can a Flux or Mono publish?

- An item (Any object)
- A complete event, where the flux/mono tell the subscribers that no more items will be published.
- A failure event.

Full course: https://www.youtube.com/watch?v=EExlnnq5Grs&list=PLqq-6Pq4lTTYPR2oH7kgElMYZhJd4vOGI&index=2&ab_channel=JavaBrains
