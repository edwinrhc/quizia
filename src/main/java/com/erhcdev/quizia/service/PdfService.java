package com.erhcdev.quizia.service;

import com.erhcdev.quizia.model.QuestionResult;
import com.erhcdev.quizia.model.QuizResult;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generatePdf(QuizResult result) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 50, 50);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // === Encabezado ===
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
            Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK);
            Font dateFont = new Font(Font.HELVETICA, 10, Font.ITALIC, Color.GRAY);

            Paragraph title = new Paragraph("📘 Quiz AI - " + result.getTopic(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph date = new Paragraph("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            document.add(date);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total de preguntas: " + result.getQuestions().size(), normalFont));
            document.add(new Paragraph(" "));

            // === Variables para resumen ===
            int correctCount = 0;
            int incorrectCount = 0;

            // === Iterar preguntas ===
            int index = 1;
            for (QuestionResult q : result.getQuestions()) {
                Font questionFont = new Font(Font.HELVETICA, 13, Font.BOLD);
                document.add(new Paragraph(index++ + ". " + q.getQuestion(), questionFont));

                for (String option : q.getOptions()) {
                    boolean isCorrect = option.equals(q.getCorrectAnswer());
                    boolean isSelected = option.equals(q.getSelectedAnswer());

                    // Color por estado
                    Color color;
                    String icon;
                    if (isCorrect && isSelected) {
                        color = Color.GREEN.darker();
                        icon = "✅";
                        correctCount++;
                    } else if (isSelected && !isCorrect) {
                        color = Color.RED;
                        icon = "❌";
                        incorrectCount++;
                    } else if (isCorrect) {
                        color = new Color(0, 128, 0); // verde más oscuro
                        icon = "✔";
                    } else {
                        color = Color.BLACK;
                        icon = " ";
                    }

                    Font optionFont = new Font(Font.HELVETICA, 12, Font.NORMAL, color);
                    document.add(new Paragraph("   " + icon + " " + option, optionFont));
                }

                document.add(new Paragraph(" "));
            }

            // === Resumen final ===
            document.add(new Paragraph("──────────────────────────────"));
            Font summaryFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.BLUE);
            document.add(new Paragraph("📊 Resumen del Quiz", summaryFont));
            document.add(new Paragraph(" "));

            Font resultFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK);
            document.add(new Paragraph("✅ Correctas: " + correctCount, new Font(Font.HELVETICA, 12, Font.BOLD, Color.GREEN.darker())));
            document.add(new Paragraph("❌ Incorrectas: " + incorrectCount, new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED)));

            int total = result.getQuestions().size();
            int score = (int) ((double) correctCount / total * 100);
            document.add(new Paragraph("🎯 Puntaje final: " + score + " / 100", resultFont));

            document.add(new Paragraph(" "));
            Paragraph thanks = new Paragraph("Desarrollado por Edwin RH • Quiz IA Spring Boot", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.GRAY));
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

}
