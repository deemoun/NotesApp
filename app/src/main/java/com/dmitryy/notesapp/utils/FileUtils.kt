package com.dmitryy.notesapp.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.dmitryy.notesapp.data.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {
    private val gson = Gson()

    /**
     * Export notes to JSON format
     */
    fun exportNotesToJson(notes: List<Note>): String {
        Logger.d("FileUtils: exportNotesToJson - exporting ${notes.size} notes")
        val json = gson.toJson(notes)
        Logger.d("FileUtils: exportNotesToJson - JSON length: ${json.length}")
        return json
    }

    /**
     * Import notes from JSON string
     */
    fun importNotesFromJson(jsonContent: String): List<Note> {
        Logger.d("FileUtils: importNotesFromJson - JSON length: ${jsonContent.length}")
        val type = object : TypeToken<List<Note>>() {}.type
        val notes: List<Note> = gson.fromJson(jsonContent, type)
        Logger.d("FileUtils: importNotesFromJson - imported ${notes.size} notes")
        return notes
    }

    /**
     * Save JSON content to file and return share intent
     */
    fun saveAndShareJson(context: Context, jsonContent: String, fileName: String? = null): Intent {
        Logger.d("FileUtils: saveAndShareJson - content length: ${jsonContent.length}")
        val dateFormat = SimpleDateFormat("dd_MM_yyyy_HHmm", Locale.getDefault())
        val actualFileName = fileName ?: "notes_${dateFormat.format(Date())}.json"
        Logger.d("FileUtils: saveAndShareJson - fileName: $actualFileName")
        
        val jsonFile = File(context.cacheDir, actualFileName)
        jsonFile.writeText(jsonContent)
        
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            jsonFile
        )
        
        Logger.d("FileUtils: saveAndShareJson - file saved and intent created")
        return Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    /**
     * Generate PDF from note and return share intent
     */
    fun generateAndSharePdf(
        context: Context,
        title: String,
        content: String
    ): Intent {
        Logger.d("FileUtils: generateAndSharePdf - title: '$title', content length: ${content.length}")
        // Use title if available, otherwise use timestamp
        val fileName = if (title.isNotBlank()) {
            title.replace("[^a-zA-Z0-9]".toRegex(), "_")
        } else {
            val dateFormat = SimpleDateFormat("dd_MM_yyyy_HHmm", Locale.getDefault())
            "Note_${dateFormat.format(Date())}"
        }
        Logger.d("FileUtils: generateAndSharePdf - fileName: $fileName")
        
        // Create PDF file in cache directory
        val pdfFile = File(context.cacheDir, "${fileName}.pdf")
        
        // Create PDF document
        val writer = PdfWriter(pdfFile)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)
        
        // Add title
        val titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)
        val titleParagraph = Paragraph(title)
            .setFont(titleFont)
            .setFontSize(24f)
            .setMarginBottom(20f)
        document.add(titleParagraph)
        
        // Add content
        val contentFont = PdfFontFactory.createFont(StandardFonts.HELVETICA)
        val contentParagraph = Paragraph(content)
            .setFont(contentFont)
            .setFontSize(12f)
        document.add(contentParagraph)
        
        // Close document
        document.close()
        
        // Share PDF
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            pdfFile
        )
        
        Logger.d("FileUtils: generateAndSharePdf - PDF generated successfully")
        return Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
