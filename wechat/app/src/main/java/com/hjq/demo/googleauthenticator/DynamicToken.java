package com.hjq.demo.googleauthenticator;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DynamicToken {

	private static DynamicToken instance=new DynamicToken();

	/**
	 * 单例获取
	 */
	public static DynamicToken getInstance(){
//		if(instance==null){
//			synchronized (this){
//				//必要再次判断
//				if(instance==null)
//					instance=new DynamicToken();
//			}
//		}
        return instance;
    }


	private int invertal=0;

	private DynamicToken() {}

	public String getDynamicCode(String key,long systemTime,int invertal) throws Exception{
		byte[] data;
		data = sha1(key,(systemTime+invertal)/60000);//sha1生成 20字节（160位）的数据摘要
		int o = data[19]& 0xf;//通过对最后一个字节的低4位二进制位建立索引，索引范围为  （0-15）+4  ，正好20个字节。
		int number = hashToInt(data, o)& 0x7fffffff;  //然后计算索引指向的连续4字节空间生成int整型数据。
		return output(String.valueOf(number%1000000));//对获取到的整型数据进行模运算，再对结果进行补全（长度不够6位，在首位补零）得到长度为6的字符串
	}

	public String getDynamicCode(String key,long systemTime) throws Exception{
		return getDynamicCode(key, systemTime, invertal);
	}

	public String getDynamicCode(String key) throws Exception{
		return getDynamicCode(key, System.currentTimeMillis(), invertal);
	}



   public DynamicToken setTimeIntertal(int offset){
	   this.invertal = offset;
	   return this;
   }

	private byte[] sha1(String secret,long msg) throws Exception{
		SecretKey secretKey = new SecretKeySpec(Base32String.decode(secret), "");//创建秘钥
		try {
			Mac mac= Mac.getInstance("HmacSHA1");
			mac.init(secretKey);//初始化秘钥
			byte[] value = ByteBuffer.allocate(8).putLong(msg).array();//将long类型的数据转换为byte数组
			return mac.doFinal(value);//计算数据摘要
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[20];
	}

    private int hashToInt(byte[] bytes, int start) {
        DataInput input = new DataInputStream(
            new ByteArrayInputStream(bytes, start, bytes.length - start));
        int val;
        try {
          val = input.readInt();
        } catch (IOException e) {
          throw new IllegalStateException(e);
        }
        return val;
      }

    private String output(String s){
    	if(s.length()<6){
    		s = "0"+s;
    		return output(s);
    	}
    	return s;
    }
    public static void main(String[] args) {
		DynamicToken dt = new DynamicToken();
		try {
			System.out.println(dt.getDynamicCode(args[0]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
