package com.fpush.common.router;

import com.fpush.common.api.connection.NettyConnection;

public final class LocalRouter implements Router<NettyConnection> {
    private final NettyConnection connection;

    public LocalRouter(NettyConnection connection) {
        this.connection = connection;
    }

    public int getClientType() {
        return 2;
    }

    @Override
    public NettyConnection getRouteValue() {
        return connection;
    }

    @Override
    public RouterType getRouteType() {
        return RouterType.LOCAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalRouter that = (LocalRouter) o;

        return getClientType() == that.getClientType();

    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getClientType());
    }

    @Override
    public String toString() {
        return "LocalRouter{" + connection + '}';
    }
}
