package org.cdc.potatomaker.resourcepack;

import lombok.Data;
import org.cdc.potatomaker.annotation.Open;

/**
 * e-mail: 3154934427@qq.com
 * 资源包信息
 *
 * @author cdc123
 * @classname PackInfo
 * @date 2022/11/15 9:55
 */
@Data
@Open
public class PackInfo {
    /**
     * 资源包名称
     */
    private String name;
    /**
     * 资源包支持的最小版本
     */
    private int minVersion = 1;
    /**
     * 资源包描述
     */
    private String description;
}
