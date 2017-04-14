package ROMdb.Reports;

import ROMdb.Helpers.RequirementsRow;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.collections.ObservableList;
import com.itextpdf.text.Font;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Anthony Orio on 4/13/2017.
 *
 * The purpose of this class is to generate the reports using the
 * open source software iText.
 */
public class ReportGenerator {

    private static final Font BOLD_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static final Font BOLD_HEADERS = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static final String t = "   ";
    private static final String dt = "     ";
    private static final String tt = "         ";

    public static void main(String[] args) throws FileNotFoundException, DocumentException {

        ReportGenerator.generatePortraitSLOCS(null);


    }

    public static void generatePortraitSLOCS(ObservableList<RequirementsRow> reqItems)
                                                    throws FileNotFoundException, DocumentException {


        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/Anthony Orio/Desktop/Rowan/Software Engineering/Project/Desktoppic.pdf"));

        /* Beginning creating the document. */
        document.open();


        /* Line separator */
        document.add( defaultSeparator() );

        /* Create title */
        Paragraph title = new Paragraph();
        title.setAlignment(Element.ALIGN_CENTER);
        title.add(new Chunk("Add/Chg/Del SLOC's Summary\n\n", BOLD_TITLE));
        document.add(title);

        /* Line separate */
        document.add( defaultSeparator() );

        /* Column headings */
        Paragraph headings = new Paragraph();
        String columnNames = "Build" +dt+dt+t+ "Doors ID" +tt+tt+dt+ "CPRS Paragraph" +tt+tt+dt+ "CSC" +tt+tt+ "CSU" +tt+tt+dt+ "Add" +tt+ "Chg" +tt+ "Del";
        headings.add(new Chunk(columnNames, BOLD_HEADERS));
        document.add(headings);

        /* Line separate */
        document.add( defaultSeparator() );
        document.add(new Chunk("\n"));

        //document = subtotalSection(document);


        document.close();
    }

    private static Chunk defaultSeparator() throws DocumentException {

        LineSeparator ls = new LineSeparator(1.5f, 105f, BaseColor.BLACK, Element.ALIGN_CENTER, 0);

        return new Chunk(ls);
    }

    private static Document subtotalSection(Document doc, ArrayList<RequirementsRow> partition) throws DocumentException {

        LineSeparator ls = new LineSeparator(1f, 105f, BaseColor.BLACK, Element.ALIGN_CENTER, 0);
        doc.add(new Chunk((ls)));

        PdfPTable table = new PdfPTable(4);
        PdfPCell table_cell;

        doc.add(table);

        ls = new LineSeparator(1f, 105f, BaseColor.BLACK, Element.ALIGN_CENTER, 0);
        doc.add(new Chunk((ls)));

        return doc;
    }

    private double[] calculateSubtotal(ArrayList<RequirementsRow> partition)
    {
        double[] subs = new double[4];

        return subs;
    }
}



// 1) Separate by first name
// 2) Partition.
// 3) Generate subtotal section with partitions.



/*PdfContentByte canvas = writer.getDirectContent();
        CMYKColor blackColor = new CMYKColor(0.f, 0.f, 0.f, 1.f);
        canvas.setColorStroke(blackColor);

        canvas.moveTo(0, 100);
        canvas.lineTo(30, 560);
        canvas.closePathStroke();*/