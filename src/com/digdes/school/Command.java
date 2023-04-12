package com.digdes.school;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Command {
    int indexWhere;
    String[] args;
    String str;
    String where;
    Map<String, Object> row;

    public enum Columns {
        id,
        lastname,
        age,
        cost,
        active
    }

    public Command(String str) {
        this.str = str;
        this.indexWhere = -1;
    }

    private void setHasWhere(String cmd) {
        if (!cmd.equals(""))
            this.indexWhere = cmd.toLowerCase().indexOf("where ");
    }

    private Boolean checkColumns(Map<String, Object> row)
    {
        try {
            Map<String, Function<String, Object>> types = new HashMap<>();

            types.put("id", Long::parseLong);
            types.put("lastname", String::toString);
            types.put("cost", Double::parseDouble);
            types.put("age", Long::parseLong);
            for (String key: row.keySet())
                Columns.valueOf(key.toLowerCase()).name();
            for (String key: row.keySet()) {
                if (key.equalsIgnoreCase("active") && !row.get(key).equals("true") && !row.get(key).equals("false"))
                    throw new Exception();
                for (String nameKey: types.keySet()) {
                    if (key.equalsIgnoreCase(nameKey)) {
                        row.put(key, types.get(nameKey).apply(row.get(key).toString()));
                    }
                }
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Wrong arguments");
            return false;
        }
    }

    public void splitArgs(String cmd) {
        try {
            if (!cmd.replaceAll("\\s", "").equals("") || cmd != null) {
                this.args = cmd.split(",");
                String[] tmpstr;
                Map<String, Object> tmpRow = new HashMap<>();

                for (String item: this.args)
                    if (!item.equals("")) {
                        tmpstr = item.split("=");
                        tmpRow.put(tmpstr[0], tmpstr[1]);
                    }
                if (checkColumns(tmpRow))
                    this.row = tmpRow;
            }
        }
        catch (Exception e) {
            System.out.println("Wrong arguments");
        }
    }

    private void splitWhere(String cmd) {
        if (!cmd.isEmpty())
            this.where = cmd.substring(this.indexWhere + 6);
    }

    public void setArgs() {
        setHasWhere(this.str);
        String tmp;

        if (this.indexWhere != -1) {
            tmp = this.str.substring(0, this.indexWhere).replaceAll("[^а-яА-Я0-9A-Za-z_.,=]","");
            splitArgs(tmp);
            splitWhere(this.str);
        }
        else {
            tmp = this.str.replaceAll("[^а-яА-Я0-9A-Za-z_.,=]","");
            splitArgs(tmp);
        }
    }
}
