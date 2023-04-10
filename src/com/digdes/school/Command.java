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
        System.out.println("Исходная строка - " + this.str);
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
                System.out.println(Columns.valueOf(key.toLowerCase()).name());
            for (String key: row.keySet()) {
                if (key.equalsIgnoreCase("active") && !row.get(key).equals("true") && !row.get(key).equals("false"))
                    throw new Exception();
                for (String nameKey: types.keySet()) {
                    if (key.equalsIgnoreCase(nameKey)) {
                        System.out.println("\n" + key + "--" + nameKey);
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
                {
                    if (!item.equals("")) {
                        tmpstr = item.split("=");
                        tmpRow.put(tmpstr[0], tmpstr[1]);
                    }
                }
                System.out.println(tmpRow.keySet());
                System.out.println(tmpRow.values());
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
        System.out.println("Where index is " + this.indexWhere);
        String tmp;
//        this.str = this.str.replaceAll("[^а-яА-Я0-9A-Za-z_.,=]","");

        if (this.indexWhere != -1) {
            System.out.println("Where есть");
            tmp = this.str.substring(0, this.indexWhere).replaceAll("[^а-яА-Я0-9A-Za-z_.,=]","");
            System.out.println("Готовая строка - " + tmp);
            System.out.println("Аргументы:");
            splitArgs(tmp);
            splitWhere(this.str);
            System.out.println("Условие: " + this.where);

        }
        else {
            System.out.println("Where нет");
            tmp = this.str.replaceAll("[^а-яА-Я0-9A-Za-z_.,=]","");
            System.out.println("Готовая строка - " + tmp);
            System.out.println("Аргументы:");
            splitArgs(tmp);
        }
    }
}
