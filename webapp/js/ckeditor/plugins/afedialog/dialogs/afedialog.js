CKEDITOR.dialog.add( 'afeDialog', function( editor ) {
    return {
        title: "公式编辑器",
        resizable:	CKEDITOR.DIALOG_RESIZE_NONE,
        minWidth: 475,
        minHeight: 500,
        contents: [
            {
                id: 'tab-basic',
                label: 'Editor',
                elements: [
                    {
                    	//mathquill-editable �ɱ༭
                    	//mathquill-rendered-math �Ѿ���Ⱦ
                    	//mathquill-embedded-latex ֻ����̬��Ⱦ
                        type: 'html',
                        //CKEDITOR.basePath
                        //jmeditor1.0/ckeditor/plugins/jme/dialogs/mathdialog.html
                        html: '<div id="formulamath" style="width:470px;height:500px;"><iframe id="math_frame" style="width:470px;height:500px;" frameborder="no" src="' + CKEDITOR.basePath + 'plugins/afedialog/dialogs/mathdialog.jsp"></iframe></div>'
                    }   
                ]
            }
        ],
        onShow : function(){
        	//getIFrameDOM("math_frame").getLatex();
        	if (document.getElementById("math_frame").contentWindow['reloadAntelopeFormulaEditor']) {
        		document.getElementById("math_frame").contentWindow['reloadAntelopeFormulaEditor']();
        	}
        	//$("#formulamath", getIFrameDOM("math_frame")).getLatex();
        },
        onOk: function() {
        	//alert("testff");
        	var latext = document.getElementById("math_frame").contentWindow['getLatex']();
        	
        	$.ajaxSettings.async = false;
        	var latextsid = "";
        	$.post(ctx + "/common/AfeUtilController/addNewLatex.vot", "latex=" + encodeURIComponent(encodeURIComponent(latext)), function(data){
        		latextsid = data;
        	});
        	$.ajaxSettings.async = true;
        	
        	//alert(latextsid);
        	//alert(getIFrameDOM("math_frame"));
        	//getIFrameDOM("math_frame").getLatex();
        	//alert("fff");
        	/*
        	var thedoc = document.frames ? document.frames('math_frame').document : getIFrameDOM("math_frame");
        	var mathHTML = '<span class="mathquill-rendered-math" style="font-size:' + JMEditor.defaultFontSize + ';" >' + $("#jme-math",thedoc).html() + '</span><span>&nbsp;</span>';
            editor.insertHtml(mathHTML);
			return;*/
        	
        	editor.insertHtml("<img src='" + ctx + "/common/AfeUtilController/getLatexImgData.vot?sid=" + latextsid + "'/>");
        }
    };
});

function getIFrameDOM(fid){
	var fm = getIFrame(fid);
	return fm.document||fm.contentDocument;
}
function getIFrame(fid){
	return document.getElementById(fid)||document.frames[fid];
}