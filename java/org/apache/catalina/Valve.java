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


import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;


/**
 * 与特定容器相关联的请求处理组件
 * <p>A <b>Valve</b> is a request processing component associated with a
 * particular Container.  A series of（一系列的） Valves are generally associated with
 * each other into a Pipeline.  The detailed contract for a Valve is included
 * in the description of the <code>invoke()</code> method below.</p>
 *
 * <b>HISTORICAL NOTE</b>:  The "Valve" name was assigned to this concept
 * because a valve is what you use in a real world pipeline to control and/or
 * modify flows through it.
 * 历史笔记: "阀门" 的名称被分配给这个概念, 因为阀门是你在现实世界的管道中用来控制和/或修改流经它的阀门。
 * @author Craig R. McClanahan
 * @author Gunnar Rjnning
 * @author Peter Donald
 */
public interface Valve {


    //-------------------------------------------------------------- Properties

    /**
     * 得到下一个阀门
     * Return the next Valve in the pipeline containing this Valve, if any.
     */
    public Valve getNext();


    /**
     * Set the next Valve in the pipeline containing this Valve.
     *
     * @param valve The new next valve, or <code>null</code> if none
     */
    public void setNext(Valve valve);


    //---------------------------------------------------------- Public Methods


    /**
     * 执行定期任务, 如重新加载等。此方法将在此容器的类加载上下文中调用。意外的拟项目将被捕获并记录。
     * Execute a periodic task, such as reloading, etc. This method will be
     * invoked inside the classloading context of this container. Unexpected
     * throwables will be caught and logged.
     */
    public void backgroundProcess();


    /**
     * <p>Perform request processing as required by this Valve.根据此阀门的要求执行请求处理。</p>
     *
     * <p>An individual Valve <b>MAY</b> perform the following actions, in
     * the specified order:</p>
     * 单个阀门可能会按指定的顺序执行以下操作:
     * <ul>
     * <li>Examine and/or modify the properties of the specified Request and
     *     Response.检查和修改指定的请求和响应的属性。
     * <li>Examine the properties of the specified Request, completely generate
     *     the corresponding Response, and return control to the caller.检查指定请求的属性, 完全生成相应的响应, 并将控件返回给调用方。
     * <li>Examine the properties of the specified Request and Response, wrap
     *     either or both of these objects to supplement their functionality,
     *     and pass them on.
     *     检查指定的 "请求和响应" 的属性, 包装这两个对象中的一个或两个以补充其功能, 并将其传递给它们。
     * <li>If the corresponding Response was not generated (and control was not
     *     returned, call the next Valve in the pipeline (if there is one) by
     *     executing <code>getNext().invoke()</code>.
     *     如果未生成相应的响应 (并且未返回控件, 则通过执行 getNext (). call () 调用管道中的(如果有)下一个阀门 。
     * <li>Examine, but not modify, the properties of the resulting Response
     *     (which was created by a subsequently invoked Valve or Container).
     *     检查而不是修改生成的响应的属性 (由随后调用的阀门或容器创建)。
     * </ul>
     *
     * <p>A Valve <b>MUST NOT</b> do any of the following things:</p>
     * 阀门不能执行以下任何操作:
     * <ul>
     * <li>Change request properties that have already been used to direct
     *     the flow of processing control for this request (for instance,
     *     trying to change the virtual host to which a Request should be
     *     sent from a pipeline attached to a Host or Context in the
     *     standard implementation).
     *     更改已用于指导此请求的处理控件流的请求属性 (例如, 尝试更改请求应从连接到标准中的主机
     *     或上下文的管道发送到的虚拟主机)实施)。
     * <li>Create a completed Response <strong>AND</strong> pass this
     *     Request and Response on to the next Valve in the pipeline.
     *     创建已完成的响应, 并将此请求和响应传递到管道中的下一个阀门。
     * <li>Consume bytes from the input stream associated with the Request,
     *     unless it is completely generating the response, or wrapping the
     *     request before passing it on.
     *     从与请求关联的输入流中使用字节, 除非它完全生成响应, 或在传递请求之前对其进行包装。
     * <li>Modify the HTTP headers included with the Response after the
     *     <code>getNext().invoke()</code> method has returned.
     *     在 getNext().invoke() 方法返回后, 修改响应中包含的 HTTP 标头。
     * <li>Perform any actions on the output stream associated with the
     *     specified Response after the <code>getNext().invoke()</code> method has
     *     returned.
     *     在返回 "getNext().invoke()" 方法后, 对与指定响应关联的输出流执行任何操作。
     * </ul>
     *
     * @param request The servlet request to be processed
     * @param response The servlet response to be created
     *
     * @exception IOException if an input/output error occurs, or is thrown
     *  by a subsequently invoked Valve, Filter, or Servlet
     * @exception ServletException if a servlet error occurs, or is thrown
     *  by a subsequently invoked Valve, Filter, or Servlet
     */
    public void invoke(Request request, Response response)
        throws IOException, ServletException;


    /**
     * 貌似是长连接或者服务器推（待考证）
     * Process a Comet event.
     *
     * @param request The servlet request to be processed
     * @param response The servlet response to be created
     *
     * @exception IOException if an input/output error occurs, or is thrown
     *  by a subsequently invoked Valve, Filter, or Servlet
     * @exception ServletException if a servlet error occurs, or is thrown
     *  by a subsequently invoked Valve, Filter, or Servlet
     */
    public void event(Request request, Response response, CometEvent event)
        throws IOException, ServletException;


    public boolean isAsyncSupported();


}
