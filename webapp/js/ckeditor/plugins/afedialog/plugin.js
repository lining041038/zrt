CKEDITOR.plugins.add( 'afedialog', {
    icons: 'afedialog',
    init: function( editor ) {
        // Plugin logic goes here...
        editor.addCommand( 'afeDialog', new CKEDITOR.dialogCommand( 'afeDialog' ) );
        editor.ui.addButton( 'AfeDialog', {
		    label: '公式编辑器',
		    command: 'afeDialog',
		    toolbar: 'insert'
		});
		CKEDITOR.dialog.add( 'afeDialog', this.path + 'dialogs/afedialog.js' );
    }
});
