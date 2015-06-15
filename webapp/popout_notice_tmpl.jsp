<%@ page language="java" pageEncoding="utf-8"%>

<!-- 矩阵更新信息提示模版 -->
<p style="display:none"><textarea id="showname"><!--
 <div style='font-size:13px;padding-top:3px;'>矩阵信息有更新：{$T.name} <a title="矩阵最新更新信息"
href='<%=request.getContextPath()%>/hg/matrix/matrixchanging.jsp?changingactionsid={$T.changingactionsid}'>查看详情</a></div>
--></textarea></p>

<!-- 矩阵贡献奖品信息提示模版 -->
<p style="display:none"><textarea id="giftplan"><!--
 <div style='font-size:13px;padding-top:3px;'>矩阵信息有更新：{$T.name} <a title="矩阵最新更新信息"
href='<%=request.getContextPath()%>/hg/matrix/matrixchanging.jsp?changingactionsid={$T.changingactionsid}'>查看详情</a></div>
--></textarea></p>
