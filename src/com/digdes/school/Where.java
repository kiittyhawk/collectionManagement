package com.digdes.school;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Where {
    private List<Map<String, Object>> changeable;
    private int andOr;
    private final String originalStr;
    private String[] conditions;
    private List<Map<String, Object>> data;
    private String currentOper;

    public Where(String str, List<Map<String, Object>> data) {
        this.changeable = new ArrayList<Map<String, Object>>();
        this.andOr = 0;
        this.originalStr = str;
        this.data = data;
        this.currentOper = null;
    }

    public int getAndOr() {
        return this.andOr;
    }

    public List<Map<String, Object>> getChangeable() {
        return this.changeable;
    }

    public String getOriginalStr() {
        return this.originalStr;
    }

    private void setAndOr(String str) {
        if (str.contains(" and "))
            this.andOr = 1;
        if (str.contains(" or "))
            this.andOr = 2;
    }

    private void setConditions(String str) {
        if (this.andOr == 1) {
            this.conditions = str.split(" and ");
        }
        if (this.andOr == 2) {
            this.conditions = str.split(" or ");
        }
    }

    private void checkColumns(String key, String value) {
        try {
            Map<String, Function<String, Object>> types = new HashMap<>();

            types.put("id", Long::parseLong);
            types.put("lastname", String::toString);
            types.put("cost", Double::parseDouble);
            types.put("age", Long::parseLong);
            System.out.println(Command.Columns.valueOf(key).name());
            if (key.equalsIgnoreCase("active") && !value.equals("true") && !value.equals("false"))
                    throw new Exception();
                for (String nameKey: types.keySet()) {
                    if (key.equalsIgnoreCase(nameKey)) {
                        System.out.println("\n" + key + "--" + nameKey);
                        types.get(nameKey).apply(value);
                    }
                }
        }
        catch (Exception e) {
            System.out.println("Invalid comparison format");
        }
    }

    private void runEquals(String cond, List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("[^а-яА-Я0-9A-Za-z_.,=]","").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;

        checkColumns(values[0], values[1]);
        for (int i = 0; i < data.size(); i++) {
            Node = data.get(i).entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(values[0]) &&
                            x.getValue().toString().equals(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void runNotEquals(String cond, List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("[^а-яА-Я0-9A-Za-z_.,=!]","").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;

        checkColumns(values[0], values[1]);
        for (int i = 0; i < data.size(); i++) {
            Node = data.get(i).entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(values[0]) &&
                            !(x.getValue().toString().equals(values[1])))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void execLike(String cond, BiPredicate<String, String> exec, boolean isLower,
                          List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;

        if (isLower)
            for (int i = 0; i < data.size(); i++) {
                Node = data.get(i).entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(values[0]) &&
                        exec.test(x.getValue().toString().toLowerCase(), values[1].replaceAll("%", "").toLowerCase()))
                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
                if (Node != null)
                    this.changeable.add(Node);
            }
        else
            for (int i = 0; i < data.size(); i++) {
                Node = data.get(i).entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(values[0]) &&
                        exec.test(x.getValue().toString(), values[1].replaceAll("%", "")))
                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
                if (Node != null)
                    this.changeable.add(Node);
            }
    }

    private void runLike(String cond, List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        boolean flag = false;
        if (!values[0].equalsIgnoreCase("lastName"))
            throw new NumberFormatException();

        if (this.currentOper.equals("ilike"))
            flag = true;
        if (values[1].startsWith("%") && values[1].endsWith("%"))
            execLike(cond, String::contains, flag, data);
        else if (values[1].endsWith("%"))
            execLike(cond, String::startsWith, flag, data);
        else if (values[1].startsWith("%"))
            execLike(cond, String::endsWith, flag, data);
        else
            execLike(cond, String::equals, flag, data);
    }

    private void moreEqual(String cond, List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        Map<String, Object> Node = null;

        if (values[0].equalsIgnoreCase("lastName") || values[0].equalsIgnoreCase("active"))
            throw new NumberFormatException();
        System.out.println(Arrays.toString(values));
        checkColumns(values[0], values[1]);
        for (int i = 0; i < data.size(); i++) {
            Node = data.get(i).entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(values[0]) &&
                            Integer.parseInt(x.getValue().toString()) >= Integer.parseInt(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void lessEqual(String cond, List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;

        if (values[0].equalsIgnoreCase("lastName") || values[0].equalsIgnoreCase("active"))
            throw new NumberFormatException();
        for (int i = 0; i < data.size(); i++) {
            Node = data.get(i).entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(values[0]) &&
                            Integer.parseInt(x.getValue().toString()) <= Integer.parseInt(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void less(String cond, List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;

        if (values[0].equalsIgnoreCase("lastName") || values[0].equalsIgnoreCase("active"))
            throw new NumberFormatException();
        for (int i = 0; i < data.size(); i++) {
            Node = data.get(i).entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(values[0]) &&
                            Integer.parseInt(x.getValue().toString()) < Integer.parseInt(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void more(String cond, List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;

        if (values[0].equalsIgnoreCase("lastName") || values[0].equalsIgnoreCase("active"))
            throw new NumberFormatException();
        for (int i = 0; i < data.size(); i++) {
            Node = data.get(i).entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(values[0]) &&
                            Integer.parseInt(x.getValue().toString()) > Integer.parseInt(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void executeOperator(String cond, List<Map<String, Object>> data) {
        String[] operators = new String[] {"<=", ">=", "!=", "=", "ilike", "like", "<", ">" };

        for (String oper: operators)
            if (cond.contains(oper)) {
                this.currentOper = oper;
                System.out.println("currentOper = " + this.currentOper);
                break;
            }
        if (this.currentOper != null) {
            try {
                switch (this.currentOper) {
                    case ">=" -> moreEqual(cond, data);
                    case "<" -> less(cond, data);
                    case ">" -> more(cond, data);
                    case "<=" -> lessEqual(cond, data);
                    case "=" -> runEquals(cond, data);
                    case "!=" -> runNotEquals(cond, data);
                    case "like", "ilike" -> runLike(cond, data);
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid comparison format");
                System.exit(1);
            }
        }
    }

    private Map<String, Object> areEqualKeyValues(Map<String, Object> first, Map<String, Object> second) {
        return first.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> e.getValue().equals(second.get(e.getKey()))));
    }

    private List<Map<String, Object>> changeableSetup() {
        List<Map<String, Object>> newList = new ArrayList<>();

        for (int i = 0; i < this.data.size(); i++) {
            for (int j = 0; j < this.changeable.size(); j++) {
                Map<String, Object> result = areEqualKeyValues(this.data.get(i), this.changeable.get(j));
                if (result.containsValue(true))
                    newList.add(this.data.get(i));
            }
        }
        return newList;
    }

    private void execAnd() {
        switch (this.andOr) {
            case 1:
                System.out.println("\nAND execute");
                System.out.println("changeable: " + this.changeable);
                executeOperator(this.conditions[0], this.data);
                List<Map<String, Object>> tmpChange = changeableSetup();
                this.changeable.clear();
//                this.changeable = new ArrayList<>();
                System.out.println("tmpChange: " + tmpChange);
                executeOperator(this.conditions[1], tmpChange);
                System.out.println("changeable: " + this.changeable);
                break;
            case 2:
                for (String cond: this.conditions)
                    executeOperator(cond, this.data);
                break;
        }

    }

    public void setChangeable() {
        if (this.originalStr != null) {
            setAndOr(this.originalStr);

            if (this.andOr != 0) {
                setConditions(this.originalStr);
                execAnd();
                System.out.println("Conditions = " + Arrays.toString(this.conditions));
            }
            else
                executeOperator(this.originalStr, this.data);
        }
        System.out.println(this.changeable);
    }
}
