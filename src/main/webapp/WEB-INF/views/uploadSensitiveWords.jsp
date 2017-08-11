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

        var fiction_words_path = document.getElementById("fiction_words_path").value;
        if (fiction_words_path == "") {
            alert("请选择要上传的敏感词Excel!!!");
            return false;
        }

        var fiction_words_type = fiction_words_path.substring(fiction_words_path.lastIndexOf("."), fiction_words_path.length);
        fiction_words_type = fiction_words_type.toLowerCase();
        if (fiction_words_type != '.xlsx') {
            alert("对不起，系统仅支持标准格式的excel，请您重新上传，谢谢 !");
        }
        var df = document.wordsFile;
        var url = "/uploadwords";
        df.action = url;
        df.submit();

    }

</script>


<body>


<div name="optionbar" class="navbar">

    <form id="sensitivewordsbean" name="wordsFile" method="post" style="margin: 0" enctype="multipart/form-data">
        <table align="center">
            <tr>

                <td>文件：</td>
                <td>
                    <input name="file" id="fiction_words_path" type="file" style="width: 600px;"/>
                </td>
            </tr>
                <tr>
                <td colspan="3">
                    <a href="#" class="easyui-linkbutton buttonclass2" data-options="iconCls:'icon-add'" onclick="uploadPic();" style="height:20px;width:180px;text-decoration:none;">上传敏感词文件</a>
                </td>
                </tr>
            <tr>
                <td>最后更新时间：</td>
                <td>
                    <c:out value="${sensitivewordsbean.update_time}"/>
                </td>

            </tr>
        </table>
    </form>
</div>

</body>

</html>
