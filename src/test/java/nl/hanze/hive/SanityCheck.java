package nl.hanze.hive;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class SanityCheck {
    static Class<? extends Hive> hiveClass;
    Hive hive;

    @BeforeAll
    static void setUpClass() throws IOException, URISyntaxException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            if (!url.getProtocol().equals("file")) {
                continue;
            }
            Deque<File> queue = new ArrayDeque<>();
            queue.addFirst(new File(url.toURI()));
            String path = queue.peekFirst().getPath();
            while (!queue.isEmpty()) {
                File file = queue.remove();
                String name = file.getName();
                if (file.isDirectory() && !name.contains(".")) {
                    for (File child : file.listFiles()) {
                        queue.addFirst(child);
                    }
                } else if (file.isFile() && name.endsWith(".class")) {
                    Class cls = Class.forName(file.getPath().substring(path.length() + 1, file.getPath().length() - 6).replace(File.separator, "."));
                    if (!cls.equals(Hive.class) && Hive.class.isAssignableFrom(cls)) {
                        if (hiveClass != null) {
                            fail("Multiple implementation of " + Hive.class.getCanonicalName() + " found");
                        }
                        hiveClass = cls;
                    }
                }
            }
        }
        if (hiveClass == null) {
            fail("No implementations of " + Hive.class.getCanonicalName() + " found");
        }
    }

    @BeforeEach
    void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        hive = hiveClass.getDeclaredConstructor().newInstance();
    }

    @Test
    void testSanityCheck() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 1, 2);
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.QUEEN_BEE, 1, 2));
        hive.play(Hive.Tile.QUEEN_BEE, 1, 1);
        assertThrows(Hive.IllegalMove.class, () -> hive.move(1, 2, 1, 3));
        hive.move(1, 2, 2, 1);
    }
}
