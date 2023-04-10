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
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> e.getValue().equals(second.get(e.getKey()))));
    }

    public void run() {
        if (this.indexWhere != -1) {
            Where where = new Where(this.where, this.data);
            where.setChangeable();

            for (int i = 0; i < this.data.size(); i++) {
                for (int j = 0; j < where.getChangeable().size(); j++) {
                    Map<String, Object> result = areEqualKeyValues(this.data.get(i), where.getChangeable().get(j));
                    if (result.containsValue(true)) {
                        for (String key : this.row.keySet()) {
                            this.data.get(i).put(key, this.row.get(key));
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < this.data.size(); i++)
                for (String key : this.row.keySet()) {
                    this.data.get(i).put(key, this.row.get(key));
                }
        }
    }
}
