package ROMdb.Reports;

import ROMdb.Helpers.RequirementsRow;
import com.itextpdf.text.*;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.text.Font;
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
    private static final Font BOLD_HEADERS = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    //private static final String T = "   ";
    private static final String DT = "     ";
    private static final String TT = "         ";
    private static final String T = TT+TT+TT;
    private static final String ADDCHGDEL = "Add" +TT+ "Chg" +TT+ "Del" +TT+ "Total";

    public static void main(String[] args) throws FileNotFoundException, DocumentException {

        ArrayList<String> groups = new ArrayList<>();
        groups.add("Heading 1");
        groups.add("Heading 2");
        groups.add("Heading 3");
        groups.add("Heading 4");

        ReportGenerator.generateSLOCS(null, groups);

    }

    /**
     * Generates the SLOCS report for groups 1, 2, 3 and 4.
     * @param reqItems The requirements that will be on the report.
     * @param groups The group(s) that will be on the used as columns.
     * @throws FileNotFoundException If the file cannot be located and written to.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    public static void generateSLOCS(ArrayList<RequirementsRow> reqItems, ArrayList<String> groups)
                                                    throws FileNotFoundException, DocumentException
    {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/Anthony Orio/Desktop/Rowan/Software Engineering/Project/Desktoppic.pdf"));

        if (groups.size() >= 4)
            document.setPageSize(PageSize.A4_LANDSCAPE.rotate());

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
        document = createColumnNames(document, groups);

        /* Add subtotal section */
        document = addSubtotalSection(document, null);

        document.close();
    }

    /**
     *
     * @param doc The document to create the column names for.
     * @param groups The list contain the groups chosen by the user.
     * @return The document with the column names.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    private static Document createColumnNames(Document doc, ArrayList<String> groups) throws DocumentException
    {
        Paragraph headers = new Paragraph();
        headers.setFont(BOLD_HEADERS);

        /* We need to format based on how many groups are chosen. */
        switch (groups.size())
        {
            case 1: headers.add( new Chunk(groups.get(0)) );
                    break;
            case 2: headers.add( new Chunk(groups.get(0) +T+ groups.get(1)) );
                    break;
            case 3: headers.add( new Chunk(groups.get(0) +T+ groups.get(1) +T+ groups.get(2)) );
                    break;
            case 4: headers.add( new Chunk(groups.get(0) +T+ groups.get(1) +T+ groups.get(2) +T+ groups.get(3)) );
                    break;
            case 5:
                    break;
            case 6:
                    break;

        }

        // This will let us "glue" add/chg/del onto the right hand side.
        Chunk glue = new Chunk(new VerticalPositionMark());

        // Add glue and place add/chg/del.
        headers.add(new Chunk(glue));
        headers.add(ADDCHGDEL);

        doc.add(headers);

        return doc;
    }

    /**
     * Adds a line separator that was chosen to be considered "default".
     * @return A Chunk containing the line separator.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    private static Chunk defaultSeparator() throws DocumentException
    {
        LineSeparator ls = new LineSeparator(1.5f, 105f, BaseColor.BLACK, Element.ALIGN_CENTER, 0);
        return new Chunk(ls);
    }

    /**
     * Add the subtotal section to the document. A document may contain multiple subtotal sections
     * depending on how many partitions exist.
     * @param doc The document to add the subtotal section to.
     * @param partition The group of rows that will be part of this subtotal section.
     * @return The document with the added subtotal section.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    private static Document addSubtotalSection(Document doc, ArrayList<RequirementsRow> partition) throws DocumentException {

        LineSeparator ls = new LineSeparator(1f, 105f, BaseColor.BLACK, Element.ALIGN_CENTER, -10);
        doc.add(new Chunk((ls)));

        /* This table will contain the nested subtotal table and subtotal phrase */
        PdfPTable wordsAndTable = new PdfPTable(2);
        wordsAndTable.setWidthPercentage(50f);
        wordsAndTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        /* Add the phrase "Subtotal" to the wordsAndTable. */
        PdfPCell textCell = createTextCell("Subtotal");
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        wordsAndTable.addCell( textCell );

        /* The nested table that will contain the numbers return from calculateSubtotal. */
        PdfPTable subTable = new PdfPTable(4);
        subTable.setWidthPercentage(100f);

        /* Add the numbers to each of the subtotal columns. */
        PdfPCell table_cell = new PdfPCell( new Phrase("Test", BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        subTable.addCell(table_cell);
        subTable.addCell(table_cell);
        subTable.addCell(table_cell);
        subTable.addCell(table_cell);

        /* Nest the subtotal table inside of the wordsAndTable table. */
        PdfPCell tableCell = createNestedTableCell(subTable);
        tableCell.setBorder(Rectangle.NO_BORDER);
        wordsAndTable.addCell( tableCell );

        doc.add(wordsAndTable);


        ls = new LineSeparator(1f, 105f, BaseColor.BLACK, Element.ALIGN_CENTER, 10);
        doc.add(new Chunk((ls)));

        return doc;
    }

    /**
     * Calculates the subtotal for the partition passed.
     * @param partition The group of rows that will be calculated together.
     * @return An array containing the subtotals for each column.
     */
    private static double[] calculateSubtotal(ArrayList<RequirementsRow> partition)
    {
        /* We have four columns. */
        double[] subs = new double[4];
        int size = partition.size();

        for(int i = 0; i < size; i++)
        {
            RequirementsRow curr = partition.get(i);

            /* Total is add, change and deleted added together.                     */
            double total = curr.getAdd() + curr.getChange() + curr.getDelete();
            subs[0] += curr.getAdd();                           /* Add subtotal     */
            subs[1] += curr.getChange();                        /* Change subtotal  */
            subs[2] += curr.getDelete();                        /* Delete subtotal  */
            subs[3] += total;                                   /* Total subtotal   */
        }

        return subs;
    }

    /**
     * Creates a cell that has a table within it.
     * @param table The table to nest into the cell.
     * @return The cell with the nested table.
     */
    private static PdfPCell createNestedTableCell(PdfPTable table)
    {
        /* Instantiate new cell. */
        PdfPCell cell = new PdfPCell();

        /* Instantiate a paragraph. */
        Paragraph p = new Paragraph();

        /* Add the table to the paragraph. */
        /* We must do this because you cannot directly add a table to a cell */
        p.add(table);

        /* Add the paragraph to the cell. */
        cell.addElement(p);

        return cell;
    }

    /**
     * Creates a cell that contains text.
     * @param text The text inside of the cell.
     * @return The text cell.
     */
    private static PdfPCell createTextCell(String text)
    {
        /* Create the phrase with the text. */
        Phrase ph = new Phrase(text, BOLD_HEADERS);

        /* Create the cell with the phrase */
        PdfPCell cell = new PdfPCell(ph);

        return cell;
    }
}

// 1) Separate by first name
// 2) Partition.
// 3) Generate subtotal section with partitions.
