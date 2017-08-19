<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page pageEncoding="UTF-8" %>
<%@ include file="taglibs.jsp" %>
<%@ include file="glbVariable.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>dianDian</title>
    <%--<script language="javascript" type="text/javascript" src="<c:url value='${util}/My97DatePicker/WdatePicker.js'/>"></script>
    --%>
    <link href="<c:url value='/ps/login_files/styles.css'/>" type="text/css" media="screen" rel="stylesheet"/>
    <style type="text/css">
        HTML {
            height: 100%
        }

        body {
            height: 100%
        }

        img, div {
            behavior: url(iepngfix.htc)
        }

    </style>

    <script language=JavaScript>
        function alertMesg() {

            var msg = document.getElementById("msg").value;

            if (msg != "") {
                alert(msg);
            }
        }
    </script>
</head>


<body style="background:#9FB6CD" onload="alertMesg()">

<input id="msg" value="<c:out value="${msg}"/>" type="hidden">

<div style="background:#1ea4d2;height:30px;text-align:left;line-height:30px;padding-left:10px;color:#ffffff">
    dianDianPoint
</div>
<div id="login">
    <div id="wrappertop">

    </div>
    <div id="wrapper">
        <div id="content">
            <div id="header" style="height:80px">
                </div>

            <div id="darkbannerwrap">
            </div>
            <form method="post" name="loginForm">
                <fieldset class="form">
                    <p>
                        <label for="user_name">用户名：</label>
                        <input name="username" id="username" type="text" style="width: 200;height: 25">
                    </p>
                    <p>
                        <label for="user_password">密码：</label>
                        <input name="userPassword" id="userPassword" type="password" style="width: 200;height: 25">
                    </p>
                    <button type="button" class="positive" name="Submit" onclick="login();">
                        <img src="<c:url value='/ps/login_files/key.png'/>" alt="登录"/>
                        登录
                    </button>

                </fieldset>


            </form>
        </div>
    </div>
</div>
</body>
<script LANGUAGE='JavaScript'>
    function login() {


        var u = document.getElementById("username").value;
        var p = document.getElementById("userPassword").value;
        document.loginForm.action = "/main";
        document.loginForm.submit();
    }

</script>
</html>


