package org.cdc.potatomaker.workspace;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.cdc.potatomaker.annotation.Open;

import java.io.File;
import java.util.HashMap;

/**
 * e-mail: 3154934427@qq.com
 * 工作区配置信息类
 *
 * @author cdc123
 * @classname WorkspaceConfig
 * @date 2022/11/15 21:16
 */
@Data
@Open
public class WorkspaceConfig {

    @Expose
    private String workspaceName;

    @Expose
    private String version;

    @Expose
    private String type = "default";

    @Expose
    private String fork = "1.0";

    @Expose
    private String[] plugins;

    private File workspaceFolder;

    private File workspaceConfigFolder;

    @Expose
    private HashMap<String,Object> extraConfigs = new HashMap<>();

}
