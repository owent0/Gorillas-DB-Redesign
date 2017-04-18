package ROMdb.Reports;

import ROMdb.Helpers.FileHandler;
import ROMdb.Helpers.RequirementsRow;
import ROMdb.Models.RequirementsModel;
import com.itextpdf.text.*;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.text.Font;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import sun.reflect.generics.tree.Tree;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Team Gorillas
 * Created by Anthony Orio on 4/13/2017.
 * Last Modified by Jatin Bhakta on 4/16/2017
 * The purpose of this class is to generate the reports using the
 * open source software iText.
 */
public class ReportGenerator
{
    private static final FileHandler fileHandler = new FileHandler();

    private static final Font BOLD_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static final Font BOLD_HEADERS = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    //private static final String T = "   ";
    //private static final String DT = "     ";
    private static final String TT = "         ";
    private static final String T = TT+TT+TT;
    private static final String ADDCHGDEL = "Add" +TT+ "Chg" +TT+ "Del" +TT+ "Total";

    public static void main(String[] args) throws FileNotFoundException, DocumentException
    {
        ArrayList<String> groups = new ArrayList<>();
        groups.add("Heading 1");
        groups.add("Heading 2");
        groups.add("Heading 3");
        groups.add("Heading 4");

        // ReportGenerator.generateDDR(false);
        ReportGenerator.generateSLOCS(groups);

    }

    /**
     *
     * @param isLandscape
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void generateDDR(boolean isLandscape) throws FileNotFoundException, DocumentException
    {
        String path = fileHandler.getPathWithFileChooser();
        String filename = "/DDRreportPortrait.pdf";

        Document document = new Document();

        if (isLandscape)
        {
            document.setPageSize(PageSize.A4_LANDSCAPE.rotate());
            filename = "/DDRreportLandscape.pdf";
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path + filename));

        // begin writing to document
        document.open();

        // Line separator
        document.add(defaultSeparator());

        // Create title
        Paragraph title = new Paragraph();
        title.setAlignment(Element.ALIGN_CENTER);
        title.add(new Chunk("DDR Requirements Traceability Report - F100-S2 \n\n", BOLD_TITLE));
        document.add(title);

        // add the table with all the data to the report
        //document.add(populateReportFieldsFromListView());
        document = populateReportFieldsFromListView(document);

        document.close();
    }


    private static PdfPTable createColHeadingsForTable(int numColumns, String[] columnHeadings)
    {
        PdfPTable tableForColHeadings = new PdfPTable(numColumns);
        tableForColHeadings.setWidthPercentage(100);

        for (int i = 0; i < columnHeadings.length; i++)
        {
            PdfPCell table_cell = new PdfPCell( new Phrase(columnHeadings[i], BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            tableForColHeadings.addCell(table_cell);
        }

        return tableForColHeadings;
    }

    /**
     *
     * @param document
     * @return
     * @throws DocumentException
     */
    private static Document populateReportFieldsFromListView(Document document) throws DocumentException
    {
        // create table w/constant # columns in PDF to represent cols from grid view (which comes from ReqData table)
        int numCols = RequirementsModel.gridViewColumnHeadings.length;
        PdfPTable ddrReportTable = new PdfPTable(numCols);
        ddrReportTable.setWidthPercentage(100);

        // add the column names for the DDR report to the DDR report table
        PdfPTable ddrReportTableForColHeadings = createColHeadingsForTable( numCols, RequirementsModel.gridViewColumnHeadings );

        // add the table (w/just the col headings) to the report
        document.add(ddrReportTableForColHeadings);

        // Line separator
        document.add(defaultSeparator());

        // number of Rows in the currFilteredList view
        int size = RequirementsModel.currentFilteredList.size();

        // represents the grid/list view that is seen by user
        ObservableList<RequirementsRow> gridView;

        // if no dropdown filter for the view is chosen, the view consists of all ReqData from DB
        if (size == 0)
        {
            gridView = RequirementsModel.allReqData;

            // we need to reset size to # of rows in grid view when no filter is applied
            size = gridView.size();
        }
        else
        {
            // at least 1 filter is applied, therefore gridView isn't allReqData anymore
            gridView = RequirementsModel.currentFilteredList;
        }

        // For every row in the FilteredList take each column property and put it in a cell in the report
        for (int i = 0; i < size; i++)
        {
            // get the current ReqRow Object in the grid/list view
            RequirementsRow currRowInGridView = gridView.get(i);

            String csc = currRowInGridView.getCsc();
            PdfPCell table_cell = new PdfPCell( new Phrase(csc, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String csu = currRowInGridView.getCsu();
            table_cell = new PdfPCell( new Phrase(csu, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String doorsID = currRowInGridView.getDoorsID();
            table_cell = new PdfPCell( new Phrase(doorsID, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String paragraph = currRowInGridView.getParagraph();
            table_cell = new PdfPCell( new Phrase(paragraph, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String baseline = currRowInGridView.getBaseline();
            table_cell = new PdfPCell( new Phrase(baseline, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String scIcr = currRowInGridView.getScicr();
            table_cell = new PdfPCell( new Phrase(scIcr, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String capability = currRowInGridView.getCapability();
            table_cell = new PdfPCell( new Phrase(capability, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String numAdded = String.valueOf(currRowInGridView.getAdd());
            table_cell = new PdfPCell( new Phrase(numAdded, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String numChanged = String.valueOf(currRowInGridView.getChange());
            table_cell = new PdfPCell( new Phrase(numChanged, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String numDeleted = String.valueOf(currRowInGridView.getDelete());
            table_cell = new PdfPCell( new Phrase(numDeleted, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String designWeight = String.valueOf(currRowInGridView.getDesignWeight());
            table_cell = new PdfPCell( new Phrase(designWeight, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String codeWeight = String.valueOf(currRowInGridView.getCodeWeight());
            table_cell = new PdfPCell( new Phrase(codeWeight, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String unitTestWeight = String.valueOf(currRowInGridView.getUnitTestWeight());
            table_cell = new PdfPCell( new Phrase(unitTestWeight, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String integrationWeight = String.valueOf(currRowInGridView.getIntegrationWeight());
            table_cell = new PdfPCell( new Phrase(integrationWeight, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String responsibleIndivid = currRowInGridView.getRi();
            table_cell = new PdfPCell( new Phrase(responsibleIndivid, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String rommer = currRowInGridView.getRommer();
            table_cell = new PdfPCell( new Phrase(rommer, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            String program = currRowInGridView.getProgram();
            table_cell = new PdfPCell( new Phrase(program, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

        }
        document.add(ddrReportTable);
        return document;
    }

    /*
    * This was here to try and avoid DRY principle, but some need different alignment props,
    * need to convert types to String etc..
    private static PdfPTable addCellToTable(PdfPTable ddrReportTable, String cellValue)
    {
        PdfPCell table_cell = new PdfPCell( new Phrase(cellValue, BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ddrReportTable.addCell(table_cell);

        return ddrReportTable;
    }
    */

    /**
     * Generates the SLOCS report for groups 1, 2, 3 and 4.
     * @param groups The group(s) that will be on the used as columns.
     * @throws FileNotFoundException If the file cannot be located and written to.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    public static void generateSLOCS(ArrayList<String> groups)
                                                    throws FileNotFoundException, DocumentException
    {
        String path = fileHandler.getPathWithFileChooser();

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path + "/Desktoppic.pdf"));

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


        /* Get the items to place onto the report. */
        ArrayList<RequirementsRow> rows = new ArrayList<>(RequirementsModel.currentFilteredList);
        //HashMap<String, ArrayList<RequirementsRow>> categories = sortByWhat(rows, groups.get(0));

        //ArrayList<RequirementsRow> partition = new ArrayList<>();






        document = addMasterTable(document, groups, rows);


        /* Add subtotal section */
        document = addSubtotalSection(document, rows, groups.size());

        document.close();
    }

   /* private static HashMap<String, ArrayList<RequirementsRow>> sortByWhat(ArrayList<RequirementsRow> rows, String sortByThis)
    {
        TreeSet<String> categories = new TreeSet<>();
        HashMap<String, ArrayList<RequirementsRow>> map = new HashMap<>();
        int size = rows.size();

        ArrayList<RequirementsRow> temp = new ArrayList<>();
        switch (sortByThis)
        {
            case "SC/ICR":

                break;

            case "CSC":
                break;

            case "CSU":
                break;

            case "Capability":
                break;

            //case "Build":                   break;

            case "Responsible Individual":
                break;

            //case "CPRS Function":           break;

            case "Baseline":
                break;

            case "Paragraph/Figure":
                break;

            case "Program":
                break;

            default:
                break;
        }

        return map;
    }*/

    /**
     *
     * @param doc The document to create the column names for.
     * @param groups The list contain the groups chosen by the user.
     * @return The document with the column names.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    private static Document createColumnNames(Document doc, ArrayList<String> groups) throws DocumentException
    {
        PdfPTable outerTable = new PdfPTable(2);
        outerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.setWidthPercentage(100f);

        PdfPTable leftTable = new PdfPTable(groups.size());
        leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.setWidthPercentage(100f);

        PdfPTable rightTable = new PdfPTable(4);
        rightTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.setWidthPercentage(100f);

        outerTable.setWidths( (groups.size() < 4) ? new int[]{60, 35} : new int[]{40, 15} );

        int size = groups.size();
        for (int i = 0; i < size; i++)
        {
            PdfPCell newCell = createTextCell(groups.get(i));
            newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftTable.addCell(newCell);
        }


        PdfPCell newCell = createTextCell("Add");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Chg");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Del");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Total");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        outerTable.addCell( createNestedTableCell(leftTable) );
        outerTable.addCell( createNestedTableCell(rightTable) );
        doc.add(outerTable);

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


    private static Document addMasterTable(Document doc, ArrayList<String> groups,  ArrayList<RequirementsRow> rows) throws DocumentException
    {
        int groupSize = groups.size();
        int rowCount = rows.size();

        PdfPTable masterTable = new PdfPTable(2);
        masterTable.setWidthPercentage(100f);
        masterTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        masterTable.setWidths( (groupSize < 4) ? new int[]{60, 35} : new int[]{40, 15} );

        /* Initialize the table with a left alignment. */
        PdfPTable partitionTable = new PdfPTable(groupSize);
        partitionTable.setWidthPercentage(100f);
        partitionTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPTable subTable = new PdfPTable(4);
        subTable.setWidthPercentage(100f);
        subTable.setHorizontalAlignment(Element.ALIGN_RIGHT);



        for (int i = 0; i < rowCount; i++)
        {
            RequirementsRow curr = rows.get(i);


            for (int ii = 0; ii < groupSize; ii++)
            {
                String item = "";

                switch (groups.get(ii))
                {
                    case "SC/ICR":                  item = curr.getScicr(); break;
                    case "CSC":                     item = curr.getCsc();   break;
                    case "CSU":                     item = curr.getCsu();   break;
                    case "Capability":              item = curr.getCapability(); break;
                    //case "Build":                   break;
                    case "Responsible Individual":  item = curr.getRi(); break;
                    //case "CPRS Function":           break;
                    case "Baseline":                item = curr.getBaseline(); break;
                    case "Paragraph/Figure":        item = curr.getParagraph(); break;
                    case "Program":                 item = curr.getProgram(); break;
                    default:
                        break;
                }

                PdfPCell newCell = createTextCell(item);
                newCell.setBorder(Rectangle.NO_BORDER);
                newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                partitionTable.addCell(newCell);
            }

            double add = rows.get(i).getAdd();
            double chg = rows.get(i).getChange();
            double del = rows.get(i).getDelete();
            double tot = add + chg + del;

            PdfPCell tempCell = new PdfPCell( createTextCell(Double.toString(add)) );
            tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            subTable.addCell( tempCell );

            tempCell = new PdfPCell( createTextCell(Double.toString(chg)) );
            tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            subTable.addCell( tempCell );

            tempCell = new PdfPCell( createTextCell(Double.toString(del)) );
            tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            subTable.addCell( tempCell );

            tempCell = new PdfPCell( createTextCell(Double.toString(tot)) );
            tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            subTable.addCell( tempCell );
        }

        PdfPCell left = createNestedTableCell(partitionTable);
        PdfPCell right = createNestedTableCell(subTable);
        //left.setBorder(Rectangle.NO_BORDER);
        left.setHorizontalAlignment(Element.ALIGN_LEFT);
        right.setHorizontalAlignment(Element.ALIGN_RIGHT);

        masterTable.addCell(left);
        masterTable.addCell(right);

        doc.add(masterTable);

        return doc;
    }

    /**
     * Add the subtotal section to the document. A document may contain multiple subtotal sections
     * depending on how many partitions exist. The group of items the user is wanting to print is
     * broken down into partitions based on the first group choice they choose.
     * @param doc The document to add the subtotal section to.
     * @param partition The group of rows that will be part of this subtotal section.
     * @return The document with the added subtotal section.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    private static Document addSubtotalSection(Document doc, ArrayList<RequirementsRow> partition, int groupSize) throws DocumentException {

        LineSeparator ls = new LineSeparator(1f, 105f, BaseColor.BLACK, Element.ALIGN_CENTER, -10);
        doc.add(new Chunk((ls)));

        PdfPTable outerTable = new PdfPTable(2);
        outerTable.setWidthPercentage(100f);
        outerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.setWidths( (groupSize < 4) ? new int[]{60, 35} : new int[]{40, 15} );

        /* Initialize the table with a left alignment. */
        PdfPTable leftTable = new PdfPTable(4);
        leftTable.setWidthPercentage(100f);
        leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPTable rightTable = new PdfPTable(4);
        rightTable.setWidthPercentage(100f);
        rightTable.setHorizontalAlignment(Element.ALIGN_RIGHT);


        PdfPCell subCell = createTextCell("Subtotal");
        subCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        outerTable.addCell(subCell);

        double[] subs = calculateSubtotal(partition);

        PdfPCell table_cell = new PdfPCell( new Phrase(Double.toString(subs[0]), BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(table_cell);

        table_cell = new PdfPCell( new Phrase(Double.toString(subs[1]), BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(table_cell);

        table_cell = new PdfPCell( new Phrase(Double.toString(subs[2]), BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(table_cell);

        table_cell = new PdfPCell( new Phrase(Double.toString(subs[3]), BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(table_cell);


        //outerTable.addCell( createNestedTableCell(leftTable) );
        outerTable.addCell( createNestedTableCell(rightTable) );
        doc.add(outerTable);

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

