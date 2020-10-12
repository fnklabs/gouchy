# gouchy
Graph Execution 

# Problem 

For processing method calls that aggregate async services we need evaluate and combine several async calls.
The most of services are async, in most of cases combination are sync.

```
Future1 = Service.execute(..)
Object1 = Future1.get()

Future2 = Service.execute(..)
Object2 = Future2.get()

Future3 = Service.execute(Object2)
Object3 = Future3.get()

Future4 = Service.execute(Object1, Object2)
Object4 = Future4.get()
```

Target realization

```
Future1 = Service.execute(..)
Future2 = Service.execute(..)

Future4 = Futures.combine(Future1, Future2)
                 .thenApply( 

Future2 = Future1.thenApply(Object -> Service.execute(Object))
                 .thenCombine(Object2 -> Service.execute(Object2))
                 .thenApply( (Object2, Object3) -> Service.execute(Object2, Object3) ))
                 .get();
                 
```
