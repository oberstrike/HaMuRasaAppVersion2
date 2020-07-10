package de.hamurasa.main.fragments.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import de.hamurasa.R
import de.util.hamurasa.utility.util.AbstractDialog
import kotlinx.android.synthetic.main.dialog_import_export.*

class ImportExportDialog(
    private val json: String,
    private val clipboardManager: ClipboardManager
) : AbstractDialog(), View.OnClickListener {
    override fun getLayoutId() = R.layout.dialog_import_export

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonMultiLineText.setText(json)
        exportButton.setOnClickListener(this)
        importButton.setOnClickListener(this)
        import_export_close_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == null)
            return
        when (v.id) {
            R.id.exportButton -> {
                export()
            }
            R.id.importButton -> {

            }
            R.id.import_export_close_button -> {
                dismiss()
            }
        }
    }


    private fun export() {
        val clip = ClipData.newPlainText("Copied Text", json)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
    }


}