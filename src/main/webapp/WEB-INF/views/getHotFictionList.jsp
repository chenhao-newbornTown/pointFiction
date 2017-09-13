<%@ page pageEncoding="UTF-8" %>
<%@ include file="taglibsnew.jsp" %>
<%@ include file="glbVariable.jsp" %>
<%@ include file="easyUItag.jsp" %>

<html>
<head>
    <title></title>

    <link rel="stylesheet" type="text/css" href="<c:url value='/ps/style/comm.css'/>"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='${otherPath}/displaytag.css'/>"/>

</head>


<script language=JavaScript>


    function listReportRule() {

        var fictionBeanListSize = document.getElementById("fictionBeanListSize").value;
        var df = document.FictionBean;
        var url = "/searchhotfictionlist?fictionBeanListSize=" + fictionBeanListSize;
        df.action = url;
        df.submit();
    }

    function getFictionDetail(fiction_id) {
        window.parent.window.OpenPanel("<c:out value='${contextPath}'/>/getfictiondetail?fiction_id=" + fiction_id, '920', '630', '小说内容');
    }

    function changeFictionStatus(fiction_id, fiction_status) {
        var line = "";
        if (fiction_status == "1") {
            line = "确定不再推荐?";
        }
        if (fiction_status == "2") {
            line = "确定加入推荐?";
        }
        if (confirm(line)) {
            var df = document.FictionBean;
            var url = "/changefictionstatus?fiction_id_jsp=" + fiction_id + "&fiction_status_jsp=" + fiction_status;
            df.action = url;
            df.submit();
        }
    }

    function updateFictionDetail(id, fiction_id, fiction_pic_path, fiction_author_name, fiction_name) {

        window.parent.window.OpenPanel("<c:out value='${contextPath}'/>/updatefictiondetail?id=" + id + "&fiction_id=" + fiction_id + "&fiction_pic_path=" + fiction_pic_path + "&fiction_author_name=" + fiction_author_name + "&fiction_name=" + fiction_name, '920', '530', '编辑小说属性');
    }

    function delFiction(id, fiction_id, fiction_pic_path, type, uid, fiction_author_id) {
        var line = "";
        if (type == "pic") {
            line = "确定要删除该小说图片么?";
        }
        if (type == "fiction") {
            line = "确定要删除该小说么?";
        }
        if (confirm(line)) {
            var df = document.FictionBean;

            var del_status = false;

            if (uid == fiction_author_id) {
                del_status = true;
            }

            var url = "/delfiction?id=" + id + "&fiction_id=" + fiction_id + "&fiction_pic_path=" + fiction_pic_path + "&type=" + type + "&del_status=" + del_status;
            df.action = url;
            df.submit();
        }

    }



</script>


<body>


<div name="optionbar" class="navbar">

    <form id="fictionBean" name="FictionBean" method="post" style="margin: 0">
        <table>
            <tr>

                <td>小说名称：</td>
                <td>
                    <input name="fiction_name" value="<c:out value='${fictionBean.fiction_name}'/>"
                           style="width: 150px;"/>
                </td>

                <td>小说作者：</td>
                <td>
                    <input name="fiction_author_name" value="<c:out value='${fictionBean.fiction_author_name}'/>"
                           style="width: 150px;"/>
                </td>

                <td>小说状态：</td>
                <td>
                    <select name="fiction_status" style="width: 150px">
                        <option value="999" selected="selected">全部</option>
                        <option value="0" <c:if test="${fictionBean.fiction_status==0}">selected="selected"</c:if>>未发布
                        </option>
                        <option value="1" <c:if test="${fictionBean.fiction_status==1}">selected="selected"</c:if>>已发布
                        </option>
                        <option value="2" <c:if test="${fictionBean.fiction_status==2}">selected="selected"</c:if>>热门
                        </option>
                    </select>
                </td>

                <td>
                    <a href="#" class="easyui-linkbutton buttonclass2" data-options="iconCls:'icon-search'"
                       style="height:20px;  width:80px;text-decoration: none;" name="ss"
                       onclick="listReportRule();">查询</a>&nbsp;
                </td>
                <td colspan="5" align="center">共有<c:out value="${fictionBeanListSize}"/>个故事</td>
                <input id="fictionBeanListSize" value="<c:out value="${fictionBeanListSize}"/>" type="hidden"/>
            </tr>
        </table>
    </form>
</div>

<div align="center" style="width:100%;height: auto">
    <form name="logForm" method="post" action="/searchhotfictionlist">

        <display:table name="fictionBeanList" cellspacing="0" cellpadding="0" style="width:100%"
                       requestURI="" id="fb" pagesize="10"
                       class="list reportsList">

            <display:column style="width:5%" title="更新时间" >

                <c:out value="${fb.update_date}"/>
            </display:column>
            <display:column style="width:10%" title="封面">
                <img src="<c:out value="${baseurl}"/>/<c:out value="${fb.fiction_pic_path}"/>" height="205px"
                     width="115px">
            </display:column>
            <display:column style="width:10%" title="故事名">
                <c:out value="${fb.fiction_name}"/>
            </display:column>
            <display:column style="width:5%" title="作者名"><c:out
                    value="${fb.fiction_author_name}"/></display:column>

            <display:column style="width:8%" title="数据">阅读&nbsp;&nbsp;<c:out
                    value="${fb.read_count}"/>&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;赞&nbsp;&nbsp;<c:out
                    value="${fb.like_count}"/></display:column>


            <display:column style="width:5%" title="状态">

                <c:if test="${fb.fiction_status==0}">
                    <span style="color: black;font-weight: bold;">未发布</span>
                </c:if>
                <c:if test="${fb.fiction_status==1}">
                    <span style="color: #8FBC8F;font-weight: bold;">已发布</span>
                </c:if>
                <c:if test="${fb.fiction_status==2}">
                    <span style="color: #DC143C;font-weight: bold;">热门</span>
                </c:if>
                <br>
                <c:if test="${fb.status=='100001'}">
                    <span style="color: #DC143C;font-weight: bold;">图片违规</span>
                </c:if>
                <c:if test="${fb.status=='100002'}">
                    <span style="color: #DC143C;font-weight: bold;">故事违规</span>
                </c:if>
            </display:column>

            <display:column headerClass="sortable" style="width:10%" title="操作">

                <c:if test="${fb.status=='100001'}">
                    <a style="text-decoration: none;" href="#"
                       onclick="delFiction('<c:out value="${fb.id}"/>','<c:out value="${fb.fiction_id}"/>','<c:out
                               value="${fb.fiction_pic_path}"/>','fiction');">删除小说</a>&nbsp;&nbsp;
                    <br>
                    <a style="text-decoration: none;" href="#"
                       onclick="getFictionDetail('<c:out value="${fb.fiction_id}"/>');">查看小说</a>&nbsp;&nbsp;

                </c:if>

                <c:if test="${fb.status=='000000'}">

                    <c:if test="${uid == fb.fiction_author_id}">
                        <a style="text-decoration: none;" href="#"
                           onclick="updateFictionDetail('<c:out value="${fb.id}"/>','<c:out value="${fb.fiction_id}"/>',
                                   '<c:out value="${fb.fiction_pic_path}"/>','<c:out
                                   value="${fb.fiction_author_name}"/>'
                                   ,'<c:out value="${fb.fiction_name}"/>');">编辑信息</a>&nbsp;&nbsp;
                        <br>
                    </c:if>
                    <%--<c:if test="${fb.fiction_status==0}">--%>
                        <%--<a style="text-decoration: none;" href="#"--%>
                           <%--onclick="delFiction('<c:out value="${fb.id}"/>','<c:out value="${fb.fiction_id}"/>','<c:out--%>
                                   <%--value="${fb.fiction_pic_path}"/>','fiction','<c:out value="${uid}"/>','<c:out--%>
                                   <%--value="${fb.fiction_author_id}"/>');">删除小说</a>&nbsp;&nbsp;--%>
                        <%--<br>--%>
                    <%--</c:if>--%>

                    <c:if test="${fb.fiction_status==1||fb.fiction_status==0}">

                        <a style="text-decoration: none;" href="#"
                           onclick="delFiction('<c:out value="${fb.id}"/>','<c:out value="${fb.fiction_id}"/>','<c:out
                                   value="${fb.fiction_pic_path}"/>','pic');">删除封面</a>&nbsp;&nbsp;
                        <br>
                        <a style="text-decoration: none;" href="#"
                           onclick="delFiction('<c:out value="${fb.id}"/>','<c:out value="${fb.fiction_id}"/>','<c:out
                                   value="${fb.fiction_pic_path}"/>','fiction','<c:out value="${uid}"/>','<c:out
                                   value="${fb.fiction_author_id}"/>' );">删除小说</a>&nbsp;&nbsp;
                        <br>
                        <a style="text-decoration: none;" href="#"
                           onclick="changeFictionStatus('<c:out value="${fb.fiction_id}"/>','2');">加入推荐</a>&nbsp;&nbsp;
                        <br>
                    </c:if>

                    <a style="text-decoration: none;" href="#"
                       onclick="getFictionDetail('<c:out value="${fb.fiction_id}"/>');">查看小说</a>&nbsp;&nbsp;
                    <c:if test="${fb.fiction_status==2}">
                        <br>
                        <a style="text-decoration: none;" href="#"
                           onclick="changeFictionStatus('<c:out value="${fb.fiction_id}"/>','1');">不再推荐</a>&nbsp;&nbsp;
                    </c:if>
                </c:if>
            </display:column>

        </display:table>
    </form>
</div>

</body>

</html>
