package org.cdc.potatomaker.ui;

import com.google.gson.Gson;
import org.cdc.potatomaker.ui.component.ImagePanel;
import org.cdc.potatomaker.util.fold.UserFolderManager;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static org.cdc.potatomaker.util.ResourceManager.fromInputStream;

/**
 * e-mail: 3154934427@qq.com
 * 工作区选择
 *
 * @author cdc123
 * @classname WorkspaceSelector
 * @date 2022/11/13 22:04
 */
public class WorkspaceSelector extends JFrame {

    ArrayList<File> workspaces = new ArrayList<>();

    RecentWorkspaces recent;

    public WorkspaceSelector(){
        //TODO
//        ImagePanel panel = new ImagePanel()

    }

    public void reloadWorkspaces(){
        workspaces.clear();
        File[] workspaces1 = UserFolderManager.getInstance().getWorkspaceFolder().listFiles(a->{
           File pm = new File(a,".pm");
           return pm.exists();
        });
        if (workspaces1 == null) workspaces1 = new File[0];
        workspaces.addAll(Arrays.asList(workspaces1));
        workspaces.addAll(reloadRecentWorkspaces());
    }

    public RecentWorkspaces reloadRecentWorkspaces(){
        return recent = new Gson().fromJson(
                fromInputStream(UserFolderManager.getInstance().getOuterResourceAsStream("recentWorkspaces.json",
                        new ByteArrayInputStream("[]".getBytes(StandardCharsets.UTF_8)))),RecentWorkspaces.class);
    }

    public void storeRecentWorkspace(){
        if (recent == null) reloadRecentWorkspaces();
        UserFolderManager.getInstance().writeResource(new Gson().toJson(recent),"recentWorkspaces.json");
    }

    public void addAndUpdateRecentWorkspace(){

    }

    public static class RecentWorkspaces extends ArrayList<File> {}

}
