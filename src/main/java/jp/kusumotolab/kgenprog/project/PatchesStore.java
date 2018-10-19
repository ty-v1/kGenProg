package jp.kusumotolab.kgenprog.project;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatchesStore {

  private static Logger log = LoggerFactory.getLogger(PatchesStore.class);

  private final List<Patches> patchesList = new ArrayList<>();

  public void add(final Patches patch) {
    patchesList.add(patch);
  }

  public void writeToFile(final Path outDir) {
    log.debug("enter writeToFile(String)");
    final String timeStamp = getTimeStamp();
    final Path outDirInthisExecution = outDir.resolve(timeStamp);

    for (final Patches patches : patchesList) {
      final String variantId = makeVariantId(patches);
      final Path variantDir = outDirInthisExecution.resolve(variantId);
      patches.writeToFile(variantDir);
    }
  }

  public void writeToLogger() {
    log.debug("enter writeToLogger()");

    for (final Patches patches : patchesList) {
      patches.writeToLogger();
    }
  }

  private String makeVariantId(final Patches patches) {
    return "variant" + (patchesList.indexOf(patches) + 1);
  }

  private String getTimeStamp() {
    final Date date = new Date();
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    return sdf.format(date);
  }
}
