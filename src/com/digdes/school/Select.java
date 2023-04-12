package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Select extends Command {
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> result;

    public Select(String str, List<Map<String, Object>> data) {
        super(str);
        setArgs();
        this.data = data;
        result = new ArrayList<>();
    }

    public List<Map<String, Object>> run() {
        if (this.indexWhere != -1) {
            Where where = new Where(this.where, this.data);
            where.setChangeable();

            for (Map<String, Object> obj: this.data) {
                for (Map<String, Object> change: where.getChangeable()) {
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
