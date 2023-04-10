package com.digdes.school;
import java.util.HashMap;
import java.util.Map;

public abstract class Command {
    int indexWhere;
    String[] args;
    String str;
    String where;
    Map<String, Object> row;

    public enum Columns {
        id,
        lastName,
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

    private void checkColumns(Map<String, Object> row)
    {
        try {
            for (String key: row.keySet())
                System.out.println(Columns.valueOf(key).name());
        }
        catch (IllegalArgumentException e) {
            System.out.println("Wrong column's name");
            System.exit(1);
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
                checkColumns(tmpRow);
                this.row = tmpRow;
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
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
