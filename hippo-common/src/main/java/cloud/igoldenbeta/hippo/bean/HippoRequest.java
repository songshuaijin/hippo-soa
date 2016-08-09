package cloud.igoldenbeta.hippo.bean;

import java.io.Serializable;
import java.util.Arrays;


/**
 * 请求包装类
 * 
 * @author sl
 *
 */
public class HippoRequest implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1011251787575609560L;
  private String requestId;
  private String className;
  private String methodName;
  private Class<?>[] parameterTypes;
  private Object[] parameters;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Class<?>[] getParameterTypes() {
    return parameterTypes;
  }

  public void setParameterTypes(Class<?>[] parameterTypes) {
    this.parameterTypes = parameterTypes;
  }

  public Object[] getParameters() {
    return parameters;
  }

  public void setParameters(Object[] parameters) {
    this.parameters = parameters;
  }

  @Override
  public String toString() {
    return "HippoRequest{" + "requestId='" + requestId + '\'' + ", className='" + className + '\''
        + ", methodName='" + methodName + '\'' + ", parameterTypes="
        + Arrays.toString(parameterTypes) + ", parameters=" + Arrays.toString(parameters) + '}';
  }
}
