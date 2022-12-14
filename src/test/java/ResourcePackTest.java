
import org.cdc.potatomaker.Launcher;
import org.cdc.potatomaker.events.Events;
import org.cdc.potatomaker.events.PMEvents;
import org.cdc.potatomaker.events.ResourcePackLoadedEvent;
import org.cdc.potatomaker.exception.DefinedException;
import org.cdc.potatomaker.resourcepack.PMResourcePackLoader;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;


/**
 * e-mail: 3154934427@qq.com
 * 资源包测试类
 *
 * @author cdc123
 * @classname ResourcePackTest
 * @date 2022/11/15 11:38
 */
public class ResourcePackTest {
    @Test
    public void testPacks() throws FileNotFoundException, DefinedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Launcher.main(new String[0]);
        PMEvents.registerListener(ResourcePackLoadedEvent.class, "ResourcePack-Test",event -> {
            System.out.println("资源包已经载入"+event.getPackName());
        });
        PMResourcePackLoader.getInstance().loadPacks();
    }
}
