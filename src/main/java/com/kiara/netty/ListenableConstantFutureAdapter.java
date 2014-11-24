/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package com.kiara.netty;

import com.google.common.util.concurrent.ListenableFuture;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 * @param <V>
 */
public class ListenableConstantFutureAdapter<V> implements ListenableFuture<V> {

    private final V value;
    private final ChannelFuture future;

    public ListenableConstantFutureAdapter(ChannelFuture future, V value) {
        this.future = future;
        this.value = value;
    }

    @Override
    public void addListener(final Runnable r, final Executor exctr) {
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                exctr.execute(r);
            }
        });
    }

    @Override
    public boolean cancel(boolean bln) {
        return future.cancel(bln);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        future.get();
        return value;
    }

    @Override
    public V get(long l, TimeUnit tu) throws InterruptedException, ExecutionException, TimeoutException {
        future.get(l, tu);
        return value;
    }
}
