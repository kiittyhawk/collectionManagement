package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Delete extends Command {
    private final List<Map<String, Object>> data;
    public Delete(String str, List<Map<String, Object>> data) {
        super(str);
        setArgs();
        this.data = data;
    }

    public void run() {
        if (this.indexWhere != -1) {
            Where where = new Where(this.where, this.data);
            where.setChangeable();

            for (Map<String, Object> obj: this.data)
                for (Map<String, Object> change: where.getChangeable())
                    if (obj.equals(change))
                        obj.clear();
        }
        else
            this.data.clear();
    }
}
