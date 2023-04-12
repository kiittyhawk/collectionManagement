package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Update extends Command {
    private final List<Map<String, Object>> data;

    public Update(String str, List<Map<String, Object>> data) {
        super(str);
        setArgs();
        this.data = data;
    }

    public void run() {
        if (this.indexWhere != -1) {
            Where where = new Where(this.where, this.data);
            where.setChangeable();

            for (Map<String, Object> obj: this.data)
                for (Map<String, Object> change: where.getChangeable()) {
                    if (obj.equals(change)) {
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
