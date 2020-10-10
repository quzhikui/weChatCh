/**
 * 
 */
package weChatCh;

import java.security.MessageDigest;

/**
 * @author Administrator
 *
 */
public class MD5Utils {
    private static final String hexDigIts[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

    /**
     * MD5����
     * @param origin �ַ�
     * @param charsetname ����
     * @return
     */
    public static String MD5Encode(String origin, String charsetname){
        String resultString = null;
        try{
            resultString = new String(origin);
            //��������MD5�㷨����ϢժҪ
            MessageDigest md = MessageDigest.getInstance("MD5");
            if(null == charsetname || "".equals(charsetname)){
            //ʹ��ָ�����ֽ������ժҪ���������£�Ȼ�����ժҪ����
            //���õ����ֽ��������ַ�������
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            }else{
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        }catch (Exception e){
        }
        return resultString;
    }

        /**
     * ���ֽ�����ת����ʮ�����ƣ������ַ�������ʽ����
     * 128λ��ָ������λ��������̫��������һ�㶼��д��16���ƣ�
     * ÿһλ16���������Դ���4λ��������������128λ��������д��16���ƾͱ����128/4=32λ��
     */
    public static String byteArrayToHexString(byte b[]){
        StringBuffer resultSb = new StringBuffer();
        for(int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    /**
     * ��һ���ֽ�ת����ʮ�����ƣ������ַ�������ʽ����
     */
    public static String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

}
