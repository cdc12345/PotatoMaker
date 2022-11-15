package org.cdc.potatomaker.resourcepack;

import lombok.Data;
import org.cdc.potatomaker.annotation.Open;

import java.awt.*;
import java.io.File;

/**
 * e-mail: 3154934427@qq.com
 * 资源包载入器,用于资源包载入时
 *
 * @author cdc123
 * @classname PackLoader
 * @date 2022/11/15 10:37
 */
@Open
@Data
public class PackLoader {
    //肯定有人想问我为什么这次不用Builder,哦,因为数据比较少,也不需要进行特别处理
    public PackLoader(PackInfo info, File packFolder, Image icon) {
        this.info = info;
        this.packFolder = packFolder;
        this.icon = icon;
    }
    private PackInfo info;

    private File packFolder;

    private Image icon;
}
