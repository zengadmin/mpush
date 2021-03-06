
package com.fpush.monitor.data;

import java.util.HashMap;
import java.util.Map;

import com.fyqz.tools.utils.Jsons;

public class MonitorResult {
    private Long timestamp = System.currentTimeMillis();
    private Map<String, Object> results = new HashMap<String,Object>(8);

    public MonitorResult addResult(String name, Object result) {
        results.put(name, result);
        return this;
    }

    public Map<String, Object> getResults() {
        return results;
    }

    public MonitorResult setResults(Map<String, Object> results) {
        this.results = results;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public MonitorResult setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public String toString() {
        return "MonitorResult{" +
                "results=" + results +
                ", timestamp=" + timestamp +
                '}';
    }

    public String toJson() {
        return Jsons.toJson(this);
    }
}
