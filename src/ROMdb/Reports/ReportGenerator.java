package ROMdb.Reports;

import ROMdb.Helpers.FileHandler;
import ROMdb.Helpers.RequirementsRow;
import ROMdb.Models.RequirementsModel;
import com.itextpdf.text.*;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.Font;
import javafx.collections.ObservableList;
import sun.reflect.generics.tree.Tree;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

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



    public static void generateDDR(boolean isLandscape) throws FileNotFoundException, DocumentException {
        /* Use a file chooser to find the path. */
        String path = fileHandler.getPathWithFileChooser();

        /* Instantiate document and its location. */
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path + "/DDRReq.pdf"));

        /* Change to landscape if there are more than four group selections. */
        if (isLandscape)
            document.setPageSize(PageSize.A4_LANDSCAPE.rotate());

         /* Beginning creating the document. */
        document.open();

        /* Line separator */
        document.add( defaultSeparator() );

        /* Create title */
        Paragraph title = new Paragraph();
        title.setAlignment(Element.ALIGN_CENTER);
        title.add(new Chunk("DDR Requirements Traceability Report\n\n", BOLD_TITLE));
        document.add(title);

        /* Line separate */
        document.add( defaultSeparator() );

        /* Column headings */
        document = addDDRHeaders(document, isLandscape);

        ArrayList<RequirementsRow> rows = new ArrayList<>(RequirementsModel.currentFilteredList);
        document = addReqRows(document, rows, isLandscape);

        /* Get the items to place onto the report. */
        /*ArrayList<RequirementsRow> rows = new ArrayList<>(RequirementsModel.currentFilteredList);

        *//* Let's sort based on the first group chosen. *//*
        TreeMap<String, ArrayList<RequirementsRow>> partitions = sortByWhat(rows, groups.get(0));

        *//* For each list associated in the hash map. *//*
        for (String key : partitions.keySet())
        {
            *//* Add the partion table. *//*
            document = addPartitionTable(document, groups, partitions.get(key), false);

            *//* Add subtotal section *//*
            document = addSubtotalSection(document, partitions.get(key), groups.size(), false);
        }
*/
        /* Done writing to PDF. */
        document.close();
    }

    public static Document addDDRHeaders(Document doc, boolean isLandscape) throws DocumentException
    {
        /* Outer table will consist of group headers on left and sub total headers on right. */
        PdfPTable outerTable = new PdfPTable(2);
        outerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.setWidthPercentage(100f);

        /* The left table with the group headers. */
        PdfPTable leftTable = new PdfPTable(5);
        leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.setWidthPercentage(100f);

        /* The right table with design, code, unit test and integ weight. */
        PdfPTable rightTable = new PdfPTable(4);
        rightTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.setWidthPercentage(100f);

        /* Set the width of the table based on the orientation of the document. */
        outerTable.setWidths( (isLandscape) ? new int[]{60, 35} : new int[]{40, 15} );

        PdfPCell newCell = createTextCell("Build");
        newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        leftTable.addCell(newCell);

        newCell = createTextCell("Doors ID");
        newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        leftTable.addCell(newCell);

        newCell = createTextCell("CPRS Paragraph");
        newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        leftTable.addCell(newCell);

        newCell = createTextCell("CSC");
        newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        leftTable.addCell(newCell);

        newCell = createTextCell("CSU");
        newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        leftTable.addCell(newCell);

        /* Create the headers for the sub totals. */
        newCell = createTextCell("Add");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Chg");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Del");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Tot");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        /* Nest the left and right tables into the outer table. */
        outerTable.addCell( createNestedTableCell(leftTable) );
        outerTable.addCell( createNestedTableCell(rightTable) );
        doc.add(outerTable);

        return doc;
    }

    public static Document addReqRows(Document doc, ArrayList<RequirementsRow> rows, boolean isLandscape) throws DocumentException
    {
        /* Outer table will consist of group headers on left and sub total headers on right. */
        PdfPTable outerTable = new PdfPTable(2);
        outerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.setWidthPercentage(100f);

        /* The left table with the group headers. */
        PdfPTable leftTable = new PdfPTable(5);
        leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.setWidthPercentage(100f);

        /* The right table with design, code, unit test and integ weight. */
        PdfPTable rightTable = new PdfPTable(3);
        rightTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.setWidthPercentage(100f);

        /* Set the width of the table based on the orientation of the document. */
        outerTable.setWidths( (isLandscape) ? new int[]{60, 35} : new int[]{40, 15} );

        TreeMap<String, ArrayList<RequirementsRow>> partitions = partitionBySCICR(rows);
        for (String key : partitions.keySet())
        {
            Phrase p = new Phrase(key);
            p.setFont(BOLD_TITLE);
            Paragraph par = new Paragraph(p);
            par.setAlignment(Element.ALIGN_LEFT);
            doc.add(par);

            doc = writePartitions(doc, partitions.get(key), isLandscape);

            int size = 0;
            if (!isLandscape)
                size = 5;

            doc = addSubtotalSection(doc, partitions.get(key), size, true);
        }

        return doc;
    }

    public static Document writePartitions(Document doc, ArrayList<RequirementsRow> partition, boolean isLandscape) throws DocumentException
    {
        /* Outer table will consist of group headers on left and sub total headers on right. */
        PdfPTable outerTable = new PdfPTable(2);
        outerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.setWidthPercentage(100f);

        /* The left table with the group headers. */
        PdfPTable leftTable = new PdfPTable(5);
        leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.setWidthPercentage(100f);

        /* The right table with design, code, unit test and integ weight. */
        PdfPTable rightTable = new PdfPTable(4);
        rightTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.setWidthPercentage(100f);

        outerTable.setWidths( (isLandscape) ? new int[]{60, 35} : new int[]{40, 15} );

        for (RequirementsRow row : partition)
        {
            PdfPCell newCell = createTextCell(row.getBuild());
            newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            leftTable.addCell(newCell);

            newCell = createTextCell(row.getDoorsID());
            newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            leftTable.addCell(newCell);

            newCell = createTextCell(row.getParagraph());
            newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            leftTable.addCell(newCell);

            newCell = createTextCell(row.getCsc());
            newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            leftTable.addCell(newCell);

            newCell = createTextCell(row.getCsu());
            newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            leftTable.addCell(newCell);

            newCell = createTextCell(Double.toString(row.getAdd()));
            newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightTable.addCell(newCell);

            newCell = createTextCell(Double.toString(row.getChange()));
            newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightTable.addCell(newCell);

            newCell = createTextCell(Double.toString(row.getDelete()));
            newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightTable.addCell(newCell);

            double tot = row.getAdd() + row.getChange() + row.getDelete();
            newCell = createTextCell(Double.toString(tot));
            newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightTable.addCell(newCell);
        }

        outerTable.addCell(leftTable);
        outerTable.addCell(rightTable);
        doc.add(outerTable);

        return doc;
    }

    public static TreeMap<String, ArrayList<RequirementsRow>> partitionBySCICR(ArrayList<RequirementsRow> rows)
    {
        TreeMap<String, ArrayList<RequirementsRow>> map = new TreeMap<>();
        ArrayList<RequirementsRow> tempList = new ArrayList<>();

        for (RequirementsRow row : rows) {
            for (RequirementsRow r : rows) {
                if (row.getScicr().equals(r.getScicr()))
                    tempList.add(r);
            }
            map.put(row.getScicr(), new ArrayList<>(tempList));
            tempList.clear();
        }

        return map;
    }

    /**
     *
     * @param isLandscape
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    /*public static void generateDDR(boolean isLandscape) throws FileNotFoundException, DocumentException
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
*/
    /**
     *
     * @param numColumns
     * @param columnHeadings
     * @return
     */
    /*private static PdfPTable createColHeadingsForTable(int numColumns, String[] columnHeadings)
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

    private static PdfPTable createSubTotalsDDR(double add, double change, double delete)
    {
        PdfPTable tableForSubTotals = new PdfPTable(3);
        tableForSubTotals.setWidthPercentage(100);

        PdfPCell table_cell = new PdfPCell( new Phrase(String.valueOf(add), BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorder(Rectangle.NO_BORDER);
        tableForSubTotals.addCell(table_cell);

        table_cell = new PdfPCell( new Phrase(String.valueOf(change), BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorder(Rectangle.NO_BORDER);
        tableForSubTotals.addCell(table_cell);

        table_cell = new PdfPCell( new Phrase(String.valueOf(delete), BOLD_HEADERS));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorder(Rectangle.NO_BORDER);
        tableForSubTotals.addCell(table_cell);

        return tableForSubTotals;
    }*/

    /**
     *
     * @param document
     * @return
     * @throws DocumentException
     */
    /*private static Document populateReportFieldsFromListView(Document document) throws DocumentException
    {
        // used to keep track of respective fields in gridView for Subtotals
        double add = 0.0;
        double change = 0.0;
        double delete = 0.0;

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

            String build = currRowInGridView.getBuild();
            table_cell = new PdfPCell( new Phrase(build, BOLD_HEADERS));
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

            add += currRowInGridView.getAdd();
            String numAdded = String.valueOf(add);
            table_cell = new PdfPCell( new Phrase(numAdded, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            change += currRowInGridView.getChange();
            String numChanged = String.valueOf(change);
            table_cell = new PdfPCell( new Phrase(numChanged, BOLD_HEADERS));
            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell.setBorder(Rectangle.NO_BORDER);
            ddrReportTable.addCell(table_cell);

            delete += currRowInGridView.getDelete();
            String numDeleted = String.valueOf(delete);
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

        // add the column names for the DDR report to the DDR report table
        PdfPTable ddrReportTableForSubTotals = createSubTotalsDDR(add, change, delete);

        document.add(ddrReportTable);

        document.add(defaultSeparator());

        // add the table (w/just the col headings) to the report
        document.add(ddrReportTableForSubTotals);

        return document;
    }*/

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
     * Creates the DCTI Report and allows the user to save it to a location
     * on the disk.
     * @param groups The groups chosen to include on the report.
     * @throws FileNotFoundException If the file path cannot be located.
     * @throws DocumentException If the document is open or cannot be written to.
     */
    public static void generateDCTI(ArrayList<String> groups) throws FileNotFoundException, DocumentException
    {
        /* Use a file chooser to find the path. */
        String path = fileHandler.getPathWithFileChooser();

        /* Instantiate document and its location. */
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path + "/DCTIStatus.pdf"));

        /* Change to landscape if there are more than four group selections. */
        if (groups.size() >= 4)
            document.setPageSize(PageSize.A4_LANDSCAPE.rotate());

         /* Beginning creating the document. */
        document.open();

        /* Line separator */
        document.add( defaultSeparator() );

        /* Create title */
        Paragraph title = new Paragraph();
        title.setAlignment(Element.ALIGN_CENTER);
        title.add(new Chunk("D/C/T/I Status\n\n", BOLD_TITLE));
        document.add(title);

        /* Line separate */
        document.add( defaultSeparator() );

        /* Column headings */
        document = createDCTIColumnNames(document, groups);


        /* Get the items to place onto the report. */
        ArrayList<RequirementsRow> rows = new ArrayList<>(RequirementsModel.currentFilteredList);

        /* Let's sort based on the first group chosen. */
        TreeMap<String, ArrayList<RequirementsRow>> partitions = sortByWhat(rows, groups.get(0));

        /* For each list associated in the hash map. */
        for (String key : partitions.keySet())
        {
            /* Add the partion table. */
            document = addPartitionTable(document, groups, partitions.get(key), false);

            /* Add subtotal section */
            document = addSubtotalSection(document, partitions.get(key), groups.size(), false);
        }

        /* Done writing to PDF. */
        document.close();
    }

    /**
     * Creates the column headers at the top of the document.
     * @param doc The document to write to.
     * @param groups The groups to include as the headers.
     * @return The document with the headers attached.
     * @throws DocumentException If the document is open or cannot be written to.
     */
    private static Document createDCTIColumnNames(Document doc, ArrayList<String> groups) throws DocumentException
    {
        /* Outer table will consist of group headers on left and sub total headers on right. */
        PdfPTable outerTable = new PdfPTable(2);
        outerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.setWidthPercentage(100f);

        /* The left table with the group headers. */
        PdfPTable leftTable = new PdfPTable(groups.size());
        leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.setWidthPercentage(100f);

        /* The right table with design, code, unit test and integ weight. */
        PdfPTable rightTable = new PdfPTable(4);
        rightTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.setWidthPercentage(100f);

        /* Set the width of the table based on the orientation of the document. */
        outerTable.setWidths( (groups.size() < 4) ? new int[]{60, 35} : new int[]{40, 15} );

        /* Create the headers for the groups. */
        int size = groups.size();
        for (int i = 0; i < size; i++)
        {
            PdfPCell newCell = createTextCell(groups.get(i));
            newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftTable.addCell(newCell);
        }

        /* Create the headers for the sub totals. */
        PdfPCell newCell = createTextCell("Design");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Code");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Unit Test");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        newCell = createTextCell("Integration");
        newCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(newCell);

        /* Nest the left and right tables into the outer table. */
        outerTable.addCell( createNestedTableCell(leftTable) );
        outerTable.addCell( createNestedTableCell(rightTable) );
        doc.add(outerTable);

        return doc;
    }

    /**
     * Generates the SLOCS report for groups 1, 2, 3 and 4.
     * @param groups The group(s) that will be on the used as columns.
     * @throws FileNotFoundException If the file cannot be located and written to.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    public static void generateSLOCS(ArrayList<String> groups)
                                                    throws FileNotFoundException, DocumentException
    {
        /* Get the directory path. */
        String path = fileHandler.getPathWithFileChooser();

        /* Instantiate the new document with the path chosen. */
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path + "/SLOCSAddChgDel.pdf"));

        /* Change to landscape view if group size is four or larger. */
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
        document = createSLOCColumnNames(document, groups);

        /* Get the items to place onto the report. */
        ArrayList<RequirementsRow> rows = new ArrayList<>(RequirementsModel.currentFilteredList);

        /* Let's sort based on the first group element chosen. */
        TreeMap<String, ArrayList<RequirementsRow>> partitions = sortByWhat(rows, groups.get(0));

        /* For each key in the hash map. */
        for (String key : partitions.keySet())
        {
            /* The partition is the group of rows associated with the key. */
            document = addPartitionTable(document, groups, partitions.get(key), true);

            /* Add subtotal section */
            document = addSubtotalSection(document, partitions.get(key), groups.size(), true);
        }

        document.close();
    }


    /**
     *
     * @param doc The document to create the column names for.
     * @param groups The list contain the groups chosen by the user.
     * @return The document with the column names.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    private static Document createSLOCColumnNames(Document doc, ArrayList<String> groups) throws DocumentException
    {
        /* Outer table will consist of group headers on left and sub total headers on right. */
        PdfPTable outerTable = new PdfPTable(2);
        outerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.setWidthPercentage(100f);

        /* The left table with the group headers. */
        PdfPTable leftTable = new PdfPTable(groups.size());
        leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.setWidthPercentage(100f);

        /* The right table with add, chg, del, total */
        PdfPTable rightTable = new PdfPTable(4);
        rightTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.setWidthPercentage(100f);

        /* Set the width of the table based on the orientation of the document. */
        outerTable.setWidths( (groups.size() < 4) ? new int[]{60, 35} : new int[]{40, 15} );

        /* Create the headers for the groups. */
        int size = groups.size();
        for (int i = 0; i < size; i++)
        {
            PdfPCell newCell = createTextCell(groups.get(i));
            newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftTable.addCell(newCell);
        }

        /* The right table with add, chg, del and total. */
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

        /* Nest the right and left table into the two cells in he outer table. */
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

    /**
     * This will add a new table for each partition created based on the first group element chosen.
     * @param doc The document to write to.
     * @param groups The group of elements to inlude in the report.
     * @param rows The rows to include in this partition.
     * @param isSlocs Used to determine the right hand headers.
     * @return The document that was written to.
     * @throws DocumentException If the document is open or cannot be written to.
     */
    private static Document addPartitionTable(Document doc, ArrayList<String> groups,  ArrayList<RequirementsRow> rows, boolean isSlocs) throws DocumentException
    {
        /* Get the size of the group elements chosen. */
        int groupSize = groups.size();

        /* The number of rows for this partition. */
        int rowCount = rows.size();

        /* The outer table will have the groups on the left and subtotals on the right. */
        PdfPTable masterTable = new PdfPTable(2);
        masterTable.setWidthPercentage(100f);
        masterTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        masterTable.setWidths( (groupSize < 4) ? new int[]{60, 35} : new int[]{40, 15} );

        /* Initialize the table with a left alignment. */
        PdfPTable partitionTable = new PdfPTable(groupSize);
        partitionTable.setWidthPercentage(100f);
        partitionTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        /* This will consist of the subtotal elements. */
        PdfPTable subTable = new PdfPTable(4);
        subTable.setWidthPercentage(100f);
        subTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        /* For each row within this partition. */
        for (int i = 0; i < rowCount; i++)
        {
            RequirementsRow curr = rows.get(i);

            /* For each group element chosen. */
            for (int ii = 0; ii < groupSize; ii++)
            {
                String item = "";

                /* We must determine which groups were chosen. */
                switch (groups.get(ii))
                {
                    case "SC/ICR":                  item = curr.getScicr();      break;
                    case "CSC":                     item = curr.getCsc();        break;
                    case "CSU":                     item = curr.getCsu();        break;
                    case "Capability":              item = curr.getCapability(); break;
                    case "Build":                   item = curr.getBuild();      break;
                    case "Responsible Individual":  item = curr.getRi();         break;
                    case "Baseline":                item = curr.getBaseline();   break;
                    case "Paragraph/Figure":        item = curr.getParagraph();  break;
                    case "Program":                 item = curr.getProgram();    break;
                    default:
                        break;
                }

                /* Add the cell to the left table. */
                PdfPCell newCell = createTextCell(item);
                newCell.setBorder(Rectangle.NO_BORDER);
                newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                partitionTable.addCell(newCell);
            }


            if (isSlocs)    /* If it is a SLOCs report we will use add, chg, del and total. */
            {
                /* Get the values for add, change, delete and total. */
                double add = rows.get(i).getAdd();
                double chg = rows.get(i).getChange();
                double del = rows.get(i).getDelete();
                double tot = add + chg + del;

                /* Add these values to cells and the cells to the table. */
                PdfPCell tempCell = new PdfPCell(createTextCell(Double.toString(add)));
                tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subTable.addCell(tempCell);

                tempCell = new PdfPCell(createTextCell(Double.toString(chg)));
                tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subTable.addCell(tempCell);

                tempCell = new PdfPCell(createTextCell(Double.toString(del)));
                tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subTable.addCell(tempCell);

                tempCell = new PdfPCell(createTextCell(Double.toString(tot)));
                tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subTable.addCell(tempCell);
            }
            else    /* If not SLOCS, then it's a D/C/T/I report that uses design, code, unit, integ weights. */
            {
                /* Get the values for design, code, unit test and integration weight. */
                double design = rows.get(i).getDesignWeight();
                double code = rows.get(i).getCodeWeight();
                double unit = rows.get(i).getUnitTestWeight();
                double integ = rows.get(i).getIntegrationWeight();

                /* Add these values to cells and then to the right table. */
                PdfPCell tempCell = new PdfPCell(createTextCell(Double.toString(design)));
                tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subTable.addCell(tempCell);

                tempCell = new PdfPCell(createTextCell(Double.toString(code)));
                tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subTable.addCell(tempCell);

                tempCell = new PdfPCell(createTextCell(Double.toString(unit)));
                tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subTable.addCell(tempCell);

                tempCell = new PdfPCell(createTextCell(Double.toString(integ)));
                tempCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subTable.addCell(tempCell);
            }
        }

        /* Add the left and right tables to the outer table. */
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
     * Determines how the elements in the report will be sorted based on the first group element chosen.
     * @param rows The rows to include in the report.
     * @param sortByThis The element to sort by.
     * @return A TreeMap containing the sorted data.
     */
    private static TreeMap<String, ArrayList<RequirementsRow>> sortByWhat(ArrayList<RequirementsRow> rows, String sortByThis)
    {
        TreeMap<String, ArrayList<RequirementsRow>> map = new TreeMap<>();
        ArrayList<RequirementsRow> tempList = new ArrayList<>();

        /* We must break the rows into partitions based on the first group element
        * chosen by the user. These partitions are used to write to the document. */
        switch (sortByThis)
        {
            case "SC/ICR":
                for (RequirementsRow row : rows) {                      /* For each row "row". */
                    for (RequirementsRow r : rows) {                    /* Compare each row "r" to the current. */
                        if (row.getScicr().equals(r.getScicr()))        /* If current val is equal to "row" then they belong in partion together. */
                            tempList.add(r);
                    }
                    map.put(row.getScicr(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            case "CSC":
                for (RequirementsRow row : rows) {
                    for (RequirementsRow r : rows) {
                        if (row.getCsc().equals(r.getCsc()))
                            tempList.add(r);
                    }
                    map.put(row.getCsc(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            case "CSU":
                for (RequirementsRow row : rows) {
                    for (RequirementsRow r : rows) {
                        if (row.getCsu().equals(r.getCsu()))
                            tempList.add(r);
                    }
                    map.put(row.getCsu(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            case "Capability":
                for (RequirementsRow row : rows) {
                    for (RequirementsRow r : rows) {
                        if (row.getCapability().equals(r.getCapability()))
                            tempList.add(r);
                    }
                    map.put(row.getCapability(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            case "Build":
                for (RequirementsRow row : rows) {
                    for (RequirementsRow r : rows) {
                        if (row.getBuild().equals(r.getBuild()))
                            tempList.add(r);
                    }
                    map.put(row.getBuild(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            case "Responsible Individual":
                for (RequirementsRow row : rows) {
                    for (RequirementsRow r : rows) {
                        if (row.getRi().equals(r.getRi()))
                            tempList.add(r);
                    }
                    map.put(row.getRi(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            case "Baseline":
                for (RequirementsRow row : rows) {
                    for (RequirementsRow r : rows) {
                        if (row.getBaseline().equals(r.getBaseline()))
                            tempList.add(r);
                    }
                    map.put(row.getBaseline(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            case "Paragraph/Figure":
                for (RequirementsRow row : rows) {
                    for (RequirementsRow r : rows) {
                        if (row.getParagraph().equals(r.getParagraph()))
                            tempList.add(r);
                    }
                    map.put(row.getParagraph(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            case "Program":
                for (RequirementsRow row : rows) {
                    for (RequirementsRow r : rows) {
                        if (row.getProgram().equals(r.getProgram()))
                            tempList.add(r);
                    }
                    map.put(row.getProgram(), new ArrayList<>(tempList));
                    tempList.clear();
                } break;
            default:
                break;
        }

        return map;
    }

    /**
     * Add the subtotal section to the document. A document may contain multiple subtotal sections
     * depending on how many partitions exist. The group of items the user is wanting to print is
     * broken down into partitions based on the first group choice they choose.
     * @param doc The document to add the subtotal section to.
     * @param partition The group of rows that will be part of this subtotal section.
     * @param isSlocs whether or not the report is Slocs (uses Add/Chg/Del)
     * @return The document with the added subtotal section.
     * @throws DocumentException If the document cannot be changed, possibly due to being open at the same time.
     */
    private static Document addSubtotalSection(Document doc, ArrayList<RequirementsRow> partition, int groupSize, boolean isSlocs) throws DocumentException {

        LineSeparator ls = new LineSeparator(1f, 105f, BaseColor.BLACK, Element.ALIGN_CENTER, -10);
        doc.add(new Chunk((ls)));

        /* Outer table with have rows on left and subtotals on right. */
        PdfPTable outerTable = new PdfPTable(2);
        outerTable.setWidthPercentage(100f);
        outerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        outerTable.setWidths( (groupSize < 4) ? new int[]{60, 35} : new int[]{40, 15} );

        /* Initialize the table with a left alignment. */
        PdfPTable leftTable = new PdfPTable(4);
        leftTable.setWidthPercentage(100f);
        leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        /* Consists of the subtotals. */
        PdfPTable rightTable = new PdfPTable(4);
        rightTable.setWidthPercentage(100f);
        rightTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        /* The word subtotal next to the subtotals. */
        PdfPCell subCell = createTextCell("Subtotal");
        subCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        outerTable.addCell(subCell);

        /* Grab the subtotals. */
        double[] subs = calculateSubtotal(partition, isSlocs);

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
    private static double[] calculateSubtotal(ArrayList<RequirementsRow> partition, boolean isSLOCS)
    {
        /* We have four columns. */
        double[] subs = new double[4];
        int size = partition.size();

        for(int i = 0; i < size; i++)
        {
            RequirementsRow curr = partition.get(i);

            if (isSLOCS)    /* If it is a SLOCs report. */
            {
                /* Total is add, change and deleted added together.                     */
                double total = curr.getAdd() + curr.getChange() + curr.getDelete();
                subs[0] += curr.getAdd();                           /* Add subtotal     */
                subs[1] += curr.getChange();                        /* Change subtotal  */
                subs[2] += curr.getDelete();                        /* Delete subtotal  */
                subs[3] += total;                                   /* Total subtotal   */
            }
            else            /* If it is a D/C/T/I report. */
            {
                //double total = curr.getAdd() + curr.getChange() + curr.getDelete();
                subs[0] += curr.getDesignWeight();                  /* Design subtotal      */
                subs[1] += curr.getCodeWeight();                    /* Code subtotal        */
                subs[2] += curr.getUnitTestWeight();                /* Unit Test subtotal   */
                subs[3] += curr.getIntegrationWeight();             /* Integ. subtotal      */
            }
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

