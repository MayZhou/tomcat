/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.catalina;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;


/**
 * 该接口通过 {@link Valve} 访问日志记录，Tomcat 内部使用它来标识记录访问请求的阀门（Valve），
 *  以便仍可以将处理链中较早拒绝的请求添加到访问日志中。
 *  此接口的实现应该是可靠的, 以防止提供的请求{@link Request}和响应{@link Response}对象为空,
 *  具有 null 属性或任何其他奇怪的结果, 这些属性或可能是由于试图记录几乎肯定会被拒绝的请求
 *  而导致的, 因为它是格式错误。
 *
 * Intended for use by a {@link Valve} to indicate that the {@link Valve}
 * provides access logging. It is used by the Tomcat internals to identify a
 * Valve that logs access requests so requests that are rejected
 * earlier in the processing chain can still be added to the access log.
 * Implementations of this interface should be robust against the provided
 * {@link Request} and {@link Response} objects being null, having null
 * attributes or any other 'oddness' that may result from attempting to log
 * a request that was almost certainly rejected because it was mal-formed.
 */
public interface AccessLog {

    /**
     * 用于覆盖访问日志记录的远程地址的请求属性的名称。
     * Name of request attribute used to override the remote address recorded by
     * the AccessLog.
     */
    public static final String REMOTE_ADDR_ATTRIBUTE =
        "org.apache.catalina.AccessLog.RemoteAddr";

    /**
     * 用于覆盖 AccessLog 记录的远程主机名的请求属性的名称。
     * Name of request attribute used to override remote host name recorded by
     * the AccessLog.
     */
    public static final String REMOTE_HOST_ATTRIBUTE =
        "org.apache.catalina.AccessLog.RemoteHost";

    /**
     * 用于重写访问日志记录的协议的请求属性的名称。
     * Name of request attribute used to override the protocol recorded by the
     * AccessLog.
     */
    public static final String PROTOCOL_ATTRIBUTE =
        "org.apache.catalina.AccessLog.Protocol";

    /**
     * 用于覆盖访问日志记录的服务器端口的请求属性的名称。
     * Name of request attribute used to override the server port recorded by
     * the AccessLog.
     */
    public static final String SERVER_PORT_ATTRIBUTE =
        "org.apache.catalina.AccessLog.ServerPort";


    /**
     *
     * 使用指定的处理时间将请求/响应添加到访问日志中。
     * Add the request/response to the access log using the specified processing
     * time.
     *
     * @param request   Request (associated with the response) to log
     * @param response  Response (associated with the request) to log
     * @param time      Time taken to process the request/response in
     *                  milliseconds (use 0 if not known)
     */
    public void log(Request request, Response response, long time);

    /**
     * 此阀门是否应设置用于请求的 IP 地址、主机名、协议和端口的请求属性？
     * 这通常与附件 {@link org.apache.catalina.valves.AccessLogValve}会这里一起使用,
     * 否则将记录原始值。属性集包括:
     * <ul>
     * <li>org.apache.catalina.RemoteAddr</li>
     * <li>org.apache.catalina.RemoteHost</li>
     * <li>org.apache.catalina.Protocol</li>
     * <li>org.apache.catalina.ServerPost</li>
     * </ul>
     * Should this valve set request attributes for IP address, hostname,
     * protocol and port used for the request? This are typically used in
     * conjunction with the {@link org.apache.catalina.valves.AccessLogValve}
     * which will otherwise log the original values.
     *
     * The attributes set are:
     * <ul>
     * <li>org.apache.catalina.RemoteAddr</li>
     * <li>org.apache.catalina.RemoteHost</li>
     * <li>org.apache.catalina.Protocol</li>
     * <li>org.apache.catalina.ServerPost</li>
     * </ul>
     *
     * @param requestAttributesEnabled  <code>true</code> causes the attributes
     *                                  to be set, <code>false</code> disables
     *                                  the setting of the attributes.
     */
    public void setRequestAttributesEnabled(boolean requestAttributesEnabled);

    /**
     * @see #setRequestAttributesEnabled(boolean)
     * @return <code>true</code> if the attributes will be logged, otherwise
     *         <code>false</code>
     */
    public boolean getRequestAttributesEnabled();
}
