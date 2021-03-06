package com.fpush.common.api.common;

import java.util.concurrent.Executor;

public interface Monitor {

    void monitor(String name, Thread thread);

    void monitor(String name, Executor executor);
}
