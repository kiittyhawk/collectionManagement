package com.digdes.school;

import java.lang.invoke.WrongMethodTypeException;
import java.util.*;

public class JavaSchoolStarter
{
    private int cmdId;
    List<Map<String, Object>> collection = new ArrayList<>();
    private String cmd;

    //Дефолтный конструктор
    public JavaSchoolStarter(){ }

    public int getCmdId() {return this.cmdId;}

    private String setCmdId(String cmd)
    {
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
        else if (cmd.toLowerCase().startsWith("delete "))
        {
            this.cmdId = 3;
            return cmd.substring(7);
        }
        else if (cmd.toLowerCase().startsWith("select "))
        {
            this.cmdId = 4;
            return cmd.substring(7);
        }
        else
            throw new WrongMethodTypeException();
    }


    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String,Object>> execute(String request) throws Exception
    {
        if (request.equals(""))
            throw new EmptyStackException();
        this.cmd = request;
        this.cmd = setCmdId(this.cmd);
        System.out.println("cmdId = " + this.cmdId);
        switch (getCmdId())
        {
            case 1:
                Insert ex = new Insert(this.cmd);
                if (ex.row != null)
                    collection.add(ex.row);
                break;
            case 2:
                Update update = new Update(this.cmd, collection);
                System.out.println("indexWhere = " + update.indexWhere);
                update.run();
                break;
            case 3:
                Delete delete = new Delete(this.cmd, collection);
                delete.run();
                break;
            case 4:
                Select select = new Select(this.cmd, collection);
                System.out.println(select.run().toString());
                break;
        }
        return collection;
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
