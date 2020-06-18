package jp.kusumotolab.kgenprog.project.factory;

import java.nio.file.Path;

public class AntProjectFactory extends BuildToolProjectFactory {

  private static final String CONFIG_FILE_NAME = "build.xml";

  public AntProjectFactory(final Path rootPath) {
    super(rootPath);
  }

  @Override
  public boolean isApplicable() {
    return !getConfigPath().isEmpty();
  }

  @Override
  protected String getConfigFileName() {
    return CONFIG_FILE_NAME;
  }

  @Override
  public TargetProject create() {
    // TODO
    new RuntimeException("DON'T CALL ME. This method is not implemented").printStackTrace();
    return null;
  }
}
