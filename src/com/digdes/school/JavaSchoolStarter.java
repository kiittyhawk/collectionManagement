package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter
{
    private int cmdId;
    List<Map<String, Object>> collection = new ArrayList<>();

    public JavaSchoolStarter(){ }

    public int getCmdId() {
        return this.cmdId;
    }

    private String setCmdId(String cmd) throws Exception {
        if (cmd.toLowerCase().startsWith("insert values "))
        {
            this.cmdId = 1;
            return cmd.substring(14);
        }
        else if (cmd.toLowerCase().startsWith("update values "))
        {
            this.cmdId = 2;
            return cmd.substring(14);
        }
        else if (cmd.toLowerCase().startsWith("delete"))
        {
            this.cmdId = 3;
            return cmd.substring(6);
        }
        else if (cmd.toLowerCase().startsWith("select"))
        {
            this.cmdId = 4;
            return cmd.substring(6);
        }
        else
            throw new Exception("Invalid command name");
    }

    private List<Map<String, Object>> removeEmpty(List<Map<String, Object>> data) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isEmpty())
                data.remove(i);
        }
        return data;
    }

    public List<Map<String,Object>> execute(String request) throws Exception
    {
        if (request.equals(""))
            throw new Exception();
        String cmd = setCmdId(request);
        switch (getCmdId())
        {
            case 1 -> {
                Insert ex = new Insert(cmd);
                if (ex.row != null)
                    collection.add(ex.row);
            }
            case 2 -> {
                Update update = new Update(cmd, collection);
                update.run();
            }
            case 3 -> {
                Delete delete = new Delete(cmd, collection);
                delete.run();
            }
            case 4 -> {
                Select select = new Select(cmd, collection);
                return select.run();
            }
        }
        return removeEmpty(collection);
    }

    public static void main (String[] args)
    {
        Scanner in = new Scanner(System.in);
        String cmd = in.nextLine();
        JavaSchoolStarter obj = new JavaSchoolStarter();
        while (!cmd.equals("exit"))
        {
            try
            {
                List<Map<String, Object>> data = obj.execute(in.nextLine());
                System.out.println("Готовая: " + data.toString());
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }
    }
}
