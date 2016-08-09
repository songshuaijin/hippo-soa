package cloud.igoldenbeta.hippo.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存了具体SOA实现类
 * 
 * @author sl
 *
 */
public enum HippoServiceImplCache {

  INSTANCE;
  private Map<String, Object> handlerMap = new HashMap<>();

  HippoServiceImplCache() {}

  public Map<String, Object> getHandlerMap() {
    return handlerMap;
  }
}
