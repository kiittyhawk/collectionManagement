package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Select extends Command {
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> result;

    public Select(String str, List<Map<String, Object>> data) {
        super(str);
        setArgs();
        this.data = data;
        result = new ArrayList<>();
    }

    private Map<String, Object> areEqualKeyValues(Map<String, Object> first, Map<String, Object> second) {
        return first.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> e.getValue().equals(second.get(e.getKey()))));
    }

    public List<Map<String, Object>> run() {
        if (this.indexWhere != -1) {
            Where where = new Where(this.where, this.data);
            where.setChangeable();

            for (int i = 0; i < this.data.size(); i++) {
                for (int j = 0; j < where.getChangeable().size(); j++) {
                    Map<String, Object> result = areEqualKeyValues(this.data.get(i), where.getChangeable().get(j));
                    if (result.containsValue(true))
                        this.result.add(this.data.get(i));
                }
            }
        }
        else {
            for (int i = 0; i < this.data.size(); i++) {
                this.result.add(this.data.get(i));
            }
        }
        return this.result;
    }
}
