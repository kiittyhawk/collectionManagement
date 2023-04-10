package com.digdes.school;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
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
        this.currentOper = "";
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

    private void runEquals(String cond, List<Map<String, Object>> data) {
        String[] values = cond.replaceAll("[^а-яА-Я0-9A-Za-z_.,=]","").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;
        for (int i = 0; i < this.data.size(); i++) {
            Node = this.data.get(i).entrySet().stream().filter(x -> x.getKey().equals(values[0]) &&
                            x.getValue().equals(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void runNotEquals(String cond) {
        String[] values = cond.replaceAll("[^а-яА-Я0-9A-Za-z_.,=!]","").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;
        for (int i = 0; i < this.data.size(); i++) {
            Node = this.data.get(i).entrySet().stream().filter(x -> x.getKey().equals(values[0]) &&
                            !(x.getValue().equals(values[1])))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void execLike(String cond, BiPredicate<String, String> exec, boolean isLower) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;

        if (isLower)
            for (int i = 0; i < this.data.size(); i++) {
                Node = this.data.get(i).entrySet().stream().filter(x -> x.getKey().equals(values[0]) &&
                        exec.test(x.getValue().toString().toLowerCase(), values[1].replaceAll("%", "").toLowerCase()))
                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
                if (Node != null)
                    this.changeable.add(Node);
            }
        else
            for (int i = 0; i < this.data.size(); i++) {
                Node = this.data.get(i).entrySet().stream().filter(x -> x.getKey().equals(values[0]) &&
                                exec.test(x.getValue().toString(), values[1].replaceAll("%", "")))
                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
                if (Node != null)
                    this.changeable.add(Node);
            }
    }

    private void runLike(String cond) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        boolean flag = false;

        if (this.currentOper.equals("ilike"))
            flag = true;
        if (values[1].startsWith("%") && values[1].endsWith("%"))
            execLike(cond, String::contains, flag);
        else if (values[1].endsWith("%"))
            execLike(cond, String::startsWith, flag);
        else if (values[1].startsWith("%"))
            execLike(cond, String::endsWith, flag);
        else
            execLike(cond, String::equals, flag);
    }

    private void moreEqual(String cond) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;
        for (int i = 0; i < this.data.size(); i++) {
            Node = this.data.get(i).entrySet().stream().filter(x -> x.getKey().equals(values[0]) &&
                            Integer.parseInt(x.getValue().toString()) >= Integer.parseInt(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void lessEqual(String cond) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;
        for (int i = 0; i < this.data.size(); i++) {
            Node = this.data.get(i).entrySet().stream().filter(x -> x.getKey().equals(values[0]) &&
                            Integer.parseInt(x.getValue().toString()) <= Integer.parseInt(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void less(String cond) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;
        for (int i = 0; i < this.data.size(); i++) {
            Node = this.data.get(i).entrySet().stream().filter(x -> x.getKey().equals(values[0]) &&
                            Integer.parseInt(x.getValue().toString()) < Integer.parseInt(values[1]))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            if (Node != null)
                this.changeable.add(Node);
        }
    }

    private void more(String cond) {
        String[] values = cond.replaceAll("\\s", "").split(this.currentOper);
        System.out.println(Arrays.toString(values));
        Map<String, Object> Node = null;
        for (int i = 0; i < this.data.size(); i++) {
            Node = this.data.get(i).entrySet().stream().filter(x -> x.getKey().equals(values[0]) &&
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
        if (!this.currentOper.equals("") || this.currentOper != null) {
            switch (this.currentOper) {
                case ">=":
                    moreEqual(cond);
                    break;
                case "<":
                    less(cond);
                    break;
                case ">":
                    more(cond);
                    break;
                case "<=":
                    lessEqual(cond);
                    break;
                case "=":
                    runEquals(cond, data);
                    break;
                case "!=":
                    runNotEquals(cond);
                    break;
                case "like", "ilike":
                    runLike(cond);
                    break;
            }
        }
    }

    private void execAnd() {
        switch (this.andOr) {
            case 1:
                executeOperator(this.conditions[0], this.data);
                executeOperator(this.conditions[1], this.changeable);
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
