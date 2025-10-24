package com.erhcdev.quizia.service;

import com.erhcdev.quizia.model.QuestionResult;
import com.erhcdev.quizia.model.QuizResult;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

@Service
public class PdfService {

    public byte[] generatePdf(QuizResult result){

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Quiz: " + result.getTopic()));
            document.add(new Paragraph(" "));

            int i = 1;
            for (QuestionResult q : result.getQuestions()) {
                document.add(new Paragraph(i++ + ". " + q.getQuestion()));
                for (String opt : q.getOptions()) {
                    document.add(new Paragraph("   - " + opt));
                }
                document.add(new Paragraph("✅ Correcta: " + q.getCorrectAnswer()));
                document.add(new Paragraph("❌ Tu respuesta: " + q.getSelectedAnswer()));
                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

}
