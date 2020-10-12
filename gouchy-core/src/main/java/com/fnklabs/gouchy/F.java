package com.fnklabs.gouchy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface F<R, Arg> {
    CompletableFuture<R> call(Arg arg);
}
