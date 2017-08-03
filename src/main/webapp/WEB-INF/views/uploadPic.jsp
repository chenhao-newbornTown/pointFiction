<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page pageEncoding="UTF-8" %>
<%@ include file="taglibsnew.jsp" %>
<%@ include file="glbVariable.jsp" %>
<%@ include file="easyUItag.jsp" %>

<html>
<head>
    <title></title>

    <link rel="stylesheet" type="text/css" href="<c:url value='/ps/style/comm.css'/>"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='${otherPath}/displaytag.css'/>"/>

    <%--<style type="text/css">--%>
    <%--#mainCon{margin-top:100px}--%>
    <%--.thumbnail {position:relative;z-index:0;}--%>
    <%--.thumbnail img{border-width:0;padding:2px;position:absolute;background-color:#FFFFE0;left:0px;border:1px dashed gray;visibility:hidden;color:#000;text-decoration:none;padding:5px;}--%>
    <%--.thumbnail:hover img{visibility:visible;top:20px;left:0px;}--%>
    <%--</style>--%>

</head>


<script language=JavaScript>

    function uploadPic() {

        var file = document.getElementsByName("file")[0].value;


        if (file.length > 0) {
            var df = document.picFile;
            var url = "/batchupload";
            df.action = url;
            df.submit();
        }else {
            alert("请选择要上传的图片!!!")
        }


    }


    function delPic(id) {
        if (confirm("确定要删除该图片么?")) {
            var df = document.picFile;
            var url = "/delpic?id=" + id;
            df.action = url;
            df.submit();
        }

    }

</script>


<body>


<div name="optionbar" class="navbar">

    <form id="picFile" name="picFile" method="post" style="margin: 0" enctype="multipart/form-data"
          action="/batchupload">
        <table>
            <tr>

                <td>文件：</td>
                <td>
                    <input name="file" id="file" type="file" style="width: 600px;"/>
                </td>
                <td>
                    <a href="#" class="easyui-linkbutton buttonclass2" data-options="iconCls:'icon-add'" onclick="uploadPic();" style="height:20px;width:180px;text-decoration:none;">上传图片</a>
                </td>

            </tr>
        </table>
    </form>
</div>

<div align="center" style="width:100%;height: auto">
    <form name="picForm" method="post" action="/batchupload">

        <display:table name="picBeanMongoList" cellspacing="0" cellpadding="0" style="width:100%"
                       requestURI="" id="pic" pagesize="30"
                       class="list reportsList">
            <display:column sortable="true" headerClass="sortable" style="width:10%" title="上传时间"><c:out
                    value="${pic.pic_upload_time}"/></display:column>
            <display:column sortable="true" headerClass="sortable" style="width:10%" title="封面">
                <img src="http://www.ytoapp.com/<c:out value="${pic.pic_name}"/>" height="180px" width="140px">
            </display:column>
            <display:column sortable="true" headerClass="sortable" style="width:10%" title="使用次数"><c:out
                    value="${pic.use_pic_num}"/></display:column>


            <display:column headerClass="sortable" style="width:10%" title="操作">
                <a style="text-decoration: none;" href="#" onclick="delPic('<c:out value="${pic.id}"/>');">删除</a>&nbsp;&nbsp;
            </display:column>

        </display:table>
    </form>
</div>

</body>

</html>
