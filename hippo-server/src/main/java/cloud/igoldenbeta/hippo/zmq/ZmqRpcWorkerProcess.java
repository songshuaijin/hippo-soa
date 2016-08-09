package cloud.igoldenbeta.hippo.zmq;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import cloud.igoldenbeta.hippo.bean.HippoRequest;
import cloud.igoldenbeta.hippo.bean.HippoResponse;
import cloud.igoldenbeta.hippo.server.HippoServiceImplCache;
import cloud.igoldenbeta.hippo.util.SerializationUtils;

/**
 * zmq rpc worker 处理类
 * 
 * @author sl
 *
 */
public class ZmqRpcWorkerProcess {
  private static final Logger log = LoggerFactory.getLogger(ZmqRpcWorkerProcess.class);

  public void process(ZMQ.Socket socket) {
    ZMsg msg = ZMsg.recvMsg(socket);
    msg.addLast(process(msg.removeLast()));
    msg.send(socket); // 将数据发送回去
  }

  private ZFrame process(ZFrame request) {
    HippoRequest paras = null;
    try {
      paras = SerializationUtils.deserialize(request.getData(), HippoRequest.class);
      Object serviceBean = HippoServiceImplCache.INSTANCE.getHandlerMap().get(paras.getClassName());
      FastClass serviceFastClass = FastClass.create(serviceBean.getClass());
      FastMethod serviceFastMethod =
          serviceFastClass.getMethod(paras.getMethodName(), paras.getParameterTypes());
      Object responseDto = serviceFastMethod.invoke(serviceBean, paras.getParameters());
      HippoResponse response = new HippoResponse();
      response.setRequestId(paras.getRequestId());
      response.setResult(responseDto);
      return new ZFrame(SerializationUtils.serialize(response));
    } catch (Exception e1) {
      HippoResponse response = new HippoResponse();
      response.setThrowable(e1);
      response.setError(true);
      log.error("process error: request:" + ToStringBuilder.reflectionToString(paras) + "&respose:"
          + ToStringBuilder.reflectionToString(response), e1);
      return new ZFrame(SerializationUtils.serialize(response));
    }
  }
}
