package com.fpush.monitor.quota;

public interface InfoQuota extends MonitorQuota {

    String pid();

    double load();
}
