package ROMdb.Reports;

import ROMdb.Driver.Main;
import ROMdb.Helpers.RequirementsRow;
import ROMdb.Models.RequirementsModel;
import com.itextpdf.text.*;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.Font;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

/**
 * Team Gorillas
 * Created by Anthony Orio on 4/13/2017.
 * Last Modified by Jatin Bhakta on 5/7/2017
 * The purpose of this class is to generate the reports using the
 * open source software iText.
 */
public class ReportGenerator
{
    // private static final FileHandler fileHandler = new FileHandler();

    private static final Font BOLD_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static final Font BOLD_HEADERS = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

    // Global fields needed for header/footer of a report
    private static boolean isLandscape = false;
    private static String header = "";
    private static String footer = "";

    /**
     *     inner class to add header and footer
      */
    public static class HeaderFooterPageEvent extends PdfPageEventHelper
    {
        @Override
        // this method gets called at the beginning of every page of the pdf document
        public void onStartPage(PdfWriter writer, Document document)
        {

            // if header textbox isn't blank
            if (!header.trim().isEmpty())
            {
                if (!isLandscape)
                {
                    // set header for Portrait
                    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(header), 300, 820, 0);
                }
                else
                {
                    // set header for Landscape
                    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(header), 420, 570, 0);
                }
            }
        }

        @Override
        // this method gets called at the end of every page of the pdf document
        public void onEndPage(PdfWriter writer, Document document)
        {
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date());

            if (!isLandscape)
            {
                // set footer for Portrait
                if (!footer.trim().isEmpty())
                {
                    // include footer textbox content
                    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(footer), 300, 20, 0);
                }
                // add timestamp and page number regardless of footer field
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(timeStamp), 75, 20, 0);
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Page " + document.getPageNumber()), 550, 20, 0);
            }
            else
            {
                // set footer for Landscape
                if (!footer.trim().isEmpty())
                {
                    // include footer textbox content
                    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(footer), 450, 20, 0);
                }
                // add timestamp and page number regardless of footer field
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(timeStamp), 75, 20, 0);
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Page " + document.getPageNumber()), 800, 20, 0);
            }
        }
    }


    /**
     * Generates the DDR report for portrait/landscape, lets user choose where to save it
     * @param isPdfLandscape true if the DDR report is to be in Landscape format
     * @throws FileNotFoundException When the file cannot be located.
     * @throws DocumentException If the document is open or cannot be written to.
     */
    public static String generateDDR(boolean isPdfLandscape, String headerContent, String footerContent) throws IOException, DocumentException, InterruptedException
    {
        isLandscape = isPdfLandscape;

        /* Use a file chooser to find the path. */
        // String path = fileHandler.getPathWithFileChooser();
        String path = Main.tempPDFDirectory.toString();

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());

        header = headerContent;
        footer = footerContent;

        /* Instantiate document and its location. */
        Document document = new Document();

        // file name with timestamp
        // String fileName = "/DDRReq_" + timeStamp + ".pdf";

        // String fullPathFileName = path + fileName;

        //PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fullPathFileName));

        String documentFilePath = "";
        String fileName = "";

        /* Change to landscape if there are more than four group selections. */
        if (isLandscape)
        {
            document.setPageSize(PageSize.A4_LANDSCAPE.rotate());
            fileName = "/DDRReqLand_" + timeStamp + ".pdf";
            documentFilePath = path + fileName;
        }
        else
        {
            fileName = "/DDRReqPort_" + timeStamp + ".pdf";
            documentFilePath = path + fileName;
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(documentFilePath));

        // call helper method to start the HeaderFooter event creation
        headerFooter(writer);

         /* Beginning creating the document. */
        document.open();

        /* Line separator */
        document.add( defaultSeparator() );

        /* Line separate */
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

        previewReport(documentFilePath);

        return fileName;

    } // end generateDDR

    /**
     * read the pdf just created to find out how many pages there are then add the "Page X of Y" at the bottom right corner
     * @param writer the PdfWriter that we are using to write to the document
     * @param fullPathFileName full path destination and filename of file just created
     * @throws IOException
     * @throws DocumentException
     */
 /*   public static void addPdfPageCounter(PdfWriter writer, String fullPathFileName) throws IOException, DocumentException
    {
        PdfReader reader = new PdfReader(fullPathFileName);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(fullPathFileName));
        PdfContentByte under = null;
        int totalPages = reader.getNumberOfPages();

        System.out.println("totalPages" + totalPages);
        for (int page = 1; page <= totalPages; page++)
        {
            under = stamper.getUnderContent(page);
            String pageXofY = String.format("Page %d of %d", page, totalPages);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(pageXofY), 550, 30, 0);
        }

        reader.close();
        stamper.close();
    }
*/
    /**
     * helper method to set an instance of inner class to the PdfWriter
     * @param writer for the current document we are writing to
     */
    private static void headerFooter(PdfWriter writer)
    {
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        writer.setPageEvent(event);

    }

    /**
     * Adds column headers to the DDR report
     * @param doc the current document that are we making changes to
     * @param isLandscape true if the report is to be in Landscape format
     * @return The documented that was edited.
     * @throws DocumentException If the document is open or cannot be written to.
     */
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

    /**
     * Add req rows to the document
     * @param doc the current document we are adding to
     * @param rows the rows from the gridview
     * @param isLandscape true if the report is to be in landscape format
     * @return The document that was edited.
     * @throws DocumentException If the document is open or cannot be edited.
     */
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

    /**
     *
     * @param doc the current report we're working in
     * @param partition rows from the gridview
     * @param isLandscape true if the report is to be in Landscape format
     * @return The document that was edited.
     * @throws DocumentException If the document is open or cannot be written to.
     */
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

    /**
     *
     * @param rows The list of rows to partition by SC/ICR.
     * @return A map containing the partition lists.
     */
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
     * Creates the DCTI Report and allows the user to save it to a location
     * on the disk.
     * @param groups The groups chosen to include on the report.
     * @throws FileNotFoundException If the file path cannot be located.
     * @throws DocumentException If the document is open or cannot be written to.
     */
    public static void generateDCTI(ArrayList<String> groups, String headerContent, String footerContent) throws FileNotFoundException, DocumentException
    {
        header = headerContent;
        footer = footerContent;

        /* Use a file chooser to find the path. */
        //String path = fileHandler.getPathWithFileChooser();
        String path = Main.tempPDFDirectory.toString();

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());

        /* Instantiate document and its location. */
        Document document = new Document();
        String documentFilePath = path + "/DCTIStatus_" + timeStamp + ".pdf";
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(documentFilePath));

        /* Change to landscape if there are more than four group selections. */
        if (groups.size() >= 4)
        {
            document.setPageSize(PageSize.A4_LANDSCAPE.rotate());
            isLandscape = true;
        } else {
            isLandscape = false;
        }

        // call helper method to start the HeaderFooter event creation
        headerFooter(writer);

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

        previewReport(documentFilePath);
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
    public static void generateSLOCS(ArrayList<String> groups, String headerContent, String footerContent)
                                                    throws FileNotFoundException, DocumentException
    {
        header = headerContent;
        footer = footerContent;

        /* Get the directory path. */
        // String path = fileHandler.getPathWithFileChooser();
        String path = Main.tempPDFDirectory.toString();

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());

        /* Instantiate the new document with the path chosen. */
        Document document = new Document();
        String documentFilePath = path + "/SLOCSAddChgDel_" + timeStamp + ".pdf";
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(documentFilePath));

        /* Change to landscape view if group size is four or larger. */
        if (groups.size() >= 4)
        {
            document.setPageSize(PageSize.A4_LANDSCAPE.rotate());
            isLandscape = true;
        } else {
            isLandscape = false;
        }

        // call helper method to start the HeaderFooter event creation
        headerFooter(writer);

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

        previewReport(documentFilePath);
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


//    /**
//     * Generates the Header for the report
//     *//*
//    private static void addReportHeader(Document doc, String header)
//    {
//        if (!header.trim().isEmpty())
//        {
//            *//* Create Report Header *//*
//            Paragraph reportHeader = new Paragraph();
//            reportHeader.setAlignment(Element.ALIGN_CENTER);
//            reportHeader.add(new Chunk(header, HDR_FTR));
//            try
//            {
//                doc.add(reportHeader);
//            }
//            catch (DocumentException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

//    /**
//     * Generates the Footer for the report
//     */
//    private static void addReportFooter(Document doc, String footer, String timestamp)
//    {
//        if (!footer.trim().isEmpty())
//        {
//            *//**//* Create Report Footer *//**//*
//            Paragraph reportFooter = new Paragraph();
//            reportFooter.setAlignment(Element.ALIGN_CENTER);
//            reportFooter.add(new Chunk(timestamp, HDR_FTR));
//            reportFooter.add(new Chunk(footer, HDR_FTR));
//            reportFooter.add(new Chunk("Page X of Y", HDR_FTR));
//            try
//            {
//                doc.add(reportFooter);
//            }
//            catch (DocumentException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

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

    /**
     * Opens a report PDF so it can be viewed, saved and printed through the computers default PDF Reader.
     * The report PDF is stored in a temp directory and is deleted when the program closes. The Report should be saved
     * through the PDF reader it is opened with.
     * @param filePath the file path of the file that is being opened and viewed in the system's default PDF Reader
     */
    private static void previewReport(String filePath)
    {

        File reportPDF = new File(filePath);

        // Deletes the temp PDF upon closing the program
        reportPDF.deleteOnExit();

        if(reportPDF.exists()){
            if (Desktop.isDesktopSupported()){
                try {
                    Desktop.getDesktop().open(reportPDF);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"There was an error opening: " + reportPDF.getName(), ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Awt Desktop is not supported!", ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"The file: " + reportPDF.getName() + " does not exist!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}

