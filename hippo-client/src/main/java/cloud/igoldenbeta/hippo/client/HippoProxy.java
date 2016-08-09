package cloud.igoldenbeta.hippo.client;

import java.lang.reflect.Proxy;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cloud.igoldenbeta.hippo.annotation.HippoService;
import cloud.igoldenbeta.hippo.bean.HippoRequest;
import cloud.igoldenbeta.hippo.zmq.ZmqRpcClient;

/**
 * client代理类
 * 
 * @author sl
 *
 */
@Component
public class HippoProxy {

  @Autowired
  private ZmqRpcClient zmqRpcClient;

  @SuppressWarnings("unchecked")
  <T> T create(Class<?> inferfaceClass) {
    return (T) Proxy.newProxyInstance(inferfaceClass.getClassLoader(),
        new Class<?>[] {inferfaceClass}, (proxy, method, args) -> {
          HippoRequest request = new HippoRequest();
          request.setRequestId(UUID.randomUUID().toString());
          request.setClassName(method.getDeclaringClass().getName());
          request.setMethodName(method.getName());
          request.setParameterTypes(method.getParameterTypes());
          request.setParameters(args);
          return zmqRpcClient.callService(request,
              inferfaceClass.getAnnotation(HippoService.class).serviceName());
        });
  }
}
