package org.cdc.potatomaker.preference;

import java.util.concurrent.ConcurrentHashMap;

/**
 * e-mail: 3154934427@qq.com
 * 设置
 *
 * @author cdc123
 * @classname Preferences
 * @date 2022/11/13 22:24
 */
public final class Preferences extends ConcurrentHashMap<String,Object> {
    public  Preferences(){super();}

    public Preferences clone()  {
        try {
            return (Preferences) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
