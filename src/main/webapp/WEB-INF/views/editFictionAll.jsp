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

    <%--<script type="text/javascript" src="<c:url value="${eaUIPath}/jquery.min.js"/>"/>--%>

    <%--<style type="text/css">--%>
    <%--#mainCon{margin-top:100px}--%>
    <%--.thumbnail {position:relative;z-index:0;}--%>
    <%--.thumbnail img{border-width:0;padding:2px;position:absolute;background-color:#FFFFE0;left:0px;border:1px dashed gray;visibility:hidden;color:#000;text-decoration:none;padding:5px;}--%>
    <%--.thumbnail:hover img{visibility:visible;top:20px;left:0px;}--%>
    <%--</style>--%>

</head>


<script language=JavaScript>

    function PreviewImage(imgFile) {
        var filextension = imgFile.value.substring(imgFile.value.lastIndexOf("."), imgFile.value.length);
        filextension = filextension.toLowerCase();
        if ((filextension != '.jpg') && (filextension != '.gif') && (filextension != '.jpeg') && (filextension != '.png') && (filextension != '.bmp')) {
            alert("对不起，系统仅支持标准格式的照片，请您调整格式后重新上传，谢谢 !");
            imgFile.focus();
        }
        else {
            var path;

            if (document.all) {

                imgFile.select();
                path = document.selection.createRange().text;
                document.getElementById("imgPreview").innerHTML = "";
                document.getElementById("imgPreview").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + path + "\")";//使用滤镜效果
            }
            else {
                path = window.URL.createObjectURL(imgFile.files[0]);
                document.getElementById("imgPreview").innerHTML = "<img id='img1' width='115px' height='205px' src='" + path + "'/>";
                document.getElementById("img1").src = path;
            }
        }
    }

    function createfiction() {

        var fiction_pic_path = document.getElementById("fiction_pic_path").value;
        var fiction_pic_path_temp = document.getElementById("fiction_pic_path_temp").value;
        if (fiction_pic_path == "" && fiction_pic_path_temp == "") {
            alert("请选择要上传的图片!!!");
            return false;
        }

        var fiction_name = document.getElementById("fiction_name").value;
        if (fiction_name == "") {
            alert("请输入故事名!!!");
            return false;
        }
        var fiction_author_name = document.getElementById("fiction_author_name").value;
        if (fiction_author_name == "") {
            alert("请输入作者名!!!");
            return false;
        }

        var addOrUpdate = document.getElementById("addOrUpdate").value;

        var fiction_detail_path = document.getElementById("fiction_detail_path").value;

        if (addOrUpdate == "add") {
            if (fiction_detail_path == "") {
                alert("请选择要上传的故事!!!");
                return false;
            }
        }

        if (fiction_detail_path != "") {

            var fiction_detail_type = fiction_detail_path.substring(fiction_detail_path.lastIndexOf("."), fiction_detail_path.length);
            fiction_detail_type = fiction_detail_type.toLowerCase();
            if (fiction_detail_type != '.xlsx') {
                alert("对不起，系统仅支持标准格式的excel，请您重新上传，谢谢 !");
                return false;
            }
        }

        var df = document.FictionAll;

        var url = "";
        if (addOrUpdate == "add") {
            url = "/uploadpicandfiction";
        } else{
            var fiction_id = document.getElementById("fiction_id").value;
            url = "/updatepicandfiction?fiction_id=" + fiction_id;
        }
        df.action = url;
        df.submit();
    }

    function alertMesg() {

        var msg = document.getElementById("msg").value;
        if (msg != "") {
            alert(msg);
        }
    }



</script>


<body onload="alertMesg();">


<div name="optionbar" class="navbar">

    <input id="addOrUpdate" value="<c:out value="${addOrUpdate}"/>" type="hidden">
    <input id="msg" value="<c:out value="${msg}"/>" type="hidden">

    <form id="fictionBean" name="FictionAll" method="post" style="margin: 0" enctype="multipart/form-data">

        <input id="fiction_id" value="<c:out value="${fictionBean.fiction_id}"/>" type="hidden">
        <table align="center">
            <tr>
                <td>封面：</td>
                <td height="285px">
                    <div id="imgPreview">
                        <c:if test="${addOrUpdate=='update'}">
                            <img src="<c:out value="${baseurl}"/>/<c:out value='${fictionBean.fiction_pic_path}'/>" width='115px' height='205px'>
                        </c:if>
                    </div>
                </td>
            </tr>
            <tr>
                <td>上传图片：</td>
                <td>
                    <input name="file" type="file" value="<c:out value='${fictionBean.fiction_pic_path}'/>"
                           onchange="PreviewImage(this)" id="fiction_pic_path" style="width: 250px;"/>
                    <input id="fiction_pic_path_temp" value="<c:out value="${fictionBean.fiction_pic_path}"/>"
                           type="hidden">
                </td>
            </tr>
            <tr/>
            <tr>
                <td>作者：</td>
                <td>
                    <input name="fiction_author_name" id="fiction_author_name"
                           value="<c:out value='${fictionBean.fiction_author_name}'/>"
                           style="width: 250px;"/>
                </td>
            </tr>
            <tr/>
            <tr>
                <td>故事名：</td>
                <td>
                    <input name="fiction_name" id="fiction_name" value="<c:out value='${fictionBean.fiction_name}'/>"
                           style="width: 250px;"/>
                </td>
            </tr>
            <%--<tr>--%>
            <%--<td colspan="2" align="center">--%>
            <%--<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" id="ButtonAdd">更新小说</a>--%>
            <%--<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'" id="ButtonDel">不更新小说</a>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <tr id="uploadexcel">
                <td>故事地址：</td>
                <td>
                    <input name="file" id="fiction_detail_path" type="file" style="width: 250px;"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <a href="#" class="easyui-linkbutton buttonclass2" data-options="iconCls:'icon-add'"
                       onclick="createfiction(); " style=" height:20px;width:180px;text-decoration:none;">完成,并创建</a>
                </td>
            </tr>
        </table>
    </form>
</div>

</body>

</html>
