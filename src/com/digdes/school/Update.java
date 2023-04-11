package com.digdes.school;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Update extends Command {
    private final List<Map<String, Object>> data;

    public Update(String str, List<Map<String, Object>> data) {
        super(str);
        setArgs();
        this.data = data;
    }

    private Map<String, Object> areEqualKeyValues(Map<String, Object> first, Map<String, Object> second) {
        return first.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().equals(second.get(e.getKey()))));
    }

    public void run() {
        if (this.indexWhere != -1) {
            Where where = new Where(this.where, this.data);
            where.setChangeable();

            for (Map<String, Object> obj: this.data)
                for (Map<String, Object> change: where.getChangeable()) {
                    Map<String, Object> result = areEqualKeyValues(obj, change);
                    if (result.containsValue(true)) {
                        for (String key : this.row.keySet())
                            obj.put(key, this.row.get(key));
                    }
                }
        }
        else {
            for (Map<String, Object> obj: this.data)
                for (String key : this.row.keySet())
                    obj.put(key, this.row.get(key));
        }
    }
}
