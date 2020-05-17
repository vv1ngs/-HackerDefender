<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>xtermjs</title>
    <link rel="stylesheet" href="/webjars/xterm/2.9.2/dist/xterm.css"/>
    <script type="application/javascript" src="/webjars/xterm/2.9.2/dist/xterm.js"></script>
    <script type="application/javascript" src="/webjars/xterm/2.9.2/dist/addons/attach/attach.js"></script>

</head>
<body>
<div style="width:1000px;" id="xterm"></div>
<script type="application/javascript">
    var term = new Terminal({
        cursorBlink: false,
        cols: 100,
        rows: 50
    });

    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return decodeURI(r[2]);
        }
        return null;
    }

    term.open(document.getElementById('xterm'));
    var socket = new WebSocket('ws://localhost:8080/ws/container/exec.do?containerId=' + getUrlParam("containerId"));
    term.attach(socket);
    term.focus();
</script>
</body>
</html>