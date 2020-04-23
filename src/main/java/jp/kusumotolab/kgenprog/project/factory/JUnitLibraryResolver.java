package jp.kusumotolab.kgenprog.project.factory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import jp.kusumotolab.kgenprog.CUILauncher;
import jp.kusumotolab.kgenprog.project.ClassPath;

public class JUnitLibraryResolver {

  public enum JUnitVersion {
    JUNIT3, JUNIT4
  }

  private static final String JUNIT3_DIR = "junit3/";
  private static final String JUNIT4_DIR = "junit4/";
  private static final String JUNIT3_JUNIT = "junit-3.8.2.jar";
  private static final String JUNIT4_JUNIT = "junit-4.12-kgp-custom.jar";

  public final static EnumMap<JUnitVersion, List<ClassPath>> libraries =
      new EnumMap<>(JUnitVersion.class);

  static {

    try {
      final ClassLoader classLoader = CUILauncher.class.getClassLoader();
      final InputStream junit3JInputStream =
          classLoader.getResourceAsStream(JUNIT3_DIR + JUNIT3_JUNIT);
      final InputStream junit4JInputStream =
          classLoader.getResourceAsStream(JUNIT4_DIR + JUNIT4_JUNIT);

      final Path systemTempPath = getTempDirectory();
      final Path junit3JPath = systemTempPath.resolve(JUNIT3_JUNIT);
      final Path junit4JPath = systemTempPath.resolve(JUNIT4_JUNIT);

      Files.copy(junit3JInputStream, junit3JPath, StandardCopyOption.REPLACE_EXISTING);
      Files.copy(junit4JInputStream, junit4JPath, StandardCopyOption.REPLACE_EXISTING);

      libraries.put(JUnitVersion.JUNIT3, Arrays.asList(new ClassPath(junit3JPath)));
      libraries.put(JUnitVersion.JUNIT4, Arrays.asList(new ClassPath(junit4JPath)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // TODO 一時dirの責務をひとまずこのクラスに任せたが，巨大になるなら別クラスに切った方がよさそう．
  private static Path tempDir;
  public static Path getTempDirectory() {
    try {
      if (null == tempDir) {
        tempDir = Files.createTempDirectory("kgp-");
        return tempDir;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tempDir;
  }
}
