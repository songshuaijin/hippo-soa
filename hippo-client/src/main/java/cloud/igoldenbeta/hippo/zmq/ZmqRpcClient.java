package cloud.igoldenbeta.hippo.zmq;

import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import cloud.igoldenbeta.hippo.bean.HippoRequest;
import cloud.igoldenbeta.hippo.bean.HippoResponse;
import cloud.igoldenbeta.hippo.govern.ServiceGovern;
import cloud.igoldenbeta.hippo.util.SerializationUtils;

/**
 * 客户端
 * 
 * @author sl
 *
 */
@Component
public class ZmqRpcClient {
  private static Logger log = LoggerFactory.getLogger(ZmqRpcClient.class);
  @Autowired
  private ServiceGovern serviceGovern;

  private int receiveTimeOut = 5000;

  public Object callService(HippoRequest request, String serviceName) throws Throwable {
    return process(request, serviceName);
  }

  private Object process(HippoRequest request, String serviceName) throws Throwable {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket socket = context.socket(ZMQ.REQ);
    String serviceAddress = serviceGovern.getServiceAddress(serviceName);

    String host = "tcp://" + serviceAddress;

    socket.setReceiveTimeOut(receiveTimeOut);
    try {
      socket.setIdentity(UUID.randomUUID().toString().getBytes());

      socket.connect(host);
      socket.send(SerializationUtils.serialize(request));
      byte[] rec = socket.recv();
      HippoResponse response = SerializationUtils.deserialize(rec, HippoResponse.class);
      if (response.isError()) {
        throw response.getThrowable();
      } else {
        return response.getResult();
      }
    } catch (Exception e) {
      log.error("callService error,request:" + ToStringBuilder.reflectionToString(request)
          + ",serviceName:" + serviceName, e);
      throw e;
    } finally {
      socket.close(); 
      context.term();
    }
  }

}
