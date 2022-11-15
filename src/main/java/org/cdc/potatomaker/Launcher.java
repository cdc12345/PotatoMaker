package org.cdc.potatomaker;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.exception.DefinedException;
import org.cdc.potatomaker.util.DefaultExceptionHandler;
import org.cdc.potatomaker.util.LoggingOutputStream;
import org.cdc.potatomaker.util.OSUtil;
import org.cdc.potatomaker.util.PotatoMakerApplication;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * e-mail: 3154934427@qq.com
 * 主类
 *
 * @author cdc123
 * @classname Launcher
 * @date 2022/11/5 12:09
 */
public class Launcher {

    public static void main(String[] args)  {

        System.setProperty("jna.nosys", "true");
        System.setProperty("log_directory",System.getProperty("user.dir"));

        if (OSUtil.getOS() == OSUtil.WINDOWS && ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
                .noneMatch(arg -> arg.contains("idea_rt.jar"))) {
            System.setProperty("log_disable_ansi", "true");
        } else {
            System.setProperty("log_disable_ansi", "false");
        }

        final Logger LOG = LogManager.getLogger("Launcher"); // init logger after log directory is set

        System.setErr(new PrintStream(new LoggingOutputStream(LogManager.getLogger("STDERR"), Level.ERROR), true));
        System.setOut(new PrintStream(new LoggingOutputStream(LogManager.getLogger("STDOUT"), Level.INFO), true));
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());

        String logo = """
                  
                  @@@@@@@@@@@@@@@\\].           =@@@@@@^                       ,@@@@@@^  \s
                  @@@@@@@@@@@@@@@@@@@\\.        =@@@@@@@^                     .@@@@@@@^  \s
                  @@@@@.      .[\\@@@@@@^       =@@@@@@@@`                   .@@@@@@@@^  \s
                  @@@@@.          ,@@@@@^      =@@@@=@@@@.                 .@@@@O@@@@^  \s
                  @@@@@.           =@@@@O      =@@@@`@@@@@.                /@@@/=@@@@^  \s
                  @@@@@.           .@@@@@      =@@@@^.@@@@\\               /@@@@.=@@@@^  \s
                  @@@@@.           ,@@@@O      =@@@@^ ,@@@@\\             =@@@@. =@@@@^  \s
                  @@@@@.          ./@@@@^      =@@@@^  ,@@@@^           =@@@@`  =@@@@^  \s
                  @@@@@.        .]@@@@@^       =@@@@^   =@@@@^         ,@@@@`   =@@@@^  \s
                  @@@@@\\]]]]/@@@@@@@@@`        =@@@@^    =@@@@`       ,@@@@^    =@@@@^  \s
                  @@@@@@@@@@@@@@@@@[.          =@@@@^     \\@@@@.     .@@@@^     =@@@@^  \s
                  @@@@@[[[[[[[[.               =@@@@^     .\\@@@@.   .@@@@/      =@@@@^  \s
                  @@@@@.                       =@@@@^      .@@@@\\   /@@@/       =@@@@^  \s
                  @@@@@.                       =@@@@^       .@@@@^ =@@@@.       =@@@@^  \s
                  @@@@@.                       =@@@@^        ,@@@@\\@@@@.        =@@@@^  \s
                  @@@@@.                       =@@@@^         ,@@@@@@@`         =@@@@^  \s
                  @@@@@.                       =@@@@^          =@@@@@`          =@@@@^  \s
                  @@@@@.                       =@@@@^           =@@@^           =@@@@^  \s
                """;
        LOG.info(logo);
        LOG.info("PotatoMaker感谢您的使用");

        new Thread(()-> {
            try {
                PotatoMakerApplication.createInstance(args);
            } catch (Exception e) {
                e.printStackTrace();
                Runtime.getRuntime().exit(-1);
            }
        }).start();
    }
}
