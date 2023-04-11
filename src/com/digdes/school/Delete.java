package com.digdes.school;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Delete extends Command {
    private final List<Map<String, Object>> data;
    public Delete(String str, List<Map<String, Object>> data) {
        super(str);
        setArgs();
        this.data = data;
    }

//    private Map<String, Object> areEqualKeyValues(Map<String, Object> first, Map<String, Object> second) {
//        return first.entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey,
//                        e -> e.getValue().equals(second.get(e.getKey()))));
//    }

    public void run() {
        if (this.indexWhere != -1) {
            Where where = new Where(this.where, this.data);
            where.setChangeable();

            for (Map<String, Object> obj: this.data) {
                for (Map<String, Object> change: where.getChangeable()) {
//                    Map<String, Object> result = areEqualKeyValues(obj, where.getChangeable().get(j));
                    if (obj.equals(change))
                        obj.clear();
                }
            }
        }
        else {
            this.data.clear();
        }
    }
}
