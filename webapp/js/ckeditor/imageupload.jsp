<%@ page language="java" pageEncoding="utf-8"%>
<style>
html,body{padding:0;margin:0;font-size:12px;}
</style>
<script src="${pageContext.request.contextPath}/js/jquery-1.6.2.min.js"></script>
<script>
parent.$(".cke_dialog_ui_labeled_label:contains(labeldyuanwenjian)").next().find("input")
.val('<%=request.getContextPath()%>/upload/UploadController/getSingleImageData.vot?imagesid=<%=request.getParameter("sid")%>');
</script>
<span style="line-height: 25px;">上传成功！</span>