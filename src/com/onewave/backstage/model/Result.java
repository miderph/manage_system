
package com.onewave.backstage.model;

import java.io.Serializable;

/**
 * @author liukai
 *
 */
public class Result implements Serializable
{
  
    private static final long serialVersionUID = 1321344353L;

    private int returnCode;

    public int getReturnCode()
    {
        return returnCode;
    }

    public void setReturnCode(int returnCode)
    {
        this.returnCode = returnCode;
    }

    private String des;

    public Result()
    {

    }

    public Result(int code, String des)
    {
	this.returnCode = code;
	this.des = des;
    }

    public String getDes()
    {
	return des;
    }

    public void setDes(String des)
    {
	this.des = des;
    }
}
