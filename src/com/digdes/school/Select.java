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

//    private Map<String, Object> areEqualKeyValues(Map<String, Object> first, Map<String, Object> second) {
//        return first.entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey,
//                        e -> e.getValue().equals(second.get(e.getKey()))));
//    }

    public List<Map<String, Object>> run() {
        if (this.indexWhere != -1) {
            Where where = new Where(this.where, this.data);
            where.setChangeable();

            for (Map<String, Object> obj: this.data) {
                for (Map<String, Object> change: where.getChangeable()) {
//                    Map<String, Object> result = areEqualKeyValues(obj, change);
                    if (obj.equals(change))
                        this.result.add(obj);
                }
            }
        }
        else
            this.result.addAll(this.data);
        return this.result;
    }
}
