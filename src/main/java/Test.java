import com.example.oslc.Neo4jApplication;
import com.example.oslc.contoller.BlockController;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialog;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialogs;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.springframework.boot.SpringApplication;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Map;

public class Test {
    public static void main(String[] args)   {
        Class ResourceClass = BlockController.class;
        Method[] var5 = ResourceClass.getMethods();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            GET getAnnotation = (GET)method.getAnnotation(GET.class);
            if(getAnnotation == null) {
                System.out.println("123");
            }
        }
    }
}
