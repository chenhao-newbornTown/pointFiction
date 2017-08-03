<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page pageEncoding="UTF-8"%>
<%@ include file="taglibsnew.jsp"%>
<%@ include file="glbVariable.jsp"%>
<%@ include file="easyUItag.jsp"%>

<html>
<head>
    <title></title>

    <link rel="stylesheet" type="text/css" href="<c:url value='/ps/style/comm.css'/>"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='${otherPath}/displaytag.css'/>" />

    <%--<style type="text/css">--%>
        <%--#mainCon{margin-top:100px}--%>
        <%--.thumbnail {position:relative;z-index:0;}--%>
        <%--.thumbnail img{border-width:0;padding:2px;position:absolute;background-color:#FFFFE0;left:0px;border:1px dashed gray;visibility:hidden;color:#000;text-decoration:none;padding:5px;}--%>
        <%--.thumbnail:hover img{visibility:visible;top:20px;left:0px;}--%>
    <%--</style>--%>

</head>


<script language=JavaScript>




function listReportRule() {
    var df=document.FictionBean;
    var url="/searchhotfictionlist";
    df.action=url;
    df.submit();
}

function getFictionDetail(fiction_id){
    window.parent.window.OpenPanel("<c:out value='${contextPath}'/>/getfictiondetail?fiction_id="+fiction_id,'920','830','小说内容');
}

function changeFictionStatus(fiction_id,fiction_status) {
    var line="";
    if(fiction_status=="1"){
        line="确定不再推荐?";
    }
    if(fiction_status=="2"){
        line="确定加入推荐?";
    }
    if (confirm(line)) {
        var df = document.FictionBean;
        var url = "/changefictionstatus?fiction_id_jsp=" + fiction_id + "&fiction_status_jsp=" + fiction_status;
        df.action = url;
        df.submit();
    }
}

function updateFictionDetail(fictionBean) {

    window.parent.window.OpenPanel("<c:out value='${contextPath}'/>/getfictiondetail?fiction_id="+fiction_id,'920','830','小说内容');
}

function delFiction(id) {
    if (confirm("确定要删除该小说么?")) {
        var df = document.FictionBean;
        var url = "/delfiction?id=" + id;
        df.action = url;
        df.submit();
    }

}

</script>


<body>


<div name="optionbar"  class="navbar">

    <form id="fictionBean" name="FictionBean" method="post" style="margin: 0">
        <table >
            <tr>

                <td>小说名称：</td>
                <td>
                    <input name="fiction_name" value="<c:out value='${fictionBean.fiction_name}'/>" style="width: 150px;"/>
                </td>

                <td>小说作者：</td>
                <td>
                    <input name="fiction_author_name" value="<c:out value='${fictionBean.fiction_author_name}'/>" style="width: 150px;"/>
                </td>

                <td>小说状态：</td>
                <td>
                    <select name="fiction_status" style="width: 150px">
                        <option value="999" selected="selected"></option>
                        <option value="0" <c:if test="${fictionBean.fiction_status==0}">selected="selected"</c:if>>未发布</option>
                        <option value="1" <c:if test="${fictionBean.fiction_status==1}">selected="selected"</c:if>>已发布</option>
                        <option value="2" <c:if test="${fictionBean.fiction_status==2}">selected="selected"</c:if>>热门</option>
                    </select>
                </td>

                <td>
                    <a href="#" class="easyui-linkbutton buttonclass2" data-options="iconCls:'icon-search'" style="height:20px;  width:80px;text-decoration: none;" name="ss" onclick="listReportRule();" >查询</a>&nbsp;
                    <a href="#" class="easyui-linkbutton buttonclass2"  data-options="iconCls:'icon-add'" onclick="onloadfiction(); " style=" height:20px;width:80px;text-decoration:none;">上传小说</a>
                </td>

            </tr>
        </table>
    </form>
</div>

<div align="center" style="width:100%;height: auto">
    <form name="logForm"  method="post" action="/searchhotfictionlist">

        <display:table name="fictionBeanList" cellspacing="0" cellpadding="0" style="width:100%"
                       requestURI="" id="fb"  pagesize="50"
                       class="list reportsList" >

            <display:column sortable="true" headerClass="sortable" style="width:5%" title="更新时间"><c:out value="${fb.update_time}"/></display:column>
            <display:column sortable="true" headerClass="sortable" style="width:10%" title="封面">
                <%--<a class="thumbnail" href="#">dsadasdasdasdasd <img src="http://www.ytoapp.com/1.gif"></a>--%>
                <%--<img src="http://www.ytoapp.com/1.gif>--%>
                <img src="http://www.ytoapp.com/<c:out value="${fb.fiction_pic_path}"/>" height="180px" width="140px">
            </display:column>
            <display:column sortable="true" headerClass="sortable" style="width:10%" title="故事名">
                <c:out value="${fb.fiction_name}"/>
            </display:column>
            <display:column sortable="true" headerClass="sortable" style="width:5%" title="作者名"><c:out value="${fb.fiction_author_name}"/></display:column>

                <display:column sortable="true" headerClass="sortable" style="width:8%" title="数据">阅读&nbsp;&nbsp;<c:out value="${fb.read_count}"/>&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;赞&nbsp;&nbsp;<c:out value="${fb.like_count}"/></display:column>



            <display:column sortable="true" headerClass="sortable" style="width:5%" title="状态">

                <c:if test="${fb.fiction_status==0}">
                    <span style="color: black;font-weight: bold;">未发布</span>
                </c:if>
                <c:if test="${fb.fiction_status==1}">
                    <span style="color: #8FBC8F;font-weight: bold;">已发布</span>
                </c:if>
                <c:if test="${fb.fiction_status==2}">
                    <span style="color: #DC143C;font-weight: bold;">热门</span>
                </c:if>
            </display:column>

            <display:column headerClass="sortable" style="width:10%" title="操作">

                <a style="text-decoration: none;" href="#" onclick="updateFictionDetail('<c:out value="${fb}"/>');" >编辑属性</a>&nbsp;&nbsp;
                <br>
                <a style="text-decoration: none;" href="#" onclick="getFictionDetail('<c:out value="${fb.fiction_id}"/>');" >查看小说</a>&nbsp;&nbsp;
                <c:if test="${fb.fiction_status==0}">
                    <a style="text-decoration: none;" href="#" onclick="delFiction('<c:out value="${fb.id}"/>');" >删除小说</a>&nbsp;&nbsp;
                </c:if>
                <c:if test="${fb.fiction_status==1}">
                    <a style="text-decoration: none;" href="#" onclick="changeFictionStatus('<c:out value="${fb.fiction_id}"/>','2');" >加入推荐</a>&nbsp;&nbsp;
                    <a style="text-decoration: none;" href="#" onclick="delFiction('<c:out value="${fb.id}"/>');" >删除小说</a>&nbsp;&nbsp;
                </c:if>
                <c:if test="${fb.fiction_status==2}">
                    <a style="text-decoration: none;" href="#" onclick="changeFictionStatus('<c:out value="${fb.fiction_id}"/>','1');" >不再推荐</a>&nbsp;&nbsp;
                </c:if>


            </display:column>

        </display:table>
    </form>
</div>

</body>

</html>
